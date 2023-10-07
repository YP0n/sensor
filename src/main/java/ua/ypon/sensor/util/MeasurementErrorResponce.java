package ua.ypon.sensor.util;

/**
 * @author ua.ypon 06.10.2023
 */
public class MeasurementErrorResponce {

    private String message;
    //час коли зʼявилася помилка
    private long timestamp;

    public MeasurementErrorResponce(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
