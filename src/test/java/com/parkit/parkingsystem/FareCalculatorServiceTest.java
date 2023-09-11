package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    public static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    public void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar() {
        //Given
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //When
        fareCalculatorService.calculateFare(ticket,false);

        //then
        double result = ticket.getPrice();
        assertEquals(result, 0.75);
    }

    @Test
    public void calculateFareBike() {
        //Given
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //When
        fareCalculatorService.calculateFare(ticket, false);

        //Then
        //double resultFareBike = ticket.getPrice();
        assertEquals(ticket.getPrice(), 0.5);
    }

    @Test
    public void calculateFareUnkownType() {
        //Given
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //when
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBikeWithFutureInTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
                                                                      // parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //When
        fareCalculatorService.calculateFare(ticket, false);

        //Then
        assertEquals(0.25, ticket.getPrice(),0.000001); // 0.25 = 3/4 h - 30mn gratuites = 0.75-0.5
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() {
        //Given
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
                                                                      // parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //when
        fareCalculatorService.calculateFare(ticket, false);
        //then
        assertEquals(0.375, ticket.getPrice()); // 0.375 = 0.75 -0.25 *1.5 ou 1.5 =CAR.fare
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
                                                                           // parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket, false);
        assertEquals((35.25), ticket.getPrice()); // 35.25 = (24 - 0.5)  * 1.5 = (24h - 30 mn gratuites)*car.Fare
    }

    @Test
    public void calculateFareCarWithLessThan30minutesParkingTime() {
        //Given
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));// 30 mn
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //when
        fareCalculatorService.calculateFare(ticket, false);

        //then
        assertEquals(0.0, ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithLessThan30minutesParkingTime() {
        //Given
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));// 30 mn
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //when
        fareCalculatorService.calculateFare(ticket, false);

        //then
        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithDiscount() {
        //Given
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //When
        fareCalculatorService.calculateFare(ticket, true);

        //then
        assertEquals(0.7125, ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithDiscount() {
        //Given
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));// durée du ticket > 30mn
        Date outTime = new Date();
        // rajout
        // outTime.setTime(System.currentTimeMillis()+ ( 60 * 60 * 1000) );// durée du
        // ticket > 30mn
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //When
        fareCalculatorService.calculateFare(ticket, true);

        //Then
        double result1 = ticket.getPrice();
        assertEquals(0.475, result1);// prix discount = 95% du prix normal (fare BIKE = 1) = 0.75 * 0.0375 = 0.7125
    }
}
