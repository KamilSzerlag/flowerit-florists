package com.flowerit.florists.service;

import com.flowerit.florists.domain.Flower;
import com.flowerit.florists.repository.FlowerRepository;
import com.flowerit.florists.service.dto.FlowerDTO;
import com.flowerit.florists.service.dto.MeasurementDTO;
import com.flowerit.florists.service.mapper.FlowerMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Flower}.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FlowerService {

    private final Logger log = LoggerFactory.getLogger(FlowerService.class);

    private final FlowerRepository flowerRepository;

    private final FlowerMapper flowerMapper;

    /**
     * Save a flower.
     *
     * @param flowerDTO the entity to save.
     * @return the persisted entity.
     */
    public FlowerDTO save(final FlowerDTO flowerDTO) {
        log.debug("Request to save Flower : {}", flowerDTO);
        Flower flower = flowerMapper.toEntity(flowerDTO);
        flower = flowerRepository.save(flower);
        return flowerMapper.toDto(flower);
    }

    public FlowerDTO update(final FlowerDTO flowerDTO) {
        Flower flower = flowerRepository
            .findById(flowerDTO.getId())
            .map(existingFlower -> {
                flowerMapper.partialUpdate(existingFlower, flowerDTO);
                return existingFlower;
            })
            .orElseGet(() -> {
                Flower newFlower = flowerMapper.toEntity(flowerDTO);
                return flowerRepository.save(newFlower);
            });
        return flowerMapper.toDto(flower);
    }

    /**
     * Partially update a flower.
     *
     * @param flowerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FlowerDTO> partialUpdate(FlowerDTO flowerDTO) {
        log.debug("Request to partially update Flower : {}", flowerDTO);

        return flowerRepository
            .findById(flowerDTO.getId())
            .map(existingFlower -> {
                flowerMapper.partialUpdate(existingFlower, flowerDTO);

                return existingFlower;
            })
            .map(flowerRepository::save)
            .map(flowerMapper::toDto);
    }

    /**
     * Get all the flowers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<FlowerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Flowers");
        return flowerRepository.findAll(pageable).map(flowerMapper::toDto);
    }

    /**
     * Get all the flowers that belongs to User.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<FlowerDTO> findByUser(String userId, Pageable pageable) {
        log.debug("Request to get all Flowers belongs to User");
        return flowerRepository.findByCreatedBy(userId, pageable).map(flowerMapper::toDto);
    }

    /**
     * Get one flower by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<FlowerDTO> findOne(String id) {
        log.debug("Request to get Flower : {}", id);
        return flowerRepository.findById(id).map(flowerMapper::toDto);
    }

    /**
     * Delete the flower by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Flower : {}", id);
        flowerRepository.deleteById(id);
    }

    @KafkaListener(topics = "flowerit_measurements", groupId = "group_florists")
    public void updateMeasurement(final MeasurementDTO measurementDTO) {
        flowerRepository
            .findFirstByDeviceId(measurementDTO.getDeviceId())
            .map(flower -> flower.updateMeasurement(measurementDTO))
            .ifPresent(flowerRepository::save);
    }
}
