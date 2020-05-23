package com.app.co2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(args = {"--start Islamabad", "--end Lahore", "--transportation-method=medium-diesel-car"})
class Co2ApplicationTests {

    @Test
    void contextLoads() {
    }

}
