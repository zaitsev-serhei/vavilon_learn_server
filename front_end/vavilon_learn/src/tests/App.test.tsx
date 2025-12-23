import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import App from "../App";
// Import MemoryRouter to provide routing context in tests
import { MemoryRouter } from "react-router-dom";

test("renders home", () => {
  render(
    <MemoryRouter>
      <App />
    </MemoryRouter>
  );
  expect(screen.getByText(/Welcome/i)).toBeInTheDocument();
});
