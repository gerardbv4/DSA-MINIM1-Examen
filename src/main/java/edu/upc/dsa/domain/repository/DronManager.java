package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Dron;
import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.models.Pilot;
import edu.upc.dsa.domain.models.Position;

import java.time.LocalDateTime;
import java.util.List;

public interface DronManager {
    void addPilot(String id, String name, String surname);
    void addDron(String id, String name, String fabricante, String modelo);

    void clear();  // SINGLETOOONNN DECLARAR LA FUNCIÓN EN LA INSTANCIA

    List<Dron> getDronsByFlightHours();
    List<Pilot> getPilotsByFlightHours();

    List<FlightPlan> getFlightPlansByPilot(String idPilot);
    List<FlightPlan> getFlightPlansByDron (String idDron);

    void storeDron(String id);
    Dron repairDron();

    Dron getDron(String id);
    Pilot getPilot(String id);

    int numDrons();
    int numPilots();
    int numFlightPlans();

    FlightPlan createFlightPlan (String idDron, String idPilot, LocalDateTime start,  double hours, List<Position> positions);

    int size();

    List<Dron> findAll();

    //void deleteDron(String id);

    //Dron updateDron(Dron dron);
}
