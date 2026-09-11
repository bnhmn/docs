package de.insurance.vehiclepricing.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VehicleServiceTest {

  @Autowired
  private VehicleService vehicleService;

  @ParameterizedTest
  @MethodSource("mileages")
  void shouldDetermineTheCorrectMileageFactor(int annualMileageKm, String expectedMileageFactor) {
    var actualMileageFactor = vehicleService.computeMileageFactor(annualMileageKm);
    assertThat(actualMileageFactor).isEqualTo(new BigDecimal(expectedMileageFactor));
  }

  private static Stream<Arguments> mileages() {
    return Stream.of(
        arguments(-1, "0.5"),
        arguments(0, "0.5"),
        arguments(1, "0.5"),
        arguments(5_000, "0.5"),
        arguments(5_001, "1.0"),
        arguments(10_000, "1.0"),
        arguments(10_001, "1.5"),
        arguments(20_000, "1.5"),
        arguments(20_0001, "2.0"),
        arguments(100_000, "2.0"));
  }

  @ParameterizedTest
  @MethodSource("vehicleTypes")
  void shouldDetermineTheCorrectTypeFactor(VehicleType vehicleType, String expectedTypeFactor) {
    var actualTypeFactor = vehicleService.computeTypeFactor(vehicleType);
    assertThat(actualTypeFactor).isEqualTo(new BigDecimal(expectedTypeFactor));
  }

  private static Stream<Arguments> vehicleTypes() {
    return Stream.of(
        arguments(VehicleType.CAR, "500.00"),
        arguments(VehicleType.TRUCK, "1700.00"),
        arguments(VehicleType.MOTORCYCLE, "150.00"));
  }
}
