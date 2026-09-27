import { render, screen } from "@testing-library/react";
import { userEvent } from "@testing-library/user-event";
import { describe, expect, it } from "vitest";
import App from "../src/App.tsx";

describe("App", () => {
  it("should render the app", async () => {
    const user = userEvent.setup();
    render(<App />);

    await user.click(screen.getByRole("button"));

    expect(screen.queryAllByRole("heading")[0]).toHaveTextContent(
      "Get started",
    );
  });
});
