package ua.ypon.sensor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ypon.sensor.models.Sensor;

import java.util.List;

/**
 * @author ua.ypon 19.09.2023
 */
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    List<Sensor> findSensorByName(String name);
}
