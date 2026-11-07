package edu.upc.dsa.domain.repository;
import edu.upc.dsa.domain.models.Dron;
import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.models.Pilot;
import edu.upc.dsa.domain.models.Position;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Comparator;

public class DronManagerImpl implements DronManager {
    private static DronManagerImpl instance; // SINGLETOONNN ->INSTANCIA PRIVADA ESTÁTICA

    private HashMap<String, Dron> drons;
    private HashMap<String, Pilot> pilots;
    private Deque<Dron> almacen;
    private List<FlightPlan> flightPlanList;
    final static Logger logger = Logger.getLogger(DronManagerImpl.class);

    private DronManagerImpl() {
        drons = new HashMap<>();
        pilots = new HashMap<>();
        flightPlanList = new ArrayList<>();
        almacen = new LinkedList<>();
    }

    public static DronManagerImpl getInstance()
    {
        if (instance == null) instance = new DronManagerImpl();
        return instance;
    }

    public void clear()  //SINGLETONNN !! FUNCIÓN PARA BORRAR CACHÉ DE LAS ESTRUCTURAS
    {
        logger.info("clear()");
        drons.clear();
        pilots.clear();
        flightPlanList.clear();
        almacen.clear();
        logger.info("clear -> estructuras vaciadas");
    }

    @Override
    public void addDron(String id, String name, String fabricante, String modelo) {
        logger.info("addDron(id=" + id + ", name=" + name + ", fabricante=" + fabricante + ", modelo=" + modelo + ")");
        if (this.drons.containsKey(id)) {
            logger.warn("addDron -> el dron " + id + " ya existía y se sobrescribe");
        }
        Dron d = new Dron(id, name, fabricante, modelo);
        this.drons.put(id, d);
        logger.info("addDron -> dron añadido, total drons: " + drons.size());
    }

    public void addPilot(String id, String name, String surname) {
        logger.info("addPilot(id=" + id + ", name=" + name + ", surname=" + surname + ")");
        if (this.pilots.containsKey(id)) {
            logger.warn("addPilot -> el pilot " + id + " ya existía y se sobrescribe");
        }
        Pilot p = new Pilot(id, name, surname);
        this.pilots.put(id, p);
        logger.info("addPilot -> pilot añadido, total pilots: " + pilots.size());
    }

    //COMPARATOR
    /*
    static Comparator<Product> CMP_PRODUCT_1 = new  Comparator<Product>() {

        @Override
        public int compare(Product p1, Product p2) {
            return Double.compare(p1.getPrice(), p2.getPrice());
        }
    };
    static Comparator<Product> CMP_PRODUCT_1_REVERSED =
            Comparator.comparingDouble(Product::getPrice).reversed(); //ORDRE DESCENDENT
     */

    @Override
    public List<Dron> getDronsByFlightHours() {
        logger.info("getDronsByFlightHours()");
        List<Dron> sortedList = new ArrayList<>(this.drons.values());
        sortedList.sort(Comparator.comparingDouble(Dron::getHours).reversed().thenComparing(Dron::getId));
        logger.info("getDronsByFlightHours -> " + sortedList.size() + " drons ordenados por flight hours");
        return sortedList;
    }

    @Override
    public List<Pilot> getPilotsByFlightHours()
    {
        logger.info("getPilotsByFlightHours()");
        List<Pilot> sortedList = new ArrayList<>(this.pilots.values());
        sortedList.sort(Comparator.comparingDouble(Pilot::getHours).reversed().thenComparing(Pilot::getId));
        logger.info("getPilotsByFlightHours -> " + sortedList.size() + " pilots ordenados por flight hours");
        return sortedList;
    }

    @Override
    public List<FlightPlan> getFlightPlansByPilot(String idPilot) {
        logger.info("getFlightPlansByPilot(idPilot=" + idPilot + ")");
        Pilot p = this.pilots.get(idPilot);
        if (p == null) {
            logger.warn("getFlightPlansByPilot -> no existe el pilot " + idPilot);
            return new ArrayList<>();
        }
        List<FlightPlan> flightPlans = new ArrayList<>(p.getFlightPlans());
        logger.info("getFlightPlansByPilot -> " + flightPlans.size() + " planes del pilot " + idPilot);
        return flightPlans;
    }

    public List<FlightPlan> getFlightPlansByDron(String idDron) {
        logger.info("getFlightPlansByDron(idDron=" + idDron + ")");
        Dron d = this.drons.get(idDron);
        if (d == null) {
            logger.warn("getFlightPlansByDron -> no existe el dron " + idDron);
            return new ArrayList<>();
        }
        List<FlightPlan> flightPlans = new ArrayList<>(d.getFlightPlans());
        logger.info("getFlightPlansByDron -> " + flightPlans.size() + " planes del dron " + idDron);
        return flightPlans;
    }
    @Override
    public void storeDron(String id) {
        logger.info("storeDron(id=" + id + ")");
        Dron d = this.drons.get(id);
        if (d == null) {
            logger.warn("storeDron -> no existe el dron " + id);
            return;
        }
        if (d.isInAlmacen()) {
            logger.error("storeDron -> el dron " + id + " ya está en el almacén");
            return;
        }
        almacen.push(d);
        d.setInAlmacen(true);
        logger.info("storeDron -> dron " + id + " guardado, drones en el almacén: " + almacen.size());
    }

    @Override
    public Dron repairDron() {
        logger.info("repairDron()");
        Dron d = this.almacen.poll();
        if (d == null) {
            logger.warn("repairDron -> el almacén está vacío, no hay ningún dron que reparar");
            return null;
        }
        d.setInAlmacen(false);
        logger.info("repairDron -> dron " + d.getId() + " reparado y disponible, drones en el almacén: " + almacen.size());
        return d;
    }


    @Override
    public Dron getDron(String id) {
        logger.info("getDron(id=" + id + ")");
        Dron d = this.drons.get(id);
        if (d == null) {
            logger.warn("getDron -> no existe el dron " + id);
            return null;
        }
        logger.info("getDron -> encontrado " + d);
        return d;
    }

    @Override
    public Pilot getPilot(String id) {
        logger.info("getPilot(id=" + id + ")");
        Pilot p = this.pilots.get(id);
        if (p == null) {
            logger.warn("getPilot -> no existe el pilot " + id);
        } else {
            logger.info("getPilot -> encontrado " + p);
        }
        return p;
    }

    @Override
    public int numDrons() {

        logger.info("numDrons()");
        int n = drons.size();
        logger.info("numDrons -> " + n);
        return n;
    }

    @Override //igual que el de arriba, pero lo tenía así en la cafetería
    public int size() {
        logger.info("size()");
        int n = drons.size();
        logger.info("size -> " + n);
        return n;
    }

    public int numPilots() {

        logger.info("numPilots()");
        int n = pilots.size();
        logger.info("numPilots -> " + n);
        return n;
    }

    public int numFlightPlans() {

        logger.info("numFlightPlans()");
        int n = flightPlanList.size();
        logger.info("numFlightPlans -> " + n);
        return n;
    }
    @Override
    public FlightPlan createFlightPlan(String droneId, String pilotId, LocalDateTime start, double hours, List<Position> positions) {
        logger.info("createFlightPlan(droneId=" + droneId + ", pilotId=" + pilotId + ", start=" + start + ", hours=" + hours + ", positions=" + positions + ")");

        if (start == null || hours <= 0) {
            logger.error("createFlightPlan -> fecha nula o duración no positiva");
            return null;
        }

        Dron d = this.drons.get(droneId);
        if (d == null) {
            logger.error("createFlightPlan -> no existe el dron " + droneId);
            return null;
        }

        Pilot p = this.pilots.get(pilotId);
        if (p == null) {
            logger.error("createFlightPlan -> no existe el pilot " + pilotId);
            return null;
        }

        if (d.isInAlmacen()) {
            logger.error("createFlightPlan -> el dron " + droneId + " está en el almacén y no puede ser usado");
            return null;
        }

        FlightPlan plan = new FlightPlan(droneId, pilotId, start, hours, positions);

        for (FlightPlan fp : d.getFlightPlans()) {
            if (fp.overlaps(plan)) {
                logger.error("createFlightPlan -> el dron " + droneId + " ya tiene un plan en ese intervalo");
                return null;
            }
        }

        for (FlightPlan fp : p.getFlightPlans()) {
            if (fp.overlaps(plan)) {
                logger.error("createFlightPlan -> el piloto " + pilotId + " ya tiene un plan en ese intervalo");
                return null;
            }
        }

        d.addFlightPlan(plan);
        p.addFlightPlan(plan);
        d.addHours(hours);
        p.addHours(hours);

        flightPlanList.add(plan);
        logger.info("createFlightPlan -> flightplan creado: " + plan);
        return plan;
    }

    /*
    @Override
    public void deleteDron(String id) {
        logger.info("deleteDron(id=" + id + ")");
        Dron d = this.drons.get(id);
        if (d == null) {
            logger.warn("deleteDron -> no existe el dron " + id + ", no se borra nada");
            return;
        }

        if (d.isInAlmacen()) {
            this.almacen.remove(d);
        }

        for (FlightPlan fp : d.getFlightPlans()) {
            Pilot p = this.pilots.get(fp.getPilotId());
            if (p != null) {
                p.getFlightPlans().remove(fp);
                p.addHours(-fp.getHours());
            }
        }

        this.drons.remove(id);
        logger.info("deleteDron -> dron " + id + " borrado, total drons: " + drons.size());
    }
    */

    /*
    @Override
    public Dron updateProduct(Dron dron) {
        if (dron == null) {
            logger.fatal("updateProduct -> el producto recibido es null");
            return null;
        }
        logger.info("updateProduct(id=" + dron.getId() + ", name=" + dron.getName() + ", price=" + dron.getPrice() + ")");
        Dron p = this.getProduct(dron.getId());
        if (p == null) {
            logger.warn("updateProduct -> no existe el producto " + dron.getId());
            return null;
        }
        p.setPrice(dron.getPrice());
        p.setName(dron.getName());
        logger.info("updateProduct -> producto " + p.getId() + " actualizado: name=" + p.getName() + ", price=" + p.getPrice());
        return p;
    }

     */

    @Override
    public List<Dron> findAll() {
        logger.info("findAll()");
        List<Dron> dlist = new ArrayList<>(this.drons.values());
        logger.info("findAll -> " + dlist.size() + " drons");
        return dlist;
    }
}
