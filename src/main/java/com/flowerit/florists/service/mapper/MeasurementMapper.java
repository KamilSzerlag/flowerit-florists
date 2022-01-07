package com.flowerit.florists.service.mapper;

import com.flowerit.florists.domain.Measurement;
import com.flowerit.florists.service.dto.MeasurementDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MeasurementMapper extends EntityMapper<MeasurementDTO, Measurement> {}
