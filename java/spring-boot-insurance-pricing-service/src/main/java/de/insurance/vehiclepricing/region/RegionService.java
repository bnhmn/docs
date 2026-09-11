package de.insurance.vehiclepricing.region;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionService {

  private final RegionRepository regionRepository;

  /**
   * Determine the region-specific insurance premium factor based on the location of the
   * registration office.
   *
   * @param postalCode The postal code of the registration office.
   * @return The determined region factor or <i>empty</i> if the provided postal code is unknown.
   */
  public Optional<BigDecimal> findRegionFactor(String postalCode) {
    // According to the requirements, we assume that the region factor can be determined using
    // only the postal code.
    // We ignore the possibility that a postal code may correspond to multiple regions/states.
    return regionRepository.findFirstByPostalCode(postalCode).map(Region::getFactor);
  }
}
