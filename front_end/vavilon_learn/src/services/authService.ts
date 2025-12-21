import api from "./httpService";
import { rawAxios } from "./httpService";
import { useAuthStore } from "../states/auth";
import type { User } from "../types/global";
import { scheduleTokenRefresh, clearScheduledRefresh } from "./tokenService";
import {
  type LoginResponse,
  type MeResponse,
  type RefreshResponse,
} from "../types/responses";

export const authService = {
  login: async (email: string, password: string): Promise<User> => {
    const response = await api.post<LoginResponse>("/auth/login", {
      email,
      password,
    });
    const { user, accessToken } = response.data;
    if (accessToken) {
      useAuthStore.getState().setAccessToken(accessToken);
      scheduleTokenRefresh(accessToken);
    }
    if (user) {
      useAuthStore.getState().setUser(user);
    } else {
      try {
        const userResponse = await api.get<MeResponse>("/auth/me");
        useAuthStore.getState().setUser(userResponse.data?.user);
      } catch {
        useAuthStore.getState().setUser(null);
      }
    }
    return response.data?.user ?? null;
  },

  logout: async (): Promise<void> => {
    try {
      await api.post("/auth/logout");
    } catch {}
    useAuthStore.getState().logout();
    clearScheduledRefresh();
  },

  me: async (): Promise<User | null> => {
    const response = await api.get<MeResponse>("/auth/me");
    const user = response.data?.user ?? null;
    const accessToken: string | undefined = response.data?.accessToken;
    useAuthStore.getState().setUser(user ?? null);
    useAuthStore.getState().setAccessToken(accessToken ?? null);
    if (accessToken) {
      scheduleTokenRefresh(accessToken);
    }
    return user;
  },

  refreshInternal: async (): Promise<RefreshResponse | null> => {
    try {
      const res = await rawAxios.post<RefreshResponse>("/auth/refresh");
      const newAccessToken = res.data?.accessToken ?? null;
      if (newAccessToken) {
        useAuthStore.getState().setAccessToken(newAccessToken);
        scheduleTokenRefresh(newAccessToken);
      } else {
        useAuthStore.getState().logout();
        clearScheduledRefresh;
      }
      return res.data ?? null;
    } catch (e) {
      useAuthStore.getState().logout();
      clearScheduledRefresh();
      return null;
    }
  },
  init: () => {
    useAuthStore
      .getState()
      .setRefreshHandler(() => authService.refreshInternal());
  },
  teardown: () => {
    useAuthStore.getState().setRefreshHandler(null);
  },
};

export default authService;
