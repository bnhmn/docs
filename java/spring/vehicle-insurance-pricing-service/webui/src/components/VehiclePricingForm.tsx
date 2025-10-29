import { useState } from "react";
import {
  calculatePremium,
  type PricingRequest,
  type PricingResponse,
  type VehicleType,
} from "../services/pricing";

const vehicleLabels: { type: VehicleType; label: string }[] = [
  { type: "CAR", label: "🚗 Auto" },
  { type: "TRUCK", label: "🚚 LKW" },
  { type: "MOTORCYCLE", label: "🏍️ Motorrad" },
];

export function VehiclePricingForm() {
  const [result, setResult] = useState<PricingResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [formData, setFormData] = useState({
    vehicleType: "CAR" as VehicleType,
    postalCode: "",
    annualMileage: "",
  });
  const errors: { [key in keyof PricingRequest]?: boolean } = {
    postalCode: !validatePostalCode(formData.postalCode),
    annualMileage: !validateAnnualMileage(formData.annualMileage),
  };
  const isInvalid = Object.values(errors).some(Boolean);
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = async () => {
    setSubmitted(true);
    if (isInvalid) {
      return;
    }
    try {
      setLoading(true);
      setError(null);
      const mileage = parseMileage(formData.annualMileage);
      const result = await calculatePremium({
        vehicleType: formData.vehicleType as VehicleType,
        postalCode: formData.postalCode,
        annualMileage: mileage,
      });
      setResult(result);
    } catch (error) {
      setError(error instanceof Error ? error.message : "Ein unerwarteter Fehler ist aufgetreten");
      setResult(null);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (field: keyof PricingRequest, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const handleMileageBlur = () => {
    if (formData.annualMileage) {
      const numValue = parseMileage(formData.annualMileage);
      if (!isNaN(numValue) && numValue > 0) {
        const rounded = roundToNearest100(numValue);
        const formatted = formatMileage(rounded);
        setFormData((prev) => ({ ...prev, annualMileage: formatted }));
      }
    }
  };

  const handleMileageAdjust = (adjustment: number) => {
    const currentValue = formData.annualMileage ? parseMileage(formData.annualMileage) : 0;
    const newValue = Math.max(100, currentValue + adjustment);
    const formatted = formatMileage(newValue);
    setFormData((prev) => ({ ...prev, annualMileage: formatted }));
  };

  return (
    <form
      onSubmit={(event) => {
        event.preventDefault();
        handleSubmit();
      }}
      className="pricing-form"
    >
      <div className="form-header">
        <h1>KFZ-Versicherungsrechner</h1>
        <p>Erhalte ein Angebot für Deine KFZ-Versicherung</p>
      </div>

      <div className="form-group">
        <label htmlFor="vehicleType" className="form-label">
          Fahrzeugtyp
        </label>
        <select
          id="vehicleType"
          value={formData.vehicleType}
          onChange={(e) => handleInputChange("vehicleType", e.target.value)}
          className="form-select"
        >
          {vehicleLabels.map((type) => (
            <option key={type.type} value={type.type}>
              {type.label}
            </option>
          ))}
        </select>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="postalCode" className="form-label">
            Postleitzahl
          </label>
          <input
            id="postalCode"
            type="text"
            value={formData.postalCode}
            onChange={(e) => handleInputChange("postalCode", e.target.value)}
            className={`form-input ${submitted && errors.postalCode ? "error" : ""}`}
            placeholder="50667"
            maxLength={5}
          />
          {submitted && errors.postalCode && (
            <span className="error-message">
              Bitte gib eine gültige 5-stellige Postleitzahl ein
            </span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="annualMileage" className="form-label">
            Jährliche Laufleistung
          </label>
          <div className="mileage-input-container">
            <input
              id="annualMileage"
              type="text"
              value={formData.annualMileage}
              onChange={(e) => handleInputChange("annualMileage", e.target.value)}
              onBlur={handleMileageBlur}
              className={`form-input ${submitted && errors.annualMileage ? "error" : ""}`}
              placeholder="10.000 km"
              maxLength={10}
            />
            <div className="mileage-buttons">
              <button
                type="button"
                className="mileage-button up"
                onClick={() => handleMileageAdjust(100)}
                aria-label="Erhöhe um 100 km"
              >
                ▲
              </button>
              <button
                type="button"
                className="mileage-button down"
                onClick={() => handleMileageAdjust(-100)}
                aria-label="Verringere um 100 km"
              >
                ▼
              </button>
            </div>
          </div>
          {submitted && errors.annualMileage && (
            <span className="error-message">
              Bitte gib einen Wert zwischen 1 und 100.000 km ein
            </span>
          )}
        </div>
      </div>

      <button type="submit" className="submit-button">
        Prämie Berechnen
      </button>

      {loading && (
        <div className="premium-display loading">
          <div className="spinner"></div>
          <p>Berechne Ihre Prämie...</p>
        </div>
      )}
      {error && !loading && (
        <div className="premium-display error">
          <div className="error-icon">⚠️</div>
          <h3>Ups! Etwas ist schiefgelaufen</h3>
          <p>{error}</p>
        </div>
      )}
      {result && !loading && (
        <div className="premium-display success">
          <div className="premium-header">
            <h2>Ihre Jahresprämie</h2>
          </div>
          <div className="premium-amount">{formatCurrency(result.annualPremium)}</div>
        </div>
      )}
    </form>
  );
}

function validatePostalCode(postalCode: string): boolean {
  return /^\d{5}$/.test(postalCode);
}

function validateAnnualMileage(mileage: string): boolean {
  const num = parseInt(mileage.replace(/\./g, ""), 10);
  return !isNaN(num) && num > 0 && num <= 100000;
}

function roundToNearest100(value: number): number {
  return Math.ceil(value / 100) * 100;
}

function formatMileage(value: number): string {
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " km";
}

function parseMileage(formattedMileage: string): number {
  return parseInt(formattedMileage.replace(/\./g, ""), 10);
}

const currencyFormat = new Intl.NumberFormat("de-DE", {
  style: "currency",
  currency: "EUR",
  minimumFractionDigits: 2,
});

function formatCurrency(amount: number): string {
  return currencyFormat.format(amount);
}
