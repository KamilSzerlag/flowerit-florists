package com.flowerit.florists.service;

import com.flowerit.florists.domain.Flower;
import com.flowerit.florists.repository.FlowerRepository;
import com.flowerit.florists.service.dto.FlowerDTO;
import com.flowerit.florists.service.mapper.FlowerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Flower}.
 */
@Service
public class FlowerService {

    private final Logger log = LoggerFactory.getLogger(FlowerService.class);

    private final FlowerRepository flowerRepository;

    private final FlowerMapper flowerMapper;

    public FlowerService(FlowerRepository flowerRepository, FlowerMapper flowerMapper) {
        this.flowerRepository = flowerRepository;
        this.flowerMapper = flowerMapper;
    }

    /**
     * Save a flower.
     *
     * @param flowerDTO the entity to save.
     * @return the persisted entity.
     */
    public FlowerDTO save(FlowerDTO flowerDTO) {
        log.debug("Request to save Flower : {}", flowerDTO);
        Flower flower = flowerMapper.toEntity(flowerDTO);
        flower = flowerRepository.save(flower);
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
}
