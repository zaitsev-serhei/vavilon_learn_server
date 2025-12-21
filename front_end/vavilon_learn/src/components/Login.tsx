// src/pages/Login.tsx
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { authService } from "../services/authService";

export default function Login() {
  const navigate = useNavigate();

  useEffect(() => {
    // Очистка слухача при анмаунті
    return () => {
      window.removeEventListener("message", handleMessage);
    };
  }, []);

  // Обробник повідомлень з popup (від oauth callback page)
  const handleMessage = async (ev: MessageEvent) => {
    // Перевірка origin (опціонально, для безпеки вкажи свій домен)
    // if (ev.origin !== window.location.origin) return

    const { type, success, error } = ev.data || {};
    if (type === "oauth" && success) {
      // отримаємо профіль із бекенду
      try {
        await authService.me(); // встановить user + accessToken у store
        navigate("/home");
      } catch (e) {
        console.error("me() after oauth failed", e);
      }
    } else if (type === "oauth" && !success) {
      alert(error || "OAuth login failed");
    }
  };

  // викликаємо перед відкриттям попапа щоб слухати повідомлення
  const openOAuthPopup = (provider = "google") => {
    window.addEventListener("message", handleMessage);

    // Підстав: backend endpoint який ініціює авторизацію (редірект до Google)
    // наприклад: /api/oauth2/authorize/google
    // опціонально додати state чи redirect param
    const redirectUri = window.location.origin + `/oauth2/callback/google`;
    const authUrl = `/api/oauth2/authorize/google?redirectUrl=${encodeURIComponent(redirectUri)}`;

    window.location.href = authUrl;
  };

  return (
    <div className="flex justify-center items-center h-screen bg-gray-100">
      <div className="bg-white p-8 rounded shadow-md w-96">
        <h1 className="text-2xl font-bold mb-6">Sign in</h1>
        <button
          onClick={() => openOAuthPopup("google")}
          className="w-full flex items-center gap-3 justify-center px-4 py-2 bg-white border rounded hover:shadow"
        >
          <img src="/google-icon.svg" alt="Google" className="w-5 h-5" />
          Continue with Google
        </button>
      </div>
    </div>
  );
}
