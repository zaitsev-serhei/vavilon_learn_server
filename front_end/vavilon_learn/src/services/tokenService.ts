import { useAuthStore } from "../states/auth";

let refreshTimeoutId: number | null = null;

const T_BUFFER = 60; //seconds

function parseJwt(token: string) {
  try {
    const [, payload] = token.split(".");
    const json = atob(payload.replace(/-/g, "+").replace(/_/g, "/"));
    return JSON.parse(json);
  } catch {
    return null;
  }
}

export const scheduleTokenRefresh = (token: string | null) => {
  console.log("Scheduling token refresh for token:", token);
  if (refreshTimeoutId) {
    window.clearTimeout(refreshTimeoutId);
    refreshTimeoutId = null;
  }
  if (!token) return;

  const payload = parseJwt(token);
  if (!payload || !payload.exp) return;
  const expMs = payload.exp * 1000;
  const nowMs = Date.now();
  const bufferMs = T_BUFFER * 1000;
  const msUntilRefresh = Math.max(0, expMs - nowMs - bufferMs);
  if (msUntilRefresh <= 0) {
    const handler = useAuthStore.getState().refreshHandler;
    if (typeof handler === "function") {
      // Token is expiring too soon or already expired, refresh immediately
      handler().catch(() => {});
    }
    return;
  }

  refreshTimeoutId = window.setTimeout(async () => {
    const handler = useAuthStore.getState().refreshHandler;
    if (typeof handler === "function") {
      handler().catch(() => {});
    }
  }, msUntilRefresh);
};

export const clearScheduledRefresh = () => {
  if (refreshTimeoutId) {
    window.clearTimeout(refreshTimeoutId);
    refreshTimeoutId = null;
  }
};
