package com.flowerit.florists.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlowerMapperTest {

    private FlowerMapper flowerMapper;

    @BeforeEach
    public void setUp() {
        flowerMapper = new FlowerMapperImpl();
    }
}
