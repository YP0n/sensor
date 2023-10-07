package ua.ypon.sensor.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ypon.sensor.dto.MeasurementsDTO;
import ua.ypon.sensor.models.Measurements;
import ua.ypon.sensor.models.Sensor;
import ua.ypon.sensor.repositories.SensorRepository;
import ua.ypon.sensor.services.MeasurementService;
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
    private final ModelMapper modelMapper;
    private final SensorRepository sensorRepository;

    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper, SensorRepository sensorRepository) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
        this.sensorRepository = sensorRepository;
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

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MeasurementsDTO measurementsDTO,
                                             /*BindingResult bindingResult,*/
                                             @RequestParam("sensor_id") int sensor_id) {
        Sensor sensorOwner = sensorRepository.findById(sensor_id)
                .orElseThrow(() -> new MeasurementNotCreatedException("Sensor from id " + sensor_id + " not found"));

//        if(bindingResult.hasErrors()) {
//            StringBuilder errorMsg = new StringBuilder();
//
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            for(FieldError error: errors) {
//                errorMsg.append(error.getField())
//                        .append(" - ").append(error.getDefaultMessage())
//                        .append(";");
//            }
//            throw new MeasurementNotCreatedException(errorMsg.toString());
//        }
            measurementService.save(convertToMeasurements(measurementsDTO, sensorOwner));
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

    private Measurements convertToMeasurements(MeasurementsDTO measurementsDTO, Sensor sensorOwner){
        return modelMapper.map(measurementsDTO, Measurements.class);
    }

    private MeasurementsDTO convertToMeasurementsDTO(Measurements measurements) {
        return modelMapper.map(measurements, MeasurementsDTO.class);
    }
}
