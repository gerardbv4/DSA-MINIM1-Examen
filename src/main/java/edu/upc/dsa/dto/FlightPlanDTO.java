package edu.upc.dsa.dto;

import edu.upc.dsa.domain.models.FlightPlan;
import edu.upc.dsa.domain.models.Position;

import java.util.ArrayList;
import java.util.List;

public class FlightPlanDTO {
    private String droneId;
    private String pilotId;
    private String start;
    private double hours;
    private List<Position> positions = new ArrayList<>();

    public FlightPlanDTO() {}

    public FlightPlanDTO(FlightPlan fp) {
        this.droneId = fp.getDroneId();
        this.pilotId = fp.getPilotId();
        this.start = fp.getStart().toString();
        this.hours = fp.getHours();
        this.positions = new ArrayList<>(fp.getPositions());
    }

    public String getDroneId() { return droneId; }
    public void setDroneId(String droneId) { this.droneId = droneId; }

    public String getPilotId() { return pilotId; }
    public void setPilotId(String pilotId) { this.pilotId = pilotId; }

    public String getStart() { return start; }
    public void setStart(String start) { this.start = start; }

    public double getHours() { return hours; }
    public void setHours(double hours) { this.hours = hours; }

    public List<Position> getPositions() { return positions; }
    public void setPositions(List<Position> positions) { this.positions = positions; }
}
