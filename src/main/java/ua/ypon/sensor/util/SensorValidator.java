package ua.ypon.sensor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.ypon.sensor.models.Sensor;
import ua.ypon.sensor.services.SensorService;

/**
 * @author ua.ypon 06.10.2023
 */
@Component
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Sensor sensor = (Sensor) target;

        if(sensorService.sensorExistsByName(sensor.getName())) {
            errors.rejectValue("name", "", "A sensor with this name already exists");
        }
    }
}
