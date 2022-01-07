package com.flowerit.florists.service.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class MeasurementDTO implements Serializable {

    private String deviceId;

    private Integer temperature;

    private Integer capacitive;

    private Integer lux;
}
