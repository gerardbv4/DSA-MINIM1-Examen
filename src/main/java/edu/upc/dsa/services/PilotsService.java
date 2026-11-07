package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.models.Pilot;
import edu.upc.dsa.domain.repository.DronManager;
import edu.upc.dsa.domain.repository.DronManagerImpl;
import edu.upc.dsa.dto.FlightPlanDTO;
import edu.upc.dsa.dto.PilotDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/pilots", description = "Endpoint to Pilots Service")
@Path("/pilots")
public class PilotsService {

    private DronManager dm;

    public PilotsService() {
        this.dm = DronManagerImpl.getInstance();
    }

    @POST
    @ApiOperation(value = "añade un nuevo Pilot (addPilot)", notes = "se requiere: id, name and surname")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pilot creado", response = PilotDTO.class),
            @ApiResponse(code = 400, message = "Faltan datos"),
            @ApiResponse(code = 409, message = "El Pilot con ese id ya existe")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPilot(PilotDTO dto) {
        if (dto == null || isBlank(dto.getId()) || isBlank(dto.getName()) || isBlank(dto.getSurname())) {
            return Response.status(400).build();
        }
        if (this.dm.getPilot(dto.getId()) != null)
        {
            return Response.status(409).build();
        }
        this.dm.addPilot(dto.getId(), dto.getName(), dto.getSurname());
        return Response.status(201).entity(new PilotDTO(this.dm.getPilot(dto.getId()))).build();
    }

    @GET
    @ApiOperation(value = "Obtener los pilotos ordenados por horas de vuelo (getPilotsByFlightHours)", notes = "De más a menos cantidad de horas de vuelo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Éxito", response = PilotDTO.class, responseContainer = "List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPilots() {
        List<PilotDTO> result = new ArrayList<>();
        for (Pilot p : this.dm.getPilotsByFlightHours()) {
            result.add(new PilotDTO(p));
        }
        GenericEntity<List<PilotDTO>> entity = new GenericEntity<List<PilotDTO>>(result) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "obtener los flightplans de un Pilot (getFlightPlansByPilot)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Éxito", response = FlightPlanDTO.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Pilot no encontrado")
    })
    @Path("/{id}/flightplans")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFlightPlans(@PathParam("id") String id) {
        if (this.dm.getPilot(id) == null) {
            return Response.status(404).build();
        }
        List<FlightPlanDTO> result = new ArrayList<>();
        for (FlightPlan fp : this.dm.getFlightPlansByPilot(id)) {
            result.add(new FlightPlanDTO(fp));
        }
        GenericEntity<List<FlightPlanDTO>> entity = new GenericEntity<List<FlightPlanDTO>>(result) {};
        return Response.status(200).entity(entity).build();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}