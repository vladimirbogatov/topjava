package ru.javawebinar.topjava.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ResourceControllerTest extends AbstractControllerTest {

    @Test
    void resources() throws Exception {
        perform(get("/resources/css/style.css"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.valueOf("text/css")))
                .andExpect(status().isOk());
    }
}
