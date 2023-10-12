package ua.ypon.sensor.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.ypon.sensor.dto.MeasurementsDTO;
import ua.ypon.sensor.dto.RandomMeasurementsDataGenerator;
import ua.ypon.sensor.dto.RandomSensorDataGenerator;
import ua.ypon.sensor.dto.SensorDTO;
import ua.ypon.sensor.models.Measurements;
import ua.ypon.sensor.models.Sensor;
import ua.ypon.sensor.services.MeasurementService;
import ua.ypon.sensor.services.SensorService;
import ua.ypon.sensor.util.MeasurementErrorResponce;
import ua.ypon.sensor.util.MeasurementNotCreatedException;
import ua.ypon.sensor.util.MeasurementsNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ua.ypon 19.09.2023
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
        int rainyDays = measurementService.countDayWithRain(allMeasurements);
        return ResponseEntity.ok(rainyDays);
    }

    @PostMapping("/generateRandomData")
    public ResponseEntity<HttpStatus> generateRandomData(@RequestParam("count") int count) {
        for (int i = 0; i < count; i++) {
            SensorDTO randomSensorDTO = RandomSensorDataGenerator.generateRandomSensorDTO();
                Sensor sensor = modelMapper.map(randomSensorDTO, Sensor.class);
                sensorService.save(sensor);

                MeasurementsDTO randomMeasurementsDTO = RandomMeasurementsDataGenerator.generateRandomMeasurementsDTO();
                String sensorName = randomSensorDTO.getName();
                randomMeasurementsDTO.getSensor().setName(sensorName);

                measurementService.save(modelMapper.map(randomMeasurementsDTO, Measurements.class));
            }
        return ResponseEntity.ok(HttpStatus.OK);
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

    private ResponseEntity<MeasurementErrorResponce> handleException(MeasurementsNotFoundException e) {
        MeasurementErrorResponce responce = new MeasurementErrorResponce(
                "this sensor wasn't found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(responce, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<MeasurementErrorResponce> handleException(MeasurementNotCreatedException e) {
        MeasurementErrorResponce responce = new MeasurementErrorResponce(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(responce, HttpStatus.BAD_REQUEST);
    }

    private Measurements convertToMeasurements(MeasurementsDTO measurementsDTO){
        return modelMapper.map(measurementsDTO, Measurements.class);
    }

    private MeasurementsDTO convertToMeasurementsDTO(Measurements measurements) {
        return modelMapper.map(measurements, MeasurementsDTO.class);
    }
}
