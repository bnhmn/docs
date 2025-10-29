package de.insurance.vehiclepricing.pricing;

import static de.insurance.vehiclepricing.vehicle.VehicleType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import de.insurance.vehiclepricing.pricing.PricingRequest.PricingRequestBuilder;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PricingServiceTest {

  @Autowired
  private PricingService pricingService;

  @Autowired
  private PricingRepository pricingRepository;

  @BeforeEach
  void setUp() {
    pricingRepository.deleteAll();
  }

  @ParameterizedTest
  @MethodSource("pricingRequests")
  void shouldComputeTheCorrectPrice(PricingRequest request, String expectedAnnualPremium) {
    var expectedPremium = new BigDecimal(expectedAnnualPremium);

    var response = pricingService.calculate(request);

    assertThat(response.getVehicleType()).isEqualTo(request.getVehicleType());
    assertThat(response.getPostalCode()).isEqualTo(request.getPostalCode());
    assertThat(response.getAnnualMileage()).isEqualTo(request.getAnnualMileage());
    assertThat(response.getAnnualPremium()).isEqualTo(expectedPremium);

    assertThat(pricingRepository.findAll()).singleElement().satisfies(record -> {
      assertThat(record.getVehicleType()).isEqualTo(request.getVehicleType());
      assertThat(record.getPostalCode()).isEqualTo(request.getPostalCode());
      assertThat(record.getAnnualMileage()).isEqualTo(request.getAnnualMileage());
      assertThat(record.getAnnualPremium()).isEqualTo(expectedPremium);
    });
  }

  private static Stream<Arguments> pricingRequests() {
    // spotless:off
    return Stream.of(
        arguments(aRequest().vehicleType(CAR).postalCode("50667").annualMileage(10_000).build(), "670.00"),
        arguments(aRequest().vehicleType(TRUCK).postalCode("50667").annualMileage(80_000).build(), "4556.00"),
        arguments(aRequest().vehicleType(MOTORCYCLE).postalCode("50667").annualMileage(5_000).build(), "100.50")
    );
    // spotless:on
  }

  private static PricingRequestBuilder aRequest() {
    return PricingRequest.builder();
  }
}
