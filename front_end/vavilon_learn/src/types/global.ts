export type Role = "TEACHER" | "STUDENT" | "ADMIN" | null;

export type TemplateStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";

export type TemplateVisibility = "PUBLIC" | "PRIVATE" | "UNLISTED";

export type TaskDifficulty = "EASY" | "MEDIUM" | "HARD";

export type AssignmentStatus =
  | "PENDING"
  | "IN_PROGRESS"
  | "SUBMITTED"
  | "GRADED"
  | "OVERDUE";

export type BlockType =
  | "TEXT"
  | "MULTIPLE_CHOICE"
  | "FILL_IN_THE_BLANK"
  | "QUIZ";

export interface User {
  id: number;
  fullName?: string;
  email: string;
  role: Role;
  avatarUrl?: string;
  createdAt?: string;
  updatedAt?: string;
}
export interface AccessTokenPayload {
  sub: string;
  userId: number;
  role: Role;
  iat: number;
  exp: number;
}

export interface Template {
  id: number;
  title: string;
  description?: string;
  visibility: TemplateVisibility;
  status: TemplateStatus;
  difficulty: TaskDifficulty;
  authorId: number;
  blocks: TemplateBlock[];
  createdAt: string;
  updatedAt: string;
  stats?: {
    views: number;
    completions: number;
    averageScore: number;
  };
}
export interface TemplateBlock {
  id: string;
  templateId: number;
  type: BlockType;
  content: string;
  order: number;
  metadata?: Record<string, any>;
  correctAnswers?: string | string[] | null;
}

export interface Assignment {
  id: number;
  templateId: number;
  studentId: number;
  status: AssignmentStatus;
  assignedAt: string;
  dueAt?: string;
  submittedAt?: string;
  gradedAt?: string;
  score?: number;
  results?: Record<string, any>;
  studentAnswers?: Record<string, any>;
  createdAt: string;
  updatedAt: string;
}

export interface APIResponse<T = any> {
  data: T;
  message: string;
  error?: string;
}
