package edu.upc.dsa.domain.models;
import java.util.ArrayList;
import java.util.List;

public class Dron {
    //ATRIBUTOS DE LA CLASE
    private  String id;
    private  String name;
    private  String fabricante;
    private  String modelo;
    private  double hours;
    private  boolean inAlmacen;
    private List<FlightPlan> flightPlanList;

    public Dron() {} //CONSTRUCTOR VACÍO

    //CONSTRUCTOR PÚBLICO DE LA CLASE

    public Dron(String id, String name, String fabricante, String modelo) {
        this.id = id;
        this.name = name;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.flightPlanList = new ArrayList<FlightPlan>();
    }

    @Override
    public String toString()  //TO STRING PARA HACER LEGIBLE EL PRODUCT
    {
        return "Dron[id=" + id + ", name=" + name + ", fabricante=" + fabricante + ", modelo=" + modelo + " , horas=" + hours + ", enAlmacén=" + inAlmacen + ", planes=" + flightPlanList + "]";
    }

    public void addHours(double hours){ this.hours += hours; }

    public void addFlightPlan(FlightPlan f){
        flightPlanList.add(f);
    }

    //SETTERS & GETTERS

    public double getHours() { return hours; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name){ this.name = name; }

    public void setInAlmacen(Boolean bool) {this.inAlmacen = bool; }

    public boolean isInAlmacen() {return inAlmacen; }

    public List<FlightPlan> getFlightPlans()
    {
        return this.flightPlanList;
    }
    public String getFabricante() { return fabricante; }

    public String getModelo() { return modelo; }

}
