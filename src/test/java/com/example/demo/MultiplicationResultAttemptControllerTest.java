package com.example.demo;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import com.example.demo.MultiplicationResultAttemptController.ResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Matchers.any;
import static org.mockito.BDDMockito.given;


@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {
    
    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<MultiplicationResultAttempt>jsonResult;
    private JacksonTester<ResultResponse> jsonResponse;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericParameterizedTest(true);
    }

    @Test
    public void postResultReturnNotCorrect() throws Exception {
        genericParameterizedTest(false);
    }

    void genericParameterizedTest(final boolean correct) throws Exception {
        given(
            multiplicationService
            .checkAttempt(
                any(
                    MultiplicationResultAttempt.class
                )
            )
        ).willReturn(correct);

        User user = new User("john");
        Multiplication multiplication = new Multiplication (50, 70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
            user, multiplication, 3500
        );

        MockHttpServletResponse response = mvc.perform(
            post("/results").contentType(
                MediaType.APPLICATION_JSON
            ).content(
                jsonResult.write(attempt).getJson()
            )
        ).andReturn().getResponse();

        assertThat(
            response.getStatus()
        ).isEqualTo(
            HttpStatus.OK.value()
        );

        assertThat(
            response.getContentAsString()
        ).isEqualTo(
            jsonResponse.write(
                new ResultResponse(correct)
            ).getJson()
        );
    }
}