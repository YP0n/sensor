package ua.ypon.sensor.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ua.ypon.sensor.dto.SensorDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author ua.ypon 19.09.2023
 */
@Entity
@Table(name = "Measurements")
public class Measurements {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinColumn(name = "sensor_name", referencedColumnName = "id")
    private Sensor sensorOwner;

    @Column(name = "created_at")
    private LocalDateTime timestamp;

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

    public Sensor getSensorOwner() {
        return sensorOwner;
    }

    public void setSensorOwner(Sensor sensorOwner) {
        this.sensorOwner = sensorOwner;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
