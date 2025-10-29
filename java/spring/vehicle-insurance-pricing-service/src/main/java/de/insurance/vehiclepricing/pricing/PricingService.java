package de.insurance.vehiclepricing.pricing;

import de.insurance.vehiclepricing.region.RegionService;
import de.insurance.vehiclepricing.vehicle.VehicleService;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class PricingService {

  private final RegionService regionService;
  private final VehicleService vehicleService;
  private final PricingRepository pricingRepository;

  /**
   * Calculate the annual insurance premium for a vehicle based on mileage, vehicle type, and
   * region.
   *
   * <pre>
   * annualPremium = mileageFactor * typeFactor * regionFactor
   * </pre>
   */
  public PricingResponse calculate(PricingRequest request) {
    var mileageFactor = vehicleService.computeMileageFactor(request.getAnnualMileage());
    var typeFactor = vehicleService.computeTypeFactor(request.getVehicleType());
    var regionFactor =
        // Normally, we should implement a proper error handling for the region-not-found case
        regionService.findRegionFactor(request.getPostalCode()).orElseThrow();

    var annualPremium =
        mileageFactor.multiply(typeFactor).multiply(regionFactor).setScale(2, RoundingMode.HALF_UP);
    log.debug(
        "The annual premium is {}€ for mileageFactor={} typeFactor={} regionFactor={}",
        annualPremium,
        mileageFactor,
        typeFactor,
        regionFactor);

    var pricing = Pricing.of(request, annualPremium);
    pricingRepository.save(pricing);

    return PricingResponse.of(pricing);
  }
}
