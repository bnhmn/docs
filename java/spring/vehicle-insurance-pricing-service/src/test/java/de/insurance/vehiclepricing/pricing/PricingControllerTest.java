package de.insurance.vehiclepricing.pricing;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PricingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldComputeTheCorrectPriceForAValidRequest() throws Exception {
    mockMvc
        .perform(
            post("/pricing/calculations")
                .contentType(APPLICATION_JSON)
                .content(
                    """
                    {
                      "vehicleType": "CAR",
                      "postalCode": "50667",
                      "annualMileage": 10000
                    }
                    """))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    """
                    {
                      "vehicleType": "CAR",
                      "postalCode": "50667",
                      "annualMileage": 10000,
                      "annualPremium": 670.00
                    }
                    """));
  }

  @ParameterizedTest
  @MethodSource("invalidRequests")
  void shouldRejectInvalidRequests(String invalidRequest) throws Exception {
    mockMvc
        .perform(
            post("/pricing/calculations").contentType(APPLICATION_JSON).content(invalidRequest))
        .andExpect(status().isBadRequest());
  }

  private static Stream<String> invalidRequests() {
    return Stream.of(
        """
        { "vehicleType": "CAR", "postalCode": "50667" }
        """,
        """
        { "vehicleType": "CAR", "annualMileage": 10000 }
        """,
        """
        { "postalCode": "50667", "annualMileage": 10000 }
        """,
        """
        { "vehicleType": null, "postalCode": "50667", "annualMileage": 10000 }
        """,
        """
        { "vehicleType": "unknown-type", "postalCode": "50667", "annualMileage": 10000 }
        """,
        """
        { "vehicleType": "CAR", "postalCode": null, "annualMileage": 10000 }
        """,
        """
        { "vehicleType": "CAR", "postalCode": "123456", "annualMileage": 10000 }
        """,
        """
        { "vehicleType": "CAR", "postalCode": "50667", "annualMileage": null }
        """,
        """
        { "vehicleType": "CAR", "postalCode": "50667", "annualMileage": -1 }
        """);
  }
}
