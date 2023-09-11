package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void GetDiscount(){

    }

    public void calculateFare(Ticket ticket, boolean discount) {
        if (ticket.getOutTime() == null || ticket.getOutTime().before(ticket.getInTime()))
            throw new IllegalArgumentException("Out time provided is incorrect: " + ticket.getOutTime().toString());

        double duration = calculateDuration(ticket);
        double calculation;

        if (duration <= 0.5) {
            duration = 0;
        } else {
            duration -= 0.5;

            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    calculation = (duration) * Fare.CAR_RATE_PER_HOUR;//0.75
                    if (discount == true) {
                        double discountType = calculation * 0.05;//0.0375
                       calculation = (duration) * Fare.CAR_RATE_PER_HOUR - discountType; //0.75 - 0.0375 = 0.7125
                    }
                    break;
                }
                case BIKE: {
                    calculation = (duration) * Fare.BIKE_RATE_PER_HOUR;//0.5
                    if (discount == true) {
                        double discountType = calculation * 0.05;//0.025
                        calculation = (duration) * Fare.BIKE_RATE_PER_HOUR - discountType;//0.5 -0.025 = 0.475
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
            ticket.setPrice(calculation);
        }
    }

    public double calculateDuration(Ticket ticket) {
        Long inHour = ticket.getInTime().getTime();
        Long outHour = ticket.getOutTime().getTime();
        double duration = (double) (outHour - inHour) / (60 * 60 * 1000);
        return duration;
    }
}

