package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.repository.DronManager;
import edu.upc.dsa.domain.repository.DronManagerImpl;
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
import java.util.Map;

@Api(value = "/orders", description = "Endpoint to Orders Service")
@Path("/orders")

public class OrdersService {
    private DronManager pm;

    public OrdersService() {
        this.pm = DronManagerImpl.getInstance();
    }

    @POST
    @ApiOperation(value = "create a new Order", notes = "dni of the user and listProducts (product id -> quantity)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Order created", response = FlightPlan.class),
            @ApiResponse(code = 400, message = "Invalid order"),
            @ApiResponse(code = 404, message = "User or product not found")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newOrder(FlightPlan flightPlan) {
        if (flightPlan == null || flightPlan.getDNI() == null
                || flightPlan.getListProducts() == null || flightPlan.getListProducts().isEmpty()) {
            return Response.status(400).build();
        }
        if (this.pm.getUser(flightPlan.getDNI()) == null) {
            return Response.status(404).build();
        }
        for (Map.Entry<String, Integer> line : flightPlan.getListProducts().entrySet()) {
            if (line.getValue() == null || line.getValue() <= 0) {
                return Response.status(400).build();
            }
            if (this.pm.getProduct(line.getKey()) == null) {
                return Response.status(404).build();
            }
        }
        this.pm.addOrder(flightPlan);
        return Response.status(201).entity(flightPlan).build();
    }

    @POST
    @ApiOperation(value = "serve the oldest pending Order", notes = "Orders are served in arrival order (FIFO)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order served", response = FlightPlan.class),
            @ApiResponse(code = 404, message = "No pending orders")
    })
    @Path("/deliver")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deliverOrder() {
        FlightPlan flightPlan = this.pm.deliverOrder();
        if (flightPlan == null) {
            return Response.status(404).build();
        }
        return Response.status(200).entity(flightPlan).build();
    }
}
