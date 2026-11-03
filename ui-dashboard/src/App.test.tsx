import React from "react";
import { render, screen } from "@testing-library/react";
import { test, expect } from "vitest";
import "@testing-library/jest-dom/vitest";
import App from "./App";

test("renders fintech title", () => {
  render(<App />);
  const title = screen.getByText(/Fintech FIAP 2026/i);
  expect(title).toBeInTheDocument();
});