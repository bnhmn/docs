package de.insurance.vehiclepricing.region;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
enum State {
  BW(new BigDecimal("1.23"), "Baden-Württemberg"),
  BY(new BigDecimal("0.94"), "Bayern"),
  BE(new BigDecimal("1.45"), "Berlin"),
  BB(new BigDecimal("1.11"), "Brandenburg"),
  HB(new BigDecimal("0.87"), "Bremen"),
  HH(new BigDecimal("1.38"), "Hamburg"),
  HE(new BigDecimal("1.05"), "Hessen"),
  MV(new BigDecimal("1.27"), "Mecklenburg-Vorpommern"),
  NI(new BigDecimal("0.92"), "Niedersachsen"),
  NW(new BigDecimal("1.34"), "Nordrhein-Westfalen"),
  RP(new BigDecimal("0.99"), "Rheinland-Pfalz"),
  SL(new BigDecimal("1.12"), "Saarland"),
  SN(new BigDecimal("1.41"), "Sachsen"),
  ST(new BigDecimal("0.85"), "Sachsen-Anhalt"),
  SH(new BigDecimal("1.18"), "Schleswig-Holstein"),
  TH(new BigDecimal("1.07"), "Thüringen");

  private static final Map<String, State> NAME_TO_STATE = new HashMap<>();

  static {
    for (var state : values()) {
      NAME_TO_STATE.put(state.getName(), state);
    }
  }

  private final BigDecimal factor;
  private final String name;

  State(BigDecimal factor, String name) {
    this.factor = factor;
    this.name = name;
  }

  public static State fromName(String state) {
    return Optional.ofNullable(NAME_TO_STATE.get(state))
        .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
  }
}
