package com.flowerit.florists.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.flowerit.florists.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlowerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Flower.class);
        Flower flower1 = new Flower();
        flower1.setId("id1");
        Flower flower2 = new Flower();
        flower2.setId(flower1.getId());
        assertThat(flower1).isEqualTo(flower2);
        flower2.setId("id2");
        assertThat(flower1).isNotEqualTo(flower2);
        flower1.setId(null);
        assertThat(flower1).isNotEqualTo(flower2);
    }
}
