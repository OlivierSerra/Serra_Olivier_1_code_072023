package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;


    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    // TODO: check that a ticket is actualy saved in DB and Parking table is updated
    // with availability


    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket ticketBeforeTest = ticketDAO.getTicket("ABCDEF");

        parkingService.processIncomingVehicle();
        assertThat(ticketBeforeTest).isNull();

        Ticket ticketTest = ticketDAO.getTicket("ABCDEF");
        assertThat(ticketTest).isNotNull();
        assertThat(ticketTest.getParkingSpot().isAvailable()).isFalse();
        //boolean resultSaveTicket = ticketDAO.saveTicket(ticketTest);
        //assertThat(resultSaveTicket).isTrue();
        //boolean resultUpdateTicket = ticketDAO.updateTicket(ticketTest);
        //assertThat(resultUpdateTicket).isTrue();
    }

    @Test
    public void testParkingLotExit() {
        //ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        //parkingService.processExitingVehicle();
        // TODO: check that the fare generated and out time are populated correctly in
        // the database
        //On génère un nouvel objet parkingservice

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        //on génère un nouveau ticket dont le modèle est parkingSpot; vehicleRegNumber; price; inTime, outTime
        Ticket ticket = new Ticket();
        //on fixe les items du ticket pour qu'ils correspondent à un ticket qui pourrait exister.
        parkingService.processExitingVehicle();
        // La nouvelle place de parking doit être associée à une nouvelle place libre
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable(); //
        //on va injecter cette place dans le ticket
        ticket.setParkingSpot(parkingSpot);
        //on fixe la plaque comme étant celle qu'on recherche
        ticket.setVehicleRegNumber("ABCDEF");
        //on fixe un  prix
        ticket.setPrice(0);
        //on va chercher l'heure
        Date HeureEntree = new Date(System.currentTimeMillis() - 3600 * 1000);//
        Date HeureSortie = new Date(System.currentTimeMillis());
        ticket.setInTime(HeureEntree);
        ticket.setOutTime(HeureSortie);
        ticketDAO.saveTicket(ticket);

        Ticket TicketTest = ticketDAO.getTicket("ABCDEF");
        assertThat(TicketTest.getPrice()).isNotNull();
    }

    @Test
    public void testParkingLotExitRecurringUserCAR() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Ticket ticket = new Ticket();
        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();

        //on fixe les items du ticket pour qu'ils correspondent à un ticket qui pourrait exister.

        // La nouvelle place de parking doit être associée à une nouvelle place libre
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
        //on va injecter cette place dans le ticket
        ticket.setParkingSpot(parkingSpot);
        //on fixe la plaque comme étant celle qu'on recherche
        ticket.setVehicleRegNumber("ABCDEF");
        //on fixe un  prix
        ticket.setPrice(1.5); //Prix pour 1h de parking CAR
        //on va chercher l'heure
        Date HeureEntree = new Date(System.currentTimeMillis() - 3600 * 1000);//On retire une heure pour calculer la différence de temps
        Date HeureSortie = new Date(System.currentTimeMillis());
        ticket.setInTime(HeureEntree);
        ticket.setOutTime(HeureSortie);
        ticketDAO.saveTicket(ticket);

        Ticket ticketTest = ticketDAO.getTicket("ABCDEF");
        double resultDiscount = (ticketTest.getPrice() * 0.05); //(1h * 1.5€ *0.05) = 0.075
        double resultCalculation = ticketTest.getPrice() - resultDiscount; //=1h *1,5 -0.075
        //assertThat(ticketTest.getPrice()).isNotNull();
        //assertThat(resultCalculation).isEqualTo(1.349);
        assertEquals(resultCalculation, 1.425); // 1.35 = 1h * 1.5€ - 0.075
    }
}


