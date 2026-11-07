package edu.upc.dsa.dto;

import edu.upc.dsa.domain.models.Dron;

public class DronDTO {
    private String id;
    private String name;
    private String fabricante;
    private String modelo;
    private double hours;
    private boolean inAlmacen;

    public DronDTO() {}

    public DronDTO(Dron d) {
        this.id = d.getId();
        this.name = d.getName();
        this.fabricante = d.getFabricante();
        this.modelo = d.getModelo();
        this.hours = d.getHours();
        this.inAlmacen = d.isInAlmacen();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public double getHours() { return hours; }
    public void setHours(double hours) { this.hours = hours; }

    public boolean isInAlmacen() { return inAlmacen; }
    public void setInAlmacen(boolean bool) { this.inAlmacen = bool; }
}
