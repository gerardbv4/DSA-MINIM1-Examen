package edu.upc.dsa.domain.models;
import java.util.List;
import java.util.ArrayList;

public class Pilot {
    //ATRIBUTOS DE LA CLASE
    private  String id;
    private  String name;
    private  String surname;
    private  double hours;
    private List<FlightPlan> flightPlans;

    //CONSTRUCTOR PÚBLICO DE CLASE
    public Pilot(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.flightPlans = new ArrayList<FlightPlan>();
    }
    public Pilot() {}

    @Override
    public String toString() //TO STRING PARA HACER LEGIBLE EL USER
    {
        return "Pilot[id=" + id + ", name=" + name + ", surname=" + surname + ", planes=" + flightPlans.size() + "]";
    }

    public List<FlightPlan> getFlightPlans()
    {
        return this.flightPlans;
    }

    public void addFlightPlan(FlightPlan flightPlan)
    {
        flightPlans.add(flightPlan);
    }

    public int numFlightPlans()
    {
        return this.flightPlans.size();
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }
    public String getSurname() { return this.surname; }

    public double getHours() { return hours; }

    public void addHours(double hours){ this.hours += hours; }
}
