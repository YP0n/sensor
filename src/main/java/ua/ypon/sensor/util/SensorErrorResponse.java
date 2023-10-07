package ua.ypon.sensor.util;

/**
 * @author ua.ypon 19.09.2023
 */
//клас який відправляє обʼєкт при помилці
public class SensorErrorResponse {
    //повідомлення про помилку
    private String message;
    //час коли зʼявилася помилка
    private long timestamp;

    public SensorErrorResponse(String message, long timestamp) {
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
