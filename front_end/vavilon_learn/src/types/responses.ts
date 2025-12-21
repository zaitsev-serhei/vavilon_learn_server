import type { User } from "./global";

export interface LoginResponse {
  accessToken: string;
  user: User;
}

export interface RefreshResponse {
  accessToken: string;
}

export interface MeResponse {
  user: User;
  accessToken?: string;
}
