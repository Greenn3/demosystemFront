package com.example.demosystemfront.Entities;





import com.example.demosystemfront.Entities.AccType;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.Date;


public class Booking {

   public Integer id;

    public String name;

  public  LocalDate arrivalDate;

   public LocalDate departureDate;

   public AccType accType;

   private String info;

   public Double discount = 0.0;


    private String phone;
    private String email;
    private boolean hasArrived = false;
    @SerializedName("isPaid")
    public boolean isPaid = false;
    private boolean hasLeft = false;

    public boolean isHasArrived() {
        return hasArrived;
    }

    public void setHasArrived(boolean hasArrived) {
        this.hasArrived = hasArrived;
    }

    public boolean isHasLeft() {
        return hasLeft;
    }

    public void setHasLeft(boolean hasLeft) {
        this.hasLeft = hasLeft;
    }

    public Booking(Integer id, String name, LocalDate arrivalDate, LocalDate departureDate, AccType accType, String info, Double discount, Boolean isPaid, Boolean hasArrived, String phone, String email, Boolean hasLeft) {
        this.id = id;
        this.name = name;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.accType = accType;
        this.info = info;
        this.discount = discount;
        this.isPaid = isPaid;
        this.hasArrived = hasArrived;
        this.phone = phone;
        this.email = email;
        this.hasLeft = hasLeft;
    }

    public String getInfo() {
        return info;
    }

    public Booking(Integer id, String name, LocalDate arrivalDate, LocalDate departureDate, AccType accType) {
        this.id = id;
        this.name = name;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.accType = accType;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Booking() {
    }

    public Booking(Integer id, String name, LocalDate arrivalDate, LocalDate departureDate) {
        this.id = id;
        this.name = name;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public Booking(String name, LocalDate arrivalDate, LocalDate departureDate, AccType accType) {
        this.name = name;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.accType = accType;

    }


    public Booking(String name, LocalDate arrivalDate, LocalDate departureDate, AccType accType, String phone, String email, String info) {
        this.name = name;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.accType = accType;
        this.phone = phone;
        this.email = email;
        this.info = info;
    }

    public Booking(Integer id, String name, LocalDate arrivalDate, LocalDate departureDate, AccType accType, String info, Double discount, String phone, String email, boolean hasArrived, boolean isPaid, boolean hasLeft) {
        this.id = id;
        this.name = name;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.accType = accType;
        this.info = info;
        this.discount = discount;
        this.phone = phone;
        this.email = email;
        this.hasArrived = hasArrived;
        this.isPaid = isPaid;
        this.hasLeft = hasLeft;
    }

    public AccType getAccType() {
        return accType;
    }

    public void setAccType(AccType accType) {
        this.accType = accType;
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
