package de.insurance.vehiclepricing.pricing;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/pricing")
@RequiredArgsConstructor
class PricingController {

  private final PricingService pricingService;

  @PostMapping("/calculations")
  public ResponseEntity<PricingResponse> calculate(@Valid @RequestBody PricingRequest request) {
    var pricing = pricingService.calculate(request);
    return ResponseEntity.ok(pricing);
  }
}
