package com.flowerit.florists.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Measurement {

    @NotNull
    private Integer temperature;

    @NotNull
    private Integer capacitive;

    @NotNull
    private Integer lux;
}
