package de.insurance.vehiclepricing.region;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
class Region {

  @Id
  private UUID id;

  private String state;

  private String district;

  private String city;

  private String postalCode;

  private String location;

  public State getState() {
    return State.fromName(state);
  }

  /**
   * The region factor for calculating the insurance premium. It is based on the federal state in
   * which the region is located.
   */
  public BigDecimal getFactor() {
    return getState().getFactor();
  }
}
