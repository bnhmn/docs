export type VehicleType = "CAR" | "TRUCK" | "MOTORCYCLE";

export interface PricingRequest {
  vehicleType: VehicleType;
  postalCode: string;
  annualMileage: number;
}

export interface PricingResponse {
  vehicleType: VehicleType;
  postalCode: string;
  annualMileage: number;
  annualPremium: number;
}

const API_BASE_URL = "http://localhost:8080";

export async function calculatePremium(request: PricingRequest): Promise<PricingResponse> {
  try {
    const response = await fetch(`${API_BASE_URL}/pricing/calculations`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      throw new Error(`HTTP error ${response.status}!`);
    }

    const result: PricingResponse = await response.json();
    return result;
  } catch (error) {
    console.error("Error calculating premium:", error);
    throw new Error("Fehler beim Berechnen der Prämie. Bitte versuchen Sie es erneut.");
  }
}
