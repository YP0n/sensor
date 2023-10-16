package ua.ypon.sensor.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.ypon.sensor.dto.SensorDTO;
import ua.ypon.sensor.models.Sensor;
import ua.ypon.sensor.services.SensorService;
import ua.ypon.sensor.util.SensorErrorResponse;
import ua.ypon.sensor.util.SensorNotCreatedException;
import ua.ypon.sensor.util.SensorNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ua.ypon 19.09.2023
 */
@RestController
@RequestMapping("/sensor")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<SensorDTO> getSensors() {
        return sensorService.findAll().stream().map(this::convertToSensorDTO)
                .collect(Collectors.toList());// Jackson конвертує ці об"єкти в JSON
    }

    @GetMapping("/{id}")
    public SensorDTO getSensor(@PathVariable("id") int id) {
        return convertToSensorDTO(sensorService.findOne(id));// Jackson конвертує ці об"єкти в JSON
    }

    //створюємо сенесор за допомогою повернення обʼєкта responseEntity в виді HTTP запиту
    //@RequestBody для конвертації json в клас Sensor

    @PostMapping("/registration")
    public ResponseEntity<SensorDTO> create(@RequestBody @Valid SensorDTO sensorDTO) {
        if (sensorService.sensorExistsByName(sensorDTO.getName())) {
            throw new SensorNotCreatedException("A sensor with this name already exists");
        }

        Sensor createdSensor = sensorService.save(convertToSensor(sensorDTO));
        SensorDTO createdSensorDTO = convertToSensorDTO(createdSensor);

        return new ResponseEntity<>(createdSensorDTO, HttpStatus.CREATED);
    }


    //ловимо та обробляємо наше особисте виключення та створюємо обʼєкт response з повідомленням та часом створення
    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotFoundException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                "Sensor with this id wasn't found!",
                System.currentTimeMillis()
        );
        //обгортка над нашим response
        //В HTTP відповіді тіло відповіді (response) та статус у заголовці
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//NOT_FOUND - 404 status
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //modelMapper бере всю взаємодію sensorDTO з Sensor знаходячи поля які співпадають
    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}

/*
Цей код представляє контролер для обробки запитів, пов'язаних з сенсорами веб-додатка на основі Spring Framework. Ось пояснення до кожної частини коду:

package ua.ypon.sensor.controllers;: Це оголошення пакету, яке визначає, в якому пакеті розташований цей клас контролера.

@RestController: Ця анотація позначає клас як контролер, який обробляє запити та повертає результат у форматі JSON.

@RequestMapping("/sensor"): Вказує, що всі URL-запити, які починаються з "/sensor", будуть оброблятися цим контролером.

private final SensorService sensorService;: Це ін'єкція сервісу сенсора, який використовується для взаємодії з базою даних сенсорів.

private final ModelMapper modelMapper;: Це ін'єкція ModelMapper, який використовується для конвертації об'єктів між класами SensorDTO і Sensor.

@GetMapping: Ця анотація вказує, що метод обробляє GET-запити.

@PostMapping("/registration"): Вказує, що метод обробляє POST-запити за шляхом "/sensor/registration".

@RequestBody @Valid SensorDTO sensorDTO: Ця анотація вказує, що об'єкт sensorDTO передається в тілі запиту у форматі JSON та підлягає валідації.

ResponseEntity<SensorDTO> create(...): Метод, який створює новий сенсор, валідує дані, обробляє винятки та повертає результат у вигляді об'єкта ResponseEntity зі статусом та тілом.

@ExceptionHandler: Ця анотація вказує, що методи handleException використовуються для обробки винятків і відповідають за створення відповідей на винятки.

convertToSensor та convertToSensorDTO: Методи для конвертації об'єктів між SensorDTO та Sensor.

Отже, цей контролер обробляє запити для отримання і створення сенсорів, обробляє винятки і повертає відповіді у форматі JSON.
 */
