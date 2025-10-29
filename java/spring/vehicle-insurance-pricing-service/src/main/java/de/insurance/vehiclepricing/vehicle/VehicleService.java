package de.insurance.vehiclepricing.vehicle;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

  /** Determine the mileage-specific insurance premium factor. */
  public BigDecimal computeMileageFactor(int annualMileageKilometers) {
    if (annualMileageKilometers <= 5000) {
      return new BigDecimal("0.5");
    } else if (annualMileageKilometers <= 10_000) {
      return new BigDecimal("1.0");
    } else if (annualMileageKilometers <= 20_000) {
      return new BigDecimal("1.5");
    } else {
      return new BigDecimal("2.0");
    }
  }

  /** Determine the vehicle-type-specific insurance premium factor. */
  public BigDecimal computeTypeFactor(VehicleType vehicleType) {
    return switch (vehicleType) {
      case CAR -> new BigDecimal("500.00");
      case TRUCK -> new BigDecimal("1700.00");
      case MOTORCYCLE -> new BigDecimal("150.00");
    };
  }
}
