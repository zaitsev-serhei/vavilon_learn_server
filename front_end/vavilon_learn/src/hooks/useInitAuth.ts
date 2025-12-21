import { useEffect } from "react";
import authService from "../services/authService";
import { useAuthStore } from "../states/auth";

export const useInitAuth = () => {
  const accessToken = useAuthStore((state) => state.accessToken);
  useEffect(() => {
    let mounted = true;
    const init = async () => {
      if (accessToken) {
        authService.refreshInternal();
        return;
      }
      try {
        await authService.me();
      } catch {}
    };

    init();
    return () => {
      mounted = false;
    };
  }, []);
};
