package edu.upc.dsa;

import edu.upc.dsa.domain.models.Dron;
import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.models.Pilot;
import edu.upc.dsa.domain.models.Position;
import edu.upc.dsa.domain.repository.DronManager;
import edu.upc.dsa.domain.repository.DronManagerImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DronManagerTest {

    private DronManager dm;
    private List<Position> route;

    // Día fijo de pruebas; at(10) = 10:00 de ese día
    private LocalDateTime at(int hour) {
        return LocalDateTime.of(2026, 11, 5, hour, 0);
    }

    @Before
    public void setUp() {
        dm = DronManagerImpl.getInstance();

        dm.addDron("D1", "Espinete", "F1", "Espinete 3");
        dm.addDron("D2", "ChavalPeca", "F1", "ChavalPeca 1");
        dm.addDron("D3", "Gerard", "UPC", "Anafi USA");

        dm.addPilot("P1", "Gerard", "Barrallo");
        dm.addPilot("P2", "Izan", "Díaz");
        dm.addPilot("P3", "Toni", "Oller");

        route = Arrays.asList(new Position(41.27, 1.98), new Position(41.30, 2.00));
    }

    @After
    public void tearDown() {
        dm.clear();   // el Singleton conserva el estado entre tests: hay que vaciarlo
    }

    @Test
    public void addDronAndPilotTest() {
        Assert.assertEquals(3, dm.numDrons());
        Assert.assertEquals(3, dm.numPilots());

        dm.addDron("D4", "Bebop", "Parrot", "Bebop 2");
        dm.addPilot("P4", "Pau", "Roca");

        Assert.assertEquals(4, dm.numDrons());
        Assert.assertEquals(4, dm.numPilots());
        Assert.assertEquals("D4", dm.getDron("D4").getId());
        Assert.assertEquals("P4", dm.getPilot("P4").getId());

        // los datos de setUp se han guardado bien
        Assert.assertEquals("Espinete", dm.getDron("D1").getName());
        Assert.assertEquals("Gerard", dm.getPilot("P1").getName());

        // ids inexistentes: null
        Assert.assertNull(dm.getDron("XXX"));
        Assert.assertNull(dm.getPilot("XXX"));
    }

    @Test
    public void storeAndRepairDronTest() {
        dm.storeDron("D1");
        dm.storeDron("D2");
        dm.storeDron("D3");
        Assert.assertTrue(dm.getDron("D2").isInAlmacen());

        dm.storeDron("D1");   // repetido: no debe entrar otra vez en la pila

        // LIFO: sale primero el último que entró (el más cercano a la puerta)
        Dron r1 = dm.repairDron();
        Assert.assertEquals("D3", r1.getId());
        Assert.assertFalse(r1.isInAlmacen());

        Dron r2 = dm.repairDron();
        Assert.assertEquals("D2", r2.getId());

        Dron r3 = dm.repairDron();
        Assert.assertEquals("D1", r3.getId());

        // almacén vacío (también demuestra que D1 no se duplicó)
        Assert.assertNull(dm.repairDron());
    }

    @Test
    public void createFlightPlanTest() {
        // caso correcto: D1 y P1, de 10:00 a 12:00
        FlightPlan fp1 = dm.createFlightPlan("D1", "P1", at(10), 2, route);
        Assert.assertNotNull(fp1);
        Assert.assertEquals(2.0, dm.getDron("D1").getHours(), 0.001);
        Assert.assertEquals(2.0, dm.getPilot("P1").getHours(), 0.001);

        // el dron D1 ya está ocupado de 11:00 a 12:00
        Assert.assertNull(dm.createFlightPlan("D1", "P2", at(11), 1, route));

        // el piloto P1 ya está ocupado de 11:00 a 12:00
        Assert.assertNull(dm.createFlightPlan("D2", "P1", at(11), 1, route));

        // un fallo no debe modificar nada
        Assert.assertEquals(0.0, dm.getPilot("P2").getHours(), 0.001);
        Assert.assertEquals(0.0, dm.getDron("D2").getHours(), 0.001);

        // plan que empieza justo cuando termina el anterior: no hay solape
        FlightPlan fp2 = dm.createFlightPlan("D1", "P1", at(12), 1, route);
        Assert.assertNotNull(fp2);
        Assert.assertEquals(3.0, dm.getDron("D1").getHours(), 0.001);

        // dron o piloto inexistentes
        Assert.assertNull(dm.createFlightPlan("XXX", "P2", at(15), 1, route));
        Assert.assertNull(dm.createFlightPlan("D2", "XXX", at(15), 1, route));

        // dron en el almacén (en mantenimiento)
        dm.storeDron("D3");
        Assert.assertNull(dm.createFlightPlan("D3", "P2", at(15), 1, route));

        // datos no válidos
        Assert.assertNull(dm.createFlightPlan("D2", "P2", at(15), 0, route));
        Assert.assertNull(dm.createFlightPlan("D2", "P2", null, 1, route));

        // solo se crearon los dos planes correctos
        Assert.assertEquals(2, dm.numFlightPlans());
    }

    @Test
    public void dronsByFlightHoursTest() {
        // sin horas, los empates salen ordenados por id
        List<Dron> l0 = dm.getDronsByFlightHours();
        Assert.assertEquals("D1", l0.get(0).getId());
        Assert.assertEquals("D2", l0.get(1).getId());
        Assert.assertEquals("D3", l0.get(2).getId());

        dm.createFlightPlan("D1", "P1", at(10), 3, route);
        dm.createFlightPlan("D2", "P2", at(10), 1, route);
        dm.createFlightPlan("D3", "P3", at(10), 5, route);

        // descendente por horas: D3 (5), D1 (3), D2 (1)
        List<Dron> l = dm.getDronsByFlightHours();
        Assert.assertEquals(3, l.size());
        Assert.assertEquals("D3", l.get(0).getId());
        Assert.assertEquals("D1", l.get(1).getId());
        Assert.assertEquals("D2", l.get(2).getId());
    }

    @Test
    public void pilotsByFlightHoursTest() {
        dm.createFlightPlan("D1", "P1", at(10), 2, route);
        dm.createFlightPlan("D2", "P1", at(13), 1, route);
        dm.createFlightPlan("D3", "P2", at(10), 4, route);

        // P1 = 3 h, P2 = 4 h, P3 = 0 h
        List<Pilot> l = dm.getPilotsByFlightHours();
        Assert.assertEquals(3, l.size());
        Assert.assertEquals("P2", l.get(0).getId());
        Assert.assertEquals("P1", l.get(1).getId());
        Assert.assertEquals("P3", l.get(2).getId());
    }

    @Test
    public void flightPlansByPilotAndDronTest() {
        dm.createFlightPlan("D1", "P1", at(10), 2, route);
        dm.createFlightPlan("D2", "P1", at(13), 1, route);
        dm.createFlightPlan("D1", "P2", at(14), 1, route);

        List<FlightPlan> byP1 = dm.getFlightPlansByPilot("P1");
        Assert.assertEquals(2, byP1.size());
        Assert.assertEquals("D1", byP1.get(0).getDroneId());
        Assert.assertEquals("D2", byP1.get(1).getDroneId());

        Assert.assertEquals(1, dm.getFlightPlansByPilot("P2").size());
        Assert.assertEquals(0, dm.getFlightPlansByPilot("P3").size());

        Assert.assertEquals(2, dm.getFlightPlansByDron("D1").size());
        Assert.assertEquals(1, dm.getFlightPlansByDron("D2").size());
        Assert.assertEquals(0, dm.getFlightPlansByDron("D3").size());

        // id inexistente: lista vacía, nunca null
        Assert.assertEquals(0, dm.getFlightPlansByPilot("XXX").size());
        Assert.assertEquals(0, dm.getFlightPlansByDron("XXX").size());
    }
}
