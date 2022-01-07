package com.flowerit.florists.service.mapper;

import com.flowerit.florists.domain.Flower;
import com.flowerit.florists.service.dto.FlowerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Flower} and its DTO {@link FlowerDTO}.
 */
@Mapper(componentModel = "spring", uses = {}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FlowerMapper extends EntityMapper<FlowerDTO, Flower> {}
