package edu.upc.dsa.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightPlan {
    //ATRIBUTOS
    private String droneId;
    private String pilotId;
    private LocalDateTime start;
    private double hours;
    private List<Position> positions;

    //CONSTRUCTOR
    public FlightPlan()
    {
        this.positions = new ArrayList<>();
    }

    public FlightPlan(String droneId, String pilotId, LocalDateTime start, double hours, List<Position> positions)
    {
        this.droneId = droneId;
        this.pilotId = pilotId;
        this.start = start;
        this.hours = hours;
        this.positions = (positions != null) ? new ArrayList<>(positions) : new ArrayList<>(); //si la lista de posiciones es nula, crea una vacía, sino utiliza la de la entrada del constructor
    }

    public boolean overlaps(FlightPlan fp)
    {
        return this.start.isBefore(fp.getEnd()) && fp.getStart().isBefore(this.getEnd());
    }

    @Override
    public String toString()
    {
        return "FlightPlan[drone=" + droneId + ", pilot=" + pilotId + ", start=" + start + ", hours=" + hours + ", positions=" + positions + "]";
    }

    //GETTERS
    public String getDroneId() { return droneId; }

    public String getPilotId() { return pilotId; }

    public LocalDateTime getStart() { return start; }

    public LocalDateTime getEnd()
    {
        return start.plusMinutes((long) (hours * 60));
    }

    public double getHours() { return hours; }

    public List<Position> getPositions() { return positions; }
}
