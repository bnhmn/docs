package de.insurance.vehiclepricing.pricing;

import de.insurance.vehiclepricing.vehicle.VehicleType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
class PricingRequest {

  /** The entered vehicle type. */
  @NotNull
  VehicleType vehicleType;

  /** The entered postal code of the registration office. */
  @NotNull
  @Digits(integer = 5, fraction = 0)
  String postalCode;

  /** The entered annual mileage in kilometers. */
  @NotNull
  @Min(0)
  @Max(100_000)
  Integer annualMileage;
}
