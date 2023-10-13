package ua.ypon.sensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ypon.sensor.dto.SensorDTO;
import ua.ypon.sensor.models.Sensor;
import ua.ypon.sensor.repositories.SensorRepository;
import ua.ypon.sensor.util.SensorNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ua.ypon 19.09.2023
 */
@Service
@Transactional(readOnly = true)
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> findAll() {
        return sensorRepository.findAll();
    }

    public Sensor findOne(int id) {
        Optional<Sensor> foundSensor = sensorRepository.findById(id);
        //викидаємо особисте виключення якщо сенсор не знайдений
        return foundSensor.orElseThrow(SensorNotFoundException::new);
    }

    public boolean sensorExistsByName(String name) {
        List<Sensor> sensors = sensorRepository.findSensorByName(name);
        return !sensors.isEmpty();
    }

    public Sensor findSensorByNameOrId(String nameOrId) {
        try {
            int id = Integer.parseInt(nameOrId);
            return sensorRepository.findById(id)
                    .orElseThrow(() -> new SensorNotFoundException());
        } catch (NumberFormatException e) {
            List<Sensor> sensors = sensorRepository.findSensorByName(nameOrId);
            if(sensors.isEmpty()) {
                throw new SensorNotFoundException();
            }
            return sensors.get(0);
        }
    }
    @Transactional
    public Sensor save(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

}
