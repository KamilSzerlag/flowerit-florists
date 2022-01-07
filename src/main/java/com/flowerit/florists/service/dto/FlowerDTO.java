package com.flowerit.florists.service.dto;

import com.flowerit.florists.domain.enumeration.State;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.flowerit.florists.domain.Flower} entity.
 */
@ApiModel(description = "Entities for Flortists")
public class FlowerDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private State state;

    private String wikiReference;

    private byte[] image;

    private String imageContentType;
    private String deviceId;

    private String ownerId;

    private MeasurementDTO measurement;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getWikiReference() {
        return wikiReference;
    }

    public void setWikiReference(String wikiReference) {
        this.wikiReference = wikiReference;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public MeasurementDTO getMeasurement() {
        return measurement;
    }

    public void setMeasurement(MeasurementDTO measurement) {
        this.measurement = measurement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlowerDTO)) {
            return false;
        }

        FlowerDTO flowerDTO = (FlowerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, flowerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlowerDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", state='" + getState() + "'" +
            ", wikiReference='" + getWikiReference() + "'" +
            ", image='" + getImage() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", ownerId='" + getOwnerId() + "'" +
            "}";
    }
}
