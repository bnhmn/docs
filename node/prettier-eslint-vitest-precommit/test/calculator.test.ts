import { describe, expect, it } from "vitest";
import { calculateSum } from "../src/calculator";

describe("Calculator", () => {
  it("should compute the correct sum of 2 + 3", async () => {
    const result = calculateSum(2, 3);
    expect(result).toBe(5);
  });
});
