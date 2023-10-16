package ua.ypon.sensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ypon.sensor.models.Measurements;
import ua.ypon.sensor.models.Sensor;
import ua.ypon.sensor.repositories.MeasurementRepository;
import ua.ypon.sensor.repositories.SensorRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author ua.ypon 19.09.2023
 */
@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public List<Measurements> findAll() {
        return measurementRepository.findAll();
    }

    public Measurements findOne(int id) {
        Optional<Measurements> foundMeasurements = measurementRepository.findById(id);
        return foundMeasurements.orElse(null);
    }


    @Transactional
    public void save(Measurements measurements) {
            enrichMeasurement(measurements);
            measurementRepository.save(measurements);
    }

    private void enrichMeasurement(Measurements measurements) {
        measurements.setTimestamp(LocalDateTime.now());
    }

    public int countDayWithRain(List<Measurements> measurements) {
        Set<LocalDate> rainDays = new HashSet<>();

        for(Measurements measurement : measurements) {
            if(measurement.isRaining()) {
                LocalDate measurementDate = measurement.getTimestamp().toLocalDate();
                rainDays.add(measurementDate);
            }
        }
        return rainDays.size();
    }
}
/*
@Service: Ця анотація позначає цей клас як компонент Spring, який автоматично виявляється та створюється в контексті додатку. Він призначений для надання послуг у роботі з вимірюваннями.

@Transactional(readOnly = true): Ця анотація позначає, що всі методи цього сервісу, які не мають своєї власної анотації @Transactional, будуть працювати в режимі транзакцій лише для читання (readonly), тобто вони не здатні змінювати дані в базі даних.

MeasurementRepository: Це інтерфейс, який використовується для взаємодії з базою даних із вимірюваннями. Об'єкт цього інтерфейсу ін'єктується в конструкторі класу через анотацію @Autowired, щоб можливо було виконувати операції збереження, вибору і т. д. в базі даних.

findAll(): Цей метод повертає список всіх вимірювань, які зберігаються в базі даних.

findOne(int id): Цей метод приймає ідентифікатор вимірювання і повертає відповідне вимірювання, якщо таке існує. В іншому випадку він повертає null.

save(Measurements measurements): Цей метод зберігає передане вимірювання в базі даних. Перед збереженням викликається приватний метод enrichMeasurement, який додає мітку часу вимірювання, яка встановлюється в поточний момент.

enrichMeasurement(Measurements measurements): Цей приватний метод додає мітку часу вимірюванням. Мітка часу встановлюється на поточний момент, щоб вказати, коли було зроблено вимірювання.

countDayWithRain(List<Measurements> measurements): Цей метод обчислює кількість днів, коли спостерігалася дощова погода. Він приймає список вимірювань та перевіряє кожне вимірювання, чи воно вказує на дощ. Якщо так, то вимірювання додається до множини rainDays, і в результаті повертається кількість унікальних дат, коли був дощ.

Цей клас використовуєся для роботи з даними вимірювань і надає методи для зберігання, пошуку та обробки цих даних.
 */
