package ua.ypon.sensor.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * @author ua.ypon 19.09.2023
 */
@Entity
@Table(name = "Measurements")
public class Measurements {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "value")
    @NotNull
    @DecimalMin(value = "-100.0", message = "the value must be at last -100")
    @DecimalMax(value = "100.0", message = "the value should not be more than 100")
    private double value;

    @Column(name = "raining")
    @NotNull
    private boolean raining;

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private Sensor sensorOwner;

    public Measurements() {
    }

    public Measurements(double value, boolean raining) {
        this.value = value;
        this.raining = raining;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
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

    public Sensor getSensor() {
        return sensorOwner;
    }

    public void setSensor(Sensor sensorOwner) {
        this.sensorOwner = sensorOwner;
    }

    @Override
    public String toString() {
        return "{" +
                "value" + ": " + value +
                ", raining" + ": " + raining +
                ", sensor" + ": " + "{"
                + "name" + ": " + sensorOwner +
                "}" +
                "}";
    }
}
