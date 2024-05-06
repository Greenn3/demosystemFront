package com.example.demosystemfront.Entities;





import com.example.demosystemfront.Entities.AccType;

import java.time.LocalDate;
import java.util.Date;


public class Booking {

   public Integer id;

    public String name;

  public  LocalDate arrivalDate;

   public LocalDate departureDate;

   public AccType accType;

    public Booking() {
    }

    public Booking(Integer id, String name, LocalDate arrivalDate, LocalDate departureDate) {
        this.id = id;
        this.name = name;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public Booking(String name, LocalDate arrivalDate, LocalDate departureDate) {
        this.name = name;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;

    }

    public Integer getId() {
       return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", departureDate=" + departureDate +
                '}';
    }
}
