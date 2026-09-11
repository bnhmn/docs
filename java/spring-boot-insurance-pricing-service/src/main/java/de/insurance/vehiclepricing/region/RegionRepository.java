package de.insurance.vehiclepricing.region;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface RegionRepository extends JpaRepository<Region, UUID> {

  Optional<Region> findFirstByPostalCode(String postalCode);
}
