package ua.ypon.sensor.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.ypon.sensor.dto.MeasurementsDTO;
import ua.ypon.sensor.models.Measurements;
import ua.ypon.sensor.models.Sensor;
import ua.ypon.sensor.services.MeasurementService;
import ua.ypon.sensor.services.SensorService;
import ua.ypon.sensor.util.MeasurementNotCreatedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ua.ypon 19.09.2023
 */

/*
getMeasurements(): Цей метод обробляє GET-запит для отримання всіх вимірювань. Він використовує сервіс вимірювань для отримання списку всіх вимірювань і перетворює їх в об'єкти DTO (Data Transfer Object). Потім повертає список вимірювань у форматі DTO.

getMeasurement(int id): Цей метод обробляє GET-запит для отримання вимірювання за ідентифікатором. Він використовує сервіс вимірювань для отримання вимірювання за заданим ідентифікатором і перетворює його в об'єкт DTO. Потім повертає це вимірювання у форматі DTO.

countRainyDays(): Цей метод обробляє GET-запит для підрахунку кількості дощових днів. Він використовує сервіс вимірювань для отримання списку всіх вимірювань і підраховує кількість дощових днів, перевіряючи поле isRaining кожного вимірювання. Потім повертає відповідь зі значенням кількості дощових днів.

getTemperatureChartData(): Цей метод обробляє GET-запит для отримання даних для побудови графіка температур. Він використовує сервіс вимірювань для отримання списку всіх вимірювань. Потім створює об'єкт chartData, в якому зберігає значення температур і дат-міток. Ці дані повертається у форматі Map.

create(MeasurementsDTO measurementsDTO, BindingResult bindingResult): Цей метод обробляє POST-запит для створення нового вимірювання. Спочатку перевіряє результати валідації (bindingResult) і, якщо вони містять помилки, генерує виняток MeasurementNotCreatedException з описом помилок. Потім він отримує ім'я датчика з об'єкта DTO, шукає відповідний датчик і, якщо він не знайдений, повертає відповідь зі статусом "помилка запиту". В іншому випадку, він перетворює DTO в об'єкт вимірювань і зберігає його в базі даних, повертаючи відповідь зі статусом "OK".

convertToMeasurements(MeasurementsDTO measurementsDTO): Цей метод використовує ModelMapper для конвертації об'єкта DTO в об'єкт вимірювань.

convertToMeasurementsDTO(Measurements measurements): Цей метод використовує ModelMapper для конвертації об'єкта вимірювань в об'єкт DTO.
 */
@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    public MeasurementController(MeasurementService measurementService, SensorService sensorService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<MeasurementsDTO> getMeasurements() {
        return measurementService.findAll().stream().map(this::convertToMeasurementsDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MeasurementsDTO getMeasurement(@PathVariable("id") int id) {
        return convertToMeasurementsDTO(measurementService.findOne(id));
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<Integer> countRainyDays() {
        List<Measurements> allMeasurements = measurementService.findAll();
        int rainyDays = 0;
        for (Measurements measurement : allMeasurements) {
            if (measurement.isRaining()) {
                rainyDays++;
            }
        }
        return ResponseEntity.ok(rainyDays);
    }

    @GetMapping("/measurements-chart-data")
    public ResponseEntity<Map<String, Object>> getTemperatureChartData() {
        List<Measurements> measurementsList = measurementService.findAll();

        // Створюємо об'єкт Map з даними для графіка
        Map<String, Object> chartData = new HashMap<>();

        // Отримуємо значення температур для yAxisLabel
        List<Double> temperatureValues = measurementsList.stream()
                .map(Measurements::getValue)
                .collect(Collectors.toList());

        // Отримуємо список дат-міток для осі X
        List<String> timeLabels = measurementsList.stream()
                .map(measurement -> measurement.getTimestamp().toString())
                .collect(Collectors.toList());

        // Додаємо дані до об'єкта chartData
        chartData.put("dataset", temperatureValues);
        chartData.put("columnKeys", timeLabels);

        // Додайте ключ "yAxisLabel" разом з відповідним текстом
        chartData.put("yAxisLabel", "Температура (°C)");

        return ResponseEntity.ok(chartData);
    }


    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MeasurementsDTO measurementsDTO,
                                             BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new MeasurementNotCreatedException(errorMsg.toString());

        }
        String sensorName = measurementsDTO.getSensor().getName();
        Sensor sensor = sensorService.findSensorByNameOrId(sensorName);
        if(sensor == null) {
            return ResponseEntity.badRequest().build();
        }

        Measurements measurements = convertToMeasurements(measurementsDTO);
        measurements.setSensorOwner(sensor);

        measurementService.save(measurements);
            return ResponseEntity.ok(HttpStatus.OK);
    }

    private Measurements convertToMeasurements(MeasurementsDTO measurementsDTO){
        return modelMapper.map(measurementsDTO, Measurements.class);
    }

    private MeasurementsDTO convertToMeasurementsDTO(Measurements measurements) {
        return modelMapper.map(measurements, MeasurementsDTO.class);
    }
}
