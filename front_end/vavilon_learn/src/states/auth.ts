import { create } from "zustand";
import type { User } from "../types/global";
import type { RefreshResponse } from "../types/responses";

type RefreshHandler = (() => Promise<RefreshResponse | null>) | null;

interface AuthState {
  user: User | null;
  accessToken: string | null;
  setUser: (user: User | null) => void;
  setAccessToken: (token: string | null) => void;
  logout: () => void;
  refreshHandler: RefreshHandler;
  setRefreshHandler: (fn: RefreshHandler) => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  accessToken: null,
  setUser: (user) => set({ user }),
  setAccessToken: (token) => set({ accessToken: token }),
  logout: () => set({ user: null, accessToken: null }),
  refreshHandler: null,
  setRefreshHandler: (fn) => set({ refreshHandler: fn }),
}));
