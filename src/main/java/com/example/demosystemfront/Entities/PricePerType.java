package com.example.demosystemfront.Entities;



public class PricePerType {



    int id;

    PricePeriod pricePeriod;


    AccType accType;


    Double price;

    public PricePerType(int id, PricePeriod pricePeriod, AccType accType, Double price) {
        this.id = id;
        this.pricePeriod = pricePeriod;
        this.accType = accType;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PricePeriod getPricePeriod() {
        return pricePeriod;
    }

    public void setPricePeriod(PricePeriod pricePeriod) {
        this.pricePeriod = pricePeriod;
    }

    public AccType getAccType() {
        return accType;
    }

    public void setAccType(AccType accType) {
        this.accType = accType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
