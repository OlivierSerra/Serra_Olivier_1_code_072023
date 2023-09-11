package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

import java.util.Calendar;
import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean discount;
    //private double discountPrice;
   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // !!! les places sont attribués en fonction du type de véhicule. Donc les places appartiennent à des catégories.
    //Elles ne doivent PAS être attribués peu importe la catégorie de véhicule
    public ParkingSpot getParkingSpot() {
        //on va chercher l'identifiant de la place de parking.
        int ParkingNumberCategorie = parkingSpot.getId();
        //on va chercher la place de parking associée à la catégorie du véhicule
        ParkingType SpotCategorie = parkingSpot.getParkingType();
        //On va chercher une place libre.
        boolean SpotAvalaible = parkingSpot.isAvailable();
        //opérateur ternaire pour si la place n'est pas nul alors on génère une nouvelle place de parking sinon c'est nul
        return parkingSpot != null ? new ParkingSpot (ParkingNumberCategorie, SpotCategorie, SpotAvalaible) : null;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        //On crée 3 objets pour injecter     un nouvel objet de type parkingSpot
        int ParkingNumberCategorie = parkingSpot.getId();
        ParkingType SpotCategorie = parkingSpot.getParkingType();
        boolean SpotAvalaible = parkingSpot.isAvailable();
        this.parkingSpot = new ParkingSpot(ParkingNumberCategorie, SpotCategorie, SpotAvalaible);
    }


    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }
}
