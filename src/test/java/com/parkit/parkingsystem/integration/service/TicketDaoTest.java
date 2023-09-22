package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.*;

public class TicketDaoTest {

    public static TicketDAO TicketTest;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    public static void setUp() {
        TicketTest = new TicketDAO();
        TicketTest.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest(){
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown(){
    }

    @Test
    void itShouldSaveTicketForCar() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setId(5);
        ticket.setVehicleRegNumber("XY-PMS-TB");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //WHEN
        TicketTest.saveTicket(ticket);

        //THEN
        Ticket expected = TicketTest.getTicket("XY-PMS-TB");
        assertEquals(expected.getVehicleRegNumber(), "XY-PMS-TB");
    }

    @Test
    void itShouldSaveTicketForBike() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setId(5);
        ticket.setVehicleRegNumber("VELIB");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //WHEN
        TicketTest.saveTicket(ticket);

        //THEN
        Ticket ticketSaved = TicketTest.getTicket("VELIB");
        assertThat(ticketSaved.getVehicleRegNumber()).isEqualTo(ticket.getVehicleRegNumber());
    }

    @Test
    void itShouldUpdateTicket() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setVehicleRegNumber("XY-PMS-TB");
        ticket.setInTime(inTime);
        ticket.setParkingSpot(parkingSpot);
        TicketTest.saveTicket(ticket);

        //WHEN
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        Boolean TicketUpdated = TicketTest.updateTicket(ticket);

        //THEN
        Ticket expected = TicketTest.getTicket("XY-PMS-TB");
        assertThat(TicketUpdated).isTrue();
        assertThat(expected.getVehicleRegNumber()).isEqualTo(ticket.getVehicleRegNumber());
    }

    @Test
    public void updateTicketWithNoVehicleRegNumber() {
        //GIVEN
        Ticket ticket = TicketTest.getTicket(null);

        //WHEN
        boolean result = TicketTest.updateTicket(ticket);

        //THEN
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void saveTicketWithNoVehicleRegNumber() {
        //GIVEN
        Ticket ticket = TicketTest.getTicket(null);

        //WHEN
        boolean result = TicketTest.saveTicket(ticket);

        //THEN
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void itShouldReturnFalseForNonRecurrentUser(){
        //GIVEN > WHEN
        Boolean UserManyTime= TicketTest.getNbTicket("XY-ZTU-TB");

        //THEN
        assertThat(UserManyTime).isFalse();
    }

    @Test
    public void itShouldReturnTrueForRecurrentUser(){
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setId(5);
        ticket.setVehicleRegNumber("AB-UXD-VZ");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //WHEN
        TicketTest.saveTicket(ticket);
        TicketTest.saveTicket(ticket);
        Boolean UserManyTime = TicketTest.getNbTicket("AB-UXD-VZ");
        //THEN
        assertThat(UserManyTime).isTrue();
    }

}
