package de.insurance.vehiclepricing.pricing;

import de.insurance.vehiclepricing.vehicle.VehicleType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
class Pricing {

  @Id
  private final UUID id = UUID.randomUUID();

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
  private BigDecimal annualPremium;

  public static Pricing of(PricingRequest request, BigDecimal annualPremium) {
    var pricing = new Pricing();
    pricing.setVehicleType(request.getVehicleType());
    pricing.setPostalCode(request.getPostalCode());
    pricing.setAnnualMileage(request.getAnnualMileage());
    pricing.setAnnualPremium(annualPremium);
    return pricing;
  }
}
