import React from "react";
import Logo from "./Logo";
import { useNavigate } from "react-router-dom";

export function NavigationBar() {
  const navigate = useNavigate();
  const scrollToSection = (id: string) => {
    const el = document.getElementById(id);
    if (!el) {
      return;
    }
    const targetY = el.getBoundingClientRect().top + window.pageYOffset;

    smoothScrollTo(targetY, 1200);
  };

  const smoothScrollTo = (targetY: number, duration = 800) => {
    const startY = window.scrollY;
    const diff = targetY - startY;
    let startTime: number | null = null;

    const easeInOutCubic = (t: number) =>
      t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;

    const loop = (now: number) => {
      if (!startTime) startTime = now;
      const time = now - startTime;
      const progress = Math.min(time / duration, 1);
      const eased = easeInOutCubic(progress);

      window.scrollTo(0, startY + diff * eased);

      if (time < duration) requestAnimationFrame(loop);
    };

    requestAnimationFrame(loop);
  };
  return (
    <nav className="bg-white/90 dark:bg-babylon-blue/90 sticky top-0 z-50 backdrop-blur-md border-b border-gray-200 dark:border-neutral-800">
      <div className="mx-auto px-6 py-4 max-w-10xl">
        <div className="items-center justify-between flex">
          <div className="items-center flex space-x-3">
            <Logo className="w-14 h-14" />
            <span className="text-xl font-bold text-gray-900 dark:text-white">
              Vavilon Learn
            </span>
          </div>
          <div className="md:flex items-center space-x-8">
            <button
              onClick={() => scrollToSection("info")}
              className="text-gray-700 dark:text-neutral-300 hover:text-blue-600 dark:hover:text-blue-400 transition-colors"
            >
              Info
            </button>
            <button
              onClick={() => scrollToSection("info")}
              className="text-gray-700 dark:text-neutral-300 hover:text-blue-600 dark:hover:text-blue-400 transition-colors"
            >
              Usage
            </button>
            <button
              onClick={() => scrollToSection("info")}
              className="text-gray-700 dark:text-neutral-300 hover:text-blue-600 dark:hover:text-blue-400 transition-colors"
            >
              Examples
            </button>
            <button
              onClick={() => scrollToSection("info")}
              className="text-gray-700 dark:text-neutral-300 hover:text-blue-600 dark:hover:text-blue-400 transition-colors"
            >
              Feedback
            </button>
            <button
              onClick={() => navigate("/login")}
              className="hover:bg-blue-700 dark:hover:bg-blue-600 transition-all px-5 py-2 bg-blue-600 dark:bg-blue-500 text-white rounded-lg"
            >
              Get Started
            </button>
          </div>
          <button
            type="submit"
            className="dark:text-neutral-300 md:hidden text-gray-700"
          >
            <svg
              className="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              id="Windframe_pLnDlWmpj"
            ></svg>
          </button>
        </div>
      </div>
    </nav>
  );
}
