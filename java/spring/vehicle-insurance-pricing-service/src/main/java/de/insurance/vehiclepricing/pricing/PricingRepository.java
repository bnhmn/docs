package de.insurance.vehiclepricing.pricing;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface PricingRepository extends JpaRepository<Pricing, UUID> {}
