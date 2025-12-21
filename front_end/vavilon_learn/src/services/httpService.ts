import axios, {
  type AxiosInstance,
  type AxiosRequestConfig,
  type InternalAxiosRequestConfig,
} from "axios";
import { useAuthStore } from "../states/auth";
import { clearScheduledRefresh, scheduleTokenRefresh } from "./tokenService";
import type { RefreshResponse } from "../types/responses";

type PendingRequest = {
  resolve: (value?: unknown) => void;
  reject: (reason?: any) => void;
  config: AxiosRequestConfig;
};

// Create an Axios instance with default configuration
const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "/api",
  withCredentials: true,
});

//instance to perfoem refresh token request without interceptors
export const rawAxios = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "/api",
  withCredentials: true,
});

//add Authorization header to each request if access token is available
api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = useAuthStore.getState().accessToken;
  if (token) {
    config.headers = config.headers ?? {};
    (config.headers as Record<string, string>)["Authorization"] =
      `Bearer ${token}`;
  }
  return config;
});

let isRefreshing = false;

let queue: {
  resolve: (value?: unknown) => void;
  reject: (reason?: any) => void;
  config: AxiosRequestConfig;
}[] = [];

// Function to process all pending requests after token refresh
const processQueue = (error: any, token: string | null = null) => {
  queue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.config.headers = prom.config.headers ?? {};
      (prom.config.headers as Record<string, string>)["Authorization"] =
        `Bearer ${token}`;
      prom.resolve(api(prom.config));
    }
  });
  queue = [];
};

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config as AxiosRequestConfig & {
      _retry?: boolean;
    };
    if (!originalRequest) return Promise.reject(error);

    const status = error.response ? error.response.status : null;
    // If status is 401, attempt to refresh the token
    if (error.response && status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          queue.push({ resolve, reject, config: originalRequest });
        });
      }
      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const refreshHandler = useAuthStore.getState().refreshHandler;
        if (typeof refreshHandler !== "function") {
          useAuthStore.getState().logout();
          clearScheduledRefresh();
          isRefreshing = false;
          return Promise.reject(error);
        }
        const refreshResult =
          (await refreshHandler()) as RefreshResponse | null;
        const newAccessToken =
          refreshResult?.accessToken ??
          useAuthStore.getState().accessToken ??
          null;
        // If no new access token is received, logout the user
        if (!newAccessToken) {
          // refresh failed or no token -> logout
          useAuthStore.getState().logout();
          clearScheduledRefresh();
          processQueue(new Error("Refresh failed"), null);
          isRefreshing = false;
          return Promise.reject(error);
        }
        // If a new access token is received, update the store and retry the original request
        useAuthStore.getState().setAccessToken(newAccessToken);
        scheduleTokenRefresh(newAccessToken);
        isRefreshing = false;
        // Retry the original request with the new token
        originalRequest.headers = originalRequest.headers ?? {};
        (originalRequest.headers as Record<string, string>)["Authorization"] =
          `Bearer ${newAccessToken}`;

        processQueue(null, newAccessToken);

        return api(originalRequest);
      } catch (refreshError) {
        // If refresh fails, logout the user
        isRefreshing = false;
        processQueue(refreshError, null);
        // виконуємо logout, якщо refresh не вдався
        useAuthStore.getState().logout();
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default api;
