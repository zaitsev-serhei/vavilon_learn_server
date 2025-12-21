import React from "react";
import { Routes, Route } from "react-router-dom";
import { useInitAuth } from "./hooks/useInitAuth";
import Login from "./components/Login";
import "./App.css";
import LandingPage from "./pages/LandingPage";
import { NavigationBar } from "./components/NavigationBar";
import OAuthCallback from "./components/OAuthCallback";
import HomePage from "./pages/Home";

function App() {
  // Initialize authentication state
  useInitAuth;
  return (
    <Routes>
      <Route
        path="/"
        element={
          <div>
            <NavigationBar />
            <LandingPage />
          </div>
        }
      />
      <Route path="/login" element={<Login />} />
      <Route path="/oauth2/callback/google" element={<OAuthCallback />} />
      {/* Після логіну */}
      <Route path="/home" element={<HomePage />} />
    </Routes>
  );
}

export default App;
