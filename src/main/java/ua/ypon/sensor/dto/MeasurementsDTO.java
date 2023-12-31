package ua.ypon.sensor.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import ua.ypon.sensor.models.Sensor;

/**
 * @author ua.ypon 06.10.2023
 */
public class MeasurementsDTO {

    @Column(name = "value")
    @NotNull
    @DecimalMin(value = "-100.0", message = "the value must be at last -100")
    @DecimalMax(value = "100.0", message = "the value should not be more than 100")
    private double value;

    @Column(name = "raining")
    @NotNull
    private boolean raining;

    @ManyToOne
    @JoinColumn(name = "sensor_name", referencedColumnName = "id")
    private Sensor sensor;

    public double getValue() {
        return value;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }
}
