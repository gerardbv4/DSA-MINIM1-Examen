package edu.upc.dsa;

import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import edu.upc.dsa.domain.repository.DronManager;
import edu.upc.dsa.domain.repository.DronManagerImpl;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/dsaApp/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in edu.upc.dsa package
        final ResourceConfig rc = new ResourceConfig().packages("edu.upc.dsa.services");

        rc.register(io.swagger.jaxrs.listing.ApiListingResource.class);
        rc.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        BeanConfig beanConfig = new BeanConfig();

        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/dsaApp");
        beanConfig.setContact("support@example.com");
        beanConfig.setDescription("REST API for Tracks Manager");
        beanConfig.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");
        beanConfig.setResourcePackage("edu.upc.dsa.services");
        beanConfig.setTermsOfServiceUrl("http://www.example.com/resources/eula");
        beanConfig.setTitle("REST API");
        beanConfig.setVersion("1.0.0");
        beanConfig.setScan(true);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
    /*
    public static void loadInitialData() {
        DronManager pm = DronManagerImpl.getInstance();
        if (pm.size() == 0) {
            pm.addProduct("C1", "Coca-cola zero", 2);
            pm.addProduct("C2", "Coca-cola", 2.5);
            pm.addProduct("B1", "Lomo queso", 3);
            pm.addProduct("B2", "bacon queso", 3.5);

            pm.addUser("NIF1", "El Chaval", "de la Peca", "chaval.peca@gmail.com");
            pm.addUser("NIF2", "Manolo", "Vinilo", "manolo.vinilo@gmail.com");
            pm.addUser("NIF3", "Paco", "Synth", "paco.synth@gmail.com");
            pm.addUser("NIF4", "Rafa", "Theremín", "rafa.theremin@gmail.com");
        }
    }
     */

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //loadInitialData();
        final HttpServer server = startServer();

        StaticHttpHandler staticHttpHandler = new StaticHttpHandler("./public/");
        server.getServerConfiguration().addHttpHandler(staticHttpHandler, "/");


        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));

        System.in.read();
        server.stop();
    }
}

