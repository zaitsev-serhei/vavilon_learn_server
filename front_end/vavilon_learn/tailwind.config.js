/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        'babylon-blue': '#0f4c81',
        'babylon-gold': '#d4af37'
      }
    },
  },
  plugins: [],
}

