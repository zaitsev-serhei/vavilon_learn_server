import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../states/auth";

export default function OAuthCallback() {
  const navigate = useNavigate();

  useEffect(() => {
    const finish = async () => {
      try {
        // Виконуємо запит на бекенд, щоб отримати user + accessToken
        const resp = await fetch("/api/auth/me", {
          method: "GET",
          credentials: "include", // <- Важливо! відправляє httpOnly cookie
          headers: {
            Accept: "application/json",
          },
        });

        if (!resp.ok) {
          // Якщо 401 — показати помилку або перенаправити на логін
          console.error("me() failed, status:", resp.status);
          // Якщо popup: window.opener?.postMessage({ type: 'oauth', success: false }, window.location.origin);
          navigate("/login");
          return;
        }

        const body = await resp.json(); // очікуємо AuthResponse { user: {...}, accessToken: "..." }
        const { user, accessToken } = body;
        console.log("OAuthCallback received user:", body);
        useAuthStore.getState().setUser(user);
        useAuthStore.getState().setAccessToken(accessToken);
        console.log("OAuth login successful, user:", user);
        console.log("Access Token:", accessToken);
        navigate("/home");
      } catch (e) {
        console.error("OAuth callback error", e);
        navigate("/login");
      }
    };

    finish();
  }, [navigate]);

  return <div>Completing authentication...</div>;
}
