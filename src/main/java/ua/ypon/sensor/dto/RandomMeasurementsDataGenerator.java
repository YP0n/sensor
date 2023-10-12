package ua.ypon.sensor.dto;

import java.util.Random;

/**
 * @author ua.ypon 09.10.2023
 */
public class RandomMeasurementsDataGenerator {
    private static final Random random = new Random();

    public static MeasurementsDTO generateRandomMeasurementsDTO() {
        MeasurementsDTO measurementsDTO = new MeasurementsDTO();
        measurementsDTO.setValue(random.nextDouble() * 40);
        measurementsDTO.setRaining(random.nextBoolean());
//
//        SensorDTO randomSensorDTO = RandomSensorDataGenerator.generateRandomSensorDTO();
//        measurementsDTO.setSensor(randomSensorDTO);

        return measurementsDTO;
    }
}
