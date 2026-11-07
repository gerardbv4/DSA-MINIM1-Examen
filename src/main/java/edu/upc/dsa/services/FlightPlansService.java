package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.Dron;
import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.models.Pilot;
import edu.upc.dsa.domain.repository.DronManager;
import edu.upc.dsa.domain.repository.DronManagerImpl;
import edu.upc.dsa.dto.FlightPlanDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Api(value = "/flightplans", description = "Endpoint to FlightPlans Service")
@Path("/flightplans")
public class FlightPlansService {

    private DronManager dm;

    public FlightPlansService() {
        this.dm = DronManagerImpl.getInstance();
    }

    @POST
    @ApiOperation(value = "crear un nuevo FlightPlan (addFlightPlan)",
            notes = "El inicio  debe tener el formato de una fecha ISO, por ejemplo 2026-11-05T09:30. Horas es la duración estimada")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "FlightPlan creado", response = FlightPlanDTO.class),
            @ApiResponse(code = 400, message = "Datos no válidos"),
            @ApiResponse(code = 404, message = "Dron o Pilot no encontrado"),
            @ApiResponse(code = 409, message = "Dron en el almacén, o Dron o Pilot ocupados en ese intervalo")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFlightPlan(FlightPlanDTO dto)
    {
        if (dto == null || isBlank(dto.getDroneId()) || isBlank(dto.getPilotId()) || isBlank(dto.getStart()) || dto.getHours() <= 0)
        {
            return error(400, "Datos no válidos: hacen falta droneId, pilotId, start y hours mayor que 0");
        }

        LocalDateTime start;
        try {
            start = LocalDateTime.parse(dto.getStart());
        }
        catch (DateTimeParseException e) {
            return error(400, "La fecha no es válida. Formato esperado: 2026-11-05T09:30");
        }

        Dron d = this.dm.getDron(dto.getDroneId());
        if (d == null)
        {
            return error(404, "No existe el dron " + dto.getDroneId());
        }

        Pilot p = this.dm.getPilot(dto.getPilotId());
        if (p == null) {
            return error(404, "No existe el piloto " + dto.getPilotId());
        }

        if (d.isInAlmacen()) {
            return error(409, "El dron " + dto.getDroneId() + " está en el almacén para mantenimiento");
        }

        FlightPlan fp = this.dm.createFlightPlan(dto.getDroneId(), dto.getPilotId(), start,
                dto.getHours(), dto.getPositions());
        if (fp == null) {
            return error(409, "El dron o el piloto ya tienen un plan de vuelo en ese intervalo");
        }

        return Response.status(201).entity(new FlightPlanDTO(fp)).build();
    }

    private static Response error(int status, String message) {
        return Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}