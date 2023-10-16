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
/*

@Service: Ця анотація позначає цей клас як компонент Spring, який надає послуги в роботі з сенсорами.

@Transactional(readOnly = true): Ця анотація вказує, що всі методи цього сервісу, які не мають своєї власної анотації @Transactional, працюватимуть в режимі транзакцій лише для читання (readonly), тобто вони не здатні змінювати дані в базі даних.

SensorRepository: Це інтерфейс, який використовується для взаємодії з базою даних щодо сенсорів. Об'єкт цього інтерфейсу ін'єктується в конструкторі класу через анотацію @Autowired, щоб можливо було виконувати операції збереження, вибору і т. д. в базі даних.

findAll(): Цей метод повертає список всіх сенсорів, які зберігаються в базі даних.

findOne(int id): Цей метод приймає ідентифікатор сенсора і повертає відповідний сенсор, якщо такий існує. Якщо сенсор не знайдено, викидається виняток SensorNotFoundException.

sensorExistsByName(String name): Цей метод перевіряє, чи існують сенсори з вказаною назвою в базі даних і повертає true, якщо такі сенсори існують, або false, якщо немає.

findSensorByNameOrId(String nameOrId): Цей метод приймає рядок nameOrId і спробує визначити, чи це ідентифікатор сенсора (у вигляді цілого числа) чи назва сенсора. В залежності від цього він виконує відповідний пошук у базі даних і повертає знайдений сенсор. Якщо нічого не знайдено, викидається виняток SensorNotFoundException.

save(Sensor sensor): Цей метод зберігає переданий об'єкт сенсора в базі даних і повертає збережений об'єкт сенсора. Це дозволяє додавати нові сенсори до бази даних.

Цей клас допомагає керувати даними сенсорів, виконувати пошук, зберігати та перевіряти їхню наявність в системі.
 */