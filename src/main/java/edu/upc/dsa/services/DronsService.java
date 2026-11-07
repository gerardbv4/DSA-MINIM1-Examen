package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.Dron;
import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.repository.DronManager;
import edu.upc.dsa.domain.repository.DronManagerImpl;
import edu.upc.dsa.dto.DronDTO;
import edu.upc.dsa.dto.FlightPlanDTO;
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

@Api(value = "/drons", description = "Endpoint to Drons Service")
@Path("/drons")
public class DronsService {

    private DronManager dm;

    public DronsService() {
        this.dm = DronManagerImpl.getInstance();
    }

    @POST
    @ApiOperation(value = "añade un nuevo Dron (addDron)", notes = "se requiere: id, name, fabricante y modelo")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Dron creado", response = DronDTO.class),
            @ApiResponse(code = 400, message = "Faltan datos"),
            @ApiResponse(code = 409, message = "El Dron con ese id ya existe")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDron(DronDTO dto) {
        if (dto == null || isBlank(dto.getId()) || isBlank(dto.getName()) || isBlank(dto.getFabricante()) || isBlank(dto.getModelo())) {
            return Response.status(400).build();
        }
        if (this.dm.getDron(dto.getId()) != null) {
            return Response.status(409).build();
        }
        this.dm.addDron(dto.getId(), dto.getName(), dto.getFabricante(), dto.getModelo());
        return Response.status(201).entity(new DronDTO(this.dm.getDron(dto.getId()))).build();
    }

    @GET
    @ApiOperation(value = "Obtener los drones ordenados por horas de vuelo (getDronsByFlightHours)", notes = "De más a menos cantidad de horas de vuelo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Éxito", response = DronDTO.class, responseContainer = "List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrons() {
        List<DronDTO> result = new ArrayList<>();
        for (Dron d : this.dm.getDronsByFlightHours()) {
            result.add(new DronDTO(d));
        }
        GenericEntity<List<DronDTO>> entity = new GenericEntity<List<DronDTO>>(result) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "guarda un Dron en el almacén (storeDron)", notes = "Dron almacenado en la posición más cercana a la puerta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dron almacenado"),
            @ApiResponse(code = 404, message = "Dron no encontrado"),
            @ApiResponse(code = 409, message = "The Dron ya está en el almacén")
    })
    @Path("/{id}/store")
    public Response storeDron(@PathParam("id") String id) {
        Dron d = this.dm.getDron(id);
        if (d == null) {
            return Response.status(404).build();
        }
        if (d.isInAlmacen()) {
            return Response.status(409).build();
        }
        this.dm.storeDron(id);
        return Response.status(200).build();
    }

    @POST
    @ApiOperation(value = "reparar el Dron más cercano a la puerta del almacén (repairDron)", notes = "El dron reparado abandona el almacén y vuelve a estar disponible para ser usado")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dron reparado", response = DronDTO.class),
            @ApiResponse(code = 404, message = "El almacén está vacío")
    })
    @Path("/repair")
    @Produces(MediaType.APPLICATION_JSON)
    public Response repairDron() {
        Dron d = this.dm.repairDron();
        if (d == null) {
            return Response.status(404).build();
        }
        return Response.status(200).entity(new DronDTO(d)).build();
    }

    @GET
    @ApiOperation(value = "obtener los flightplans de un Dron (getFlightPlansByDron)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Éxito", response = FlightPlanDTO.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Dron no encontrado")
    })
    @Path("/{id}/flightplans")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFlightPlans(@PathParam("id") String id) {
        if (this.dm.getDron(id) == null)
        {
            return Response.status(404).build();
        }
        List<FlightPlanDTO> result = new ArrayList<>();
        for (FlightPlan fp : this.dm.getFlightPlansByDron(id))
        {
            result.add(new FlightPlanDTO(fp));
        }
        GenericEntity<List<FlightPlanDTO>> entity = new GenericEntity<List<FlightPlanDTO>>(result) {};
        return Response.status(200).entity(entity).build();
    }

    private static boolean isBlank(String s)
    {
        return s == null || s.trim().isEmpty();
    }
}
