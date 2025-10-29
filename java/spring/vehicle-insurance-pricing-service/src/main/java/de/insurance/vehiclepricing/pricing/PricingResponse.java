package de.insurance.vehiclepricing.pricing;

import de.insurance.vehiclepricing.vehicle.VehicleType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Value;

@Value
class PricingResponse {

  /** The entered vehicle type. */
  @NotNull
  VehicleType vehicleType;

  /** The entered postal code of the registration office. */
  @NotNull
  String postalCode;

  /** The entered annual mileage in kilometers. */
  @NotNull
  int annualMileage;

  /** The calculated annual insurance premium in EUR. */
  @NotNull
  BigDecimal annualPremium;

  public static PricingResponse of(Pricing pricing) {
    return new PricingResponse(
        pricing.getVehicleType(),
        pricing.getPostalCode(),
        pricing.getAnnualMileage(),
        pricing.getAnnualPremium());
  }
}
