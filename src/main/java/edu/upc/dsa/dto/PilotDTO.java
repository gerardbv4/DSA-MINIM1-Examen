package edu.upc.dsa.dto;

import edu.upc.dsa.domain.models.Pilot;

public class PilotDTO {
    private String id;
    private String name;
    private String surname;
    private double hours;

    public PilotDTO() {}

    public PilotDTO(Pilot p) {
        this.id = p.getId();
        this.name = p.getName();
        this.surname = p.getSurname();
        this.hours = p.getHours();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public double getHours() { return hours; }
    public void setHours(double hours) { this.hours = hours; }
}