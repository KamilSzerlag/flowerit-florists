package com.flowerit.florists.web.rest;

import com.flowerit.florists.repository.FlowerRepository;
import com.flowerit.florists.security.DomainUser;
import com.flowerit.florists.service.FlowerService;
import com.flowerit.florists.service.dto.FlowerDTO;
import com.flowerit.florists.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.flowerit.florists.domain.Flower}.
 */
@RestController
@RequestMapping("/api")
public class FlowerResource {

    private final Logger log = LoggerFactory.getLogger(FlowerResource.class);

    private static final String ENTITY_NAME = "floristsFlower";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlowerService flowerService;

    private final FlowerRepository flowerRepository;

    public FlowerResource(FlowerService flowerService, FlowerRepository flowerRepository) {
        this.flowerService = flowerService;
        this.flowerRepository = flowerRepository;
    }

    /**
     * {@code POST  /flowers} : Create a new flower.
     *
     * @param flowerDTO the flowerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flowerDTO, or with status {@code 400 (Bad Request)} if the flower has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flowers")
    public ResponseEntity<FlowerDTO> createFlower(@Valid @RequestBody FlowerDTO flowerDTO) throws URISyntaxException {
        log.debug("REST request to save Flower : {}", flowerDTO);
        if (flowerDTO.getId() != null) {
            throw new BadRequestAlertException("A new flower cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlowerDTO result = flowerService.save(flowerDTO);
        return ResponseEntity
            .created(new URI("/api/flowers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /flowers/:id} : Updates an existing flower.
     *
     * @param id the id of the flowerDTO to save.
     * @param flowerDTO the flowerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowerDTO,
     * or with status {@code 400 (Bad Request)} if the flowerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flowerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flowers/{id}")
    public ResponseEntity<FlowerDTO> updateFlower(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FlowerDTO flowerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Flower : {}, {}", id, flowerDTO);
        if (flowerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlowerDTO result = flowerService.save(flowerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flowerDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /flowers/:id} : Partial updates given fields of an existing flower, field will ignore if it is null
     *
     * @param id the id of the flowerDTO to save.
     * @param flowerDTO the flowerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowerDTO,
     * or with status {@code 400 (Bad Request)} if the flowerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the flowerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the flowerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flowers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FlowerDTO> partialUpdateFlower(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FlowerDTO flowerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Flower partially : {}, {}", id, flowerDTO);
        if (flowerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlowerDTO> result = flowerService.partialUpdate(flowerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flowerDTO.getId())
        );
    }

    /**
     * {@code GET  /flowers} : get all the flowers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flowers in body.
     */
    @GetMapping("/flowers")
    public ResponseEntity<List<FlowerDTO>> getAllFlowers(Pageable pageable, @AuthenticationPrincipal DomainUser domainUser) {
        log.debug("REST request to get a page of Flowers");
        Page<FlowerDTO> page = flowerService.findByUser(domainUser.getId(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flowers/:id} : get the "id" flower.
     *
     * @param id the id of the flowerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flowerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flowers/{id}")
    public ResponseEntity<FlowerDTO> getFlower(@PathVariable String id) {
        log.debug("REST request to get Flower : {}", id);
        Optional<FlowerDTO> flowerDTO = flowerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flowerDTO);
    }

    /**
     * {@code DELETE  /flowers/:id} : delete the "id" flower.
     *
     * @param id the id of the flowerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flowers/{id}")
    public ResponseEntity<Void> deleteFlower(@PathVariable String id) {
        log.debug("REST request to delete Flower : {}", id);
        flowerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
