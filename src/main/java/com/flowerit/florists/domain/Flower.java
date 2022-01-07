package com.flowerit.florists.domain;

import com.flowerit.florists.domain.enumeration.State;
import com.flowerit.florists.service.dto.MeasurementDTO;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Entities for Flortists
 */
@Document(collection = "flowers")
public class Flower extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("description")
    private String description;

    @Field("state")
    private State state;

    @Field("wiki_reference")
    private String wikiReference;

    @Field("image")
    private byte[] image;

    @Field("image_content_type")
    private String imageContentType;

    @Field("device_id")
    private String deviceId;

    @Field("owner_id")
    @CreatedBy
    private String ownerId;

    @Field("measurement")
    private Measurement measurement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Flower id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Flower name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Flower description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return this.state;
    }

    public Flower state(State state) {
        this.setState(state);
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getWikiReference() {
        return this.wikiReference;
    }

    public Flower wikiReference(String wikiReference) {
        this.setWikiReference(wikiReference);
        return this;
    }

    public void setWikiReference(String wikiReference) {
        this.wikiReference = wikiReference;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Flower image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Flower imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public Flower deviceId(String deviceId) {
        this.setDeviceId(deviceId);
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public Flower ownerId(String ownerId) {
        this.setOwnerId(ownerId);
        return this;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    /**
     * Method in accordance with
     * DDD Convention
     *
     * @param measurementDTO
     * @return
     */
    public Flower updateMeasurement(final MeasurementDTO measurementDTO) {
        if (measurementDTO == null) {
            throw new IllegalArgumentException("Measurement object can not be null!");
        }
        this.measurement = new Measurement(measurementDTO.getTemperature(), measurementDTO.getCapacitive(), measurementDTO.getLux());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Flower)) {
            return false;
        }
        return id != null && id.equals(((Flower) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Flower{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", state='" + getState() + "'" +
            ", wikiReference='" + getWikiReference() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", ownerId='" + getOwnerId() + "'" +
            "}";
    }
}
