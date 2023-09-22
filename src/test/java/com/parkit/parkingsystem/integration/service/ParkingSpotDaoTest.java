package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class ParkingSpotDaoTest {
    public static ParkingSpotDAO ticketTest;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    public static void setUp() {
        ticketTest = new ParkingSpotDAO();
        ticketTest.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @AfterAll
    public static void tearDown(){
    }

    @Test
    void name() {

    }

    @Test
    public void updateParkingCarTest () {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        //WHEN
        ticketTest.updateParking(parkingSpot);
        int result = ticketTest.getNextAvailableSlot(ParkingType.CAR);

        //THEN
        assertThat(parkingSpot.getId()).isEqualTo(result);
    }

    @Test
    public void updateParkingBikeTest () {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, true);

        //WHEN
        ticketTest.updateParking(parkingSpot);
        int result = ticketTest.getNextAvailableSlot(ParkingType.BIKE);

        //THEN
        assertThat(parkingSpot.getId()).isEqualTo(result);
    }

    @Test
    public void updateInvalidParkingSpotCarTestShouldReturnFalse()  {
        //GIVEN
        boolean isUpdated = ticketTest.updateParking(null);
        //WHEN > THEN
        assertThat(isUpdated).isEqualTo(false);
    }

    @Test
    public void getNextAvailableSlotWithInvalidParkingTypeShouldReturnMinus1 () {
        //GIVEN
        int result = ticketTest.getNextAvailableSlot(null);
        //WHEN > THEN
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void switchParkingTypeShouldUpdateTest () {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ticketTest.updateParking(parkingSpot);

        //WHEN
        int parkingNumber = ticketTest.getNextAvailableSlot(ParkingType.BIKE);
        parkingSpot.setParkingType(ParkingType.BIKE);
        parkingSpot.setId(parkingNumber);
        boolean result = ticketTest.updateParking(parkingSpot);

        //THEN
        assertThat(result).isEqualTo(true);
    }


}
