package com.flowerit.florists.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.flowerit.florists.IntegrationTest;
import com.flowerit.florists.domain.Flower;
import com.flowerit.florists.domain.enumeration.State;
import com.flowerit.florists.repository.FlowerRepository;
import com.flowerit.florists.service.dto.FlowerDTO;
import com.flowerit.florists.service.mapper.FlowerMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FlowerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlowerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final State DEFAULT_STATE = State.BAD_CONDITIONS;
    private static final State UPDATED_STATE = State.GOOD_CONDITIONS;

    private static final String DEFAULT_WIKI_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_WIKI_REFERENCE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flowers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FlowerRepository flowerRepository;

    @Autowired
    private FlowerMapper flowerMapper;

    @Autowired
    private MockMvc restFlowerMockMvc;

    private Flower flower;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flower createEntity() {
        Flower flower = new Flower()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .state(DEFAULT_STATE)
            .wikiReference(DEFAULT_WIKI_REFERENCE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .deviceId(DEFAULT_DEVICE_ID)
            .ownerId(DEFAULT_OWNER_ID);
        return flower;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flower createUpdatedEntity() {
        Flower flower = new Flower()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .state(UPDATED_STATE)
            .wikiReference(UPDATED_WIKI_REFERENCE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .deviceId(UPDATED_DEVICE_ID)
            .ownerId(UPDATED_OWNER_ID);
        return flower;
    }

    @BeforeEach
    public void initTest() {
        flowerRepository.deleteAll();
        flower = createEntity();
    }

    @Test
    void createFlower() throws Exception {
        int databaseSizeBeforeCreate = flowerRepository.findAll().size();
        // Create the Flower
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);
        restFlowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowerDTO)))
            .andExpect(status().isCreated());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeCreate + 1);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFlower.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlower.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testFlower.getWikiReference()).isEqualTo(DEFAULT_WIKI_REFERENCE);
        assertThat(testFlower.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFlower.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testFlower.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testFlower.getOwnerId()).isEqualTo(DEFAULT_OWNER_ID);
    }

    @Test
    void createFlowerWithExistingId() throws Exception {
        // Create the Flower with an existing ID
        flower.setId("existing_id");
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);

        int databaseSizeBeforeCreate = flowerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerRepository.findAll().size();
        // set the field null
        flower.setName(null);

        // Create the Flower, which fails.
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);

        restFlowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowerDTO)))
            .andExpect(status().isBadRequest());

        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowerRepository.findAll().size();
        // set the field null
        flower.setDescription(null);

        // Create the Flower, which fails.
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);

        restFlowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowerDTO)))
            .andExpect(status().isBadRequest());

        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFlowers() throws Exception {
        // Initialize the database
        flowerRepository.save(flower);

        // Get all the flowerList
        restFlowerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flower.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].wikiReference").value(hasItem(DEFAULT_WIKI_REFERENCE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].ownerId").value(hasItem(DEFAULT_OWNER_ID)));
    }

    @Test
    void getFlower() throws Exception {
        // Initialize the database
        flowerRepository.save(flower);

        // Get the flower
        restFlowerMockMvc
            .perform(get(ENTITY_API_URL_ID, flower.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flower.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.wikiReference").value(DEFAULT_WIKI_REFERENCE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID))
            .andExpect(jsonPath("$.ownerId").value(DEFAULT_OWNER_ID));
    }

    @Test
    void getNonExistingFlower() throws Exception {
        // Get the flower
        restFlowerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewFlower() throws Exception {
        // Initialize the database
        flowerRepository.save(flower);

        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();

        // Update the flower
        Flower updatedFlower = flowerRepository.findById(flower.getId()).get();
        updatedFlower
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .state(UPDATED_STATE)
            .wikiReference(UPDATED_WIKI_REFERENCE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .deviceId(UPDATED_DEVICE_ID)
            .ownerId(UPDATED_OWNER_ID);
        FlowerDTO flowerDTO = flowerMapper.toDto(updatedFlower);

        restFlowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flowerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFlower.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlower.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testFlower.getWikiReference()).isEqualTo(UPDATED_WIKI_REFERENCE);
        assertThat(testFlower.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFlower.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testFlower.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testFlower.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
    }

    @Test
    void putNonExistingFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(UUID.randomUUID().toString());

        // Create the Flower
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flowerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(UUID.randomUUID().toString());

        // Create the Flower
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(UUID.randomUUID().toString());

        // Create the Flower
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFlowerWithPatch() throws Exception {
        // Initialize the database
        flowerRepository.save(flower);

        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();

        // Update the flower using partial update
        Flower partialUpdatedFlower = new Flower();
        partialUpdatedFlower.setId(flower.getId());

        partialUpdatedFlower
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .wikiReference(UPDATED_WIKI_REFERENCE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restFlowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlower.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlower))
            )
            .andExpect(status().isOk());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFlower.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlower.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testFlower.getWikiReference()).isEqualTo(UPDATED_WIKI_REFERENCE);
        assertThat(testFlower.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFlower.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testFlower.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testFlower.getOwnerId()).isEqualTo(DEFAULT_OWNER_ID);
    }

    @Test
    void fullUpdateFlowerWithPatch() throws Exception {
        // Initialize the database
        flowerRepository.save(flower);

        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();

        // Update the flower using partial update
        Flower partialUpdatedFlower = new Flower();
        partialUpdatedFlower.setId(flower.getId());

        partialUpdatedFlower
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .state(UPDATED_STATE)
            .wikiReference(UPDATED_WIKI_REFERENCE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .deviceId(UPDATED_DEVICE_ID)
            .ownerId(UPDATED_OWNER_ID);

        restFlowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlower.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlower))
            )
            .andExpect(status().isOk());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
        Flower testFlower = flowerList.get(flowerList.size() - 1);
        assertThat(testFlower.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFlower.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlower.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testFlower.getWikiReference()).isEqualTo(UPDATED_WIKI_REFERENCE);
        assertThat(testFlower.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFlower.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testFlower.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testFlower.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
    }

    @Test
    void patchNonExistingFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(UUID.randomUUID().toString());

        // Create the Flower
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flowerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(UUID.randomUUID().toString());

        // Create the Flower
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFlower() throws Exception {
        int databaseSizeBeforeUpdate = flowerRepository.findAll().size();
        flower.setId(UUID.randomUUID().toString());

        // Create the Flower
        FlowerDTO flowerDTO = flowerMapper.toDto(flower);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flowerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flower in the database
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFlower() throws Exception {
        // Initialize the database
        flowerRepository.save(flower);

        int databaseSizeBeforeDelete = flowerRepository.findAll().size();

        // Delete the flower
        restFlowerMockMvc
            .perform(delete(ENTITY_API_URL_ID, flower.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Flower> flowerList = flowerRepository.findAll();
        assertThat(flowerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
