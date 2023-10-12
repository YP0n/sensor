package ua.ypon.sensor.dto;

import java.util.Random;

/**
 * @author ua.ypon 09.10.2023
 */
public class RandomSensorDataGenerator {
    private static final Random random = new Random();

    public static SensorDTO generateRandomSensorDTO() {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName(generateRandomSensorName());
        return sensorDTO;
    }

    private static String generateRandomSensorName() {
        int sensorNumber = random.nextInt(1000) + 1;
        return "Sensor" + sensorNumber;
    }
}
