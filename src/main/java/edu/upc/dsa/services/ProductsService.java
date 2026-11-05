package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.Dron;

import edu.upc.dsa.domain.repository.DronManager;

import edu.upc.dsa.domain.repository.DronManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import edu.upc.dsa.util.RandomUtils;

@Api(value = "/products", description = "Endpoint to Product Service")
@Path("/products")
public class ProductsService {

    private DronManager pm;

    public ProductsService() {
        this.pm = DronManagerImpl.getInstance();
    }

    @GET
    @ApiOperation(value = "get all Products", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Dron.class, responseContainer="List"),
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {

        List<Dron> drons = this.pm.findAll();

        GenericEntity<List<Dron>> entity = new GenericEntity<List<Dron>>(drons) {};
        return Response.status(200).entity(entity).build();

    }

    @GET
    @ApiOperation(value = "get a Product", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Dron.class),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("id") String id) {
        Dron p = this.pm.getProduct(id);
        if (p == null) return Response.status(404).build();
        else  return Response.status(200).entity(p).build();
    }

    @DELETE
    @ApiOperation(value = "delete a Product", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 404, message = "Product not found")
    })

    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") String id) {
        Dron p = this.pm.getProduct(id);
        if (p == null) return Response.status(404).build();
        else this.pm.deleteProduct(id);
        return Response.status(200).build();
    }

    @PUT
    @ApiOperation(value = "update a Product", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @Path("/")
    public Response updateProduct(Dron dron) {

        Dron p = this.pm.updateProduct(dron);

        if (p == null) return Response.status(404).build();

        return Response.status(200).build();
    }

    @POST
    @ApiOperation(value = "create a new Product", notes = "If no id is sent, a random one is generated")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Dron.class),
            @ApiResponse(code = 409, message = "A product with that id already exists"),
            @ApiResponse(code = 500, message = "Validation Error")
    })

    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newProduct(Dron dron) {

        if (dron.getName()==null) return Response.status(500).entity(dron).build();

        if (dron.getId()==null || dron.getId().trim().isEmpty()) {
            dron.setId(RandomUtils.getId());
        }

        if (this.pm.getProduct(dron.getId()) != null) {
            return Response.status(409).build();
        }

        this.pm.addProduct(dron.getId(), dron.getName(), dron.getPrice());
        return Response.status(201).entity(dron).build();
    }

    @GET
    @ApiOperation(value = "get all Products sorted by price", notes = "Highest price first")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Dron.class, responseContainer="List")
    })
    @Path("/byPrice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByPrice() {
        List<Dron> drons = this.pm.getProductsByPrice();
        GenericEntity<List<Dron>> entity = new GenericEntity<List<Dron>>(drons) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all Products sorted by sales", notes = "Most sold first")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Dron.class, responseContainer="List")
    })
    @Path("/bySales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsBySales() {
        List<Dron> drons = this.pm.getProductsBySales();
        GenericEntity<List<Dron>> entity = new GenericEntity<List<Dron>>(drons) {};
        return Response.status(200).entity(entity).build();
    }

}