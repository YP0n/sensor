package ua.ypon.sensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ypon.sensor.models.Measurements;
import ua.ypon.sensor.repositories.MeasurementRepository;

import java.util.List;
import java.util.Optional;

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
        Optional<Measurements> foundMasurements = measurementRepository.findById(id);
        return foundMasurements.orElse(null);
    }


    @Transactional
    public void save(Measurements measurements) {
        measurementRepository.save(measurements);
    }
}
