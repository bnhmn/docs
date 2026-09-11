package de.insurance.vehiclepricing.region;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RegionServiceTest {

  @Autowired
  private RegionService regionService;

  @ParameterizedTest
  @MethodSource("regions")
  void shouldReturnTheCorrectRegionFactorForKnownPostalCodes(
      String postalCode, String expectedRegionFactor) {
    var actualRegionFactor = regionService.findRegionFactor(postalCode);
    assertThat(actualRegionFactor).get().isEqualTo(new BigDecimal(expectedRegionFactor));
  }

  private static Stream<Arguments> regions() {
    return Stream.of(
        arguments("01067", "1.41"), // Sachsen
        arguments("10115", "1.45"), // Berlin
        arguments("14467", "1.11"), // Brandenburg
        arguments("19053", "1.27"), // Mecklenburg-Vorpommern
        arguments("20095", "1.38"), // Hamburg
        arguments("24103", "1.18"), // Schleswig-Holstein
        arguments("28195", "0.87"), // Bremen
        arguments("30159", "0.92"), // Niedersachsen
        arguments("34117", "1.05"), // Hessen
        arguments("39104", "0.85"), // Sachsen-Anhalt
        arguments("50667", "1.34"), // Nordrhein-Westfalen
        arguments("55116", "0.99"), // Rheinland-Pfalz
        arguments("66111", "1.12"), // Saarland
        arguments("68159", "1.23"), // Baden-Württemberg
        arguments("80331", "0.94"), // Bayern
        arguments("99084", "1.07") // Thüringen
        );
  }

  @Test
  void shouldReturnEmptyForUnknownPostalCode() {
    var unknownPostalCode = "00000";
    var actualRegionFactor = regionService.findRegionFactor(unknownPostalCode);
    assertThat(actualRegionFactor).isEmpty();
  }
}
