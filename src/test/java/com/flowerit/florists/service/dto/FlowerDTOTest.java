package com.flowerit.florists.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.flowerit.florists.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlowerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlowerDTO.class);
        FlowerDTO flowerDTO1 = new FlowerDTO();
        flowerDTO1.setId("id1");
        FlowerDTO flowerDTO2 = new FlowerDTO();
        assertThat(flowerDTO1).isNotEqualTo(flowerDTO2);
        flowerDTO2.setId(flowerDTO1.getId());
        assertThat(flowerDTO1).isEqualTo(flowerDTO2);
        flowerDTO2.setId("id2");
        assertThat(flowerDTO1).isNotEqualTo(flowerDTO2);
        flowerDTO1.setId(null);
        assertThat(flowerDTO1).isNotEqualTo(flowerDTO2);
    }
}
