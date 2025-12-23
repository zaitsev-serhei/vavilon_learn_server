export function HeroSection() {
  return (
    <section className="bg-gradient-to-br relative isolate overflow-hidden from-amber-50 via-white to-blue-50 dark:from-neutral-900 dark:via-neutral-950 dark:to-blue-950/30">
      <div className="pointer-events-none absolute inset-0 -z-10 overflow-hidden">
        <div className="h-[60vh] w-[60vh] rounded-full bg-gradient-to-br absolute -top-32 -left-32 from-indigo-200 via-lime-200 to-purple-300 opacity-20 blur-2xl dark:opacity-0"></div>
        <div className="h-[40vh] w-[50vh] rounded-full bg-gradient-to-tr absolute -bottom-20 right-10 from-fuchsia-300 via-orange-300 to-rose-200 opacity-40 blur-3xl dark:opacity-0"></div>
        <div className="h-[35vh] w-[45vh] rounded-full bg-gradient-to-b dark:h-[28vh] absolute top-28 left-1/4 from-orange-300 via-amber-200 to-rose-100 opacity-60 blur-3xl dark:from-orange-600 dark:via-amber-500 dark:to-rose-400 dark:opacity-64"></div>
      </div>
      <div className="mx-auto px-6 py-20 md:py-32 max-w-7xl">
        <div className="md:grid-cols-2 items-center grid gap-12">
          <div className="space-y-8 animate-fade-in">
            <div className="px-4 py-2 bg-blue-100 dark:bg-blue-900/30 text-blue-700 rounded-full text-sm font-medium inline-block dark:text-blue-300">
              The Modern Library of Babylon
            </div>
            <p className="text-5xl md:text-6xl lg:text-7xl font-bold text-gray-900 leading-tight dark:text-white">
              Master Any Language
              <span className="mt-2 bg-gradient-to-r text-transparent block from-blue-600 via-amber-500 to-blue-700 dark:from-blue-400 dark:via-amber-400 dark:to-blue-500 bg-clip-text">
                Efficiently
              </span>
            </p>
            <p className="text-lg md:text-xl text-gray-600 leading-relaxed dark:text-neutral-400">
              Where ancient wisdom meets modern technology. Learn, practice, and
              master languages through our intelligent platform combining
              historical knowledge with cutting-edge learning methods.
            </p>
            <div className="sm:flex-row flex flex-col gap-4">
              <button
                type="submit"
                className="hover:bg-blue-700 dark:hover:bg-blue-600 transform hover:scale-105 transition-all hover:shadow-xl px-8 py-4 bg-blue-600 dark:bg-blue-500 text-white rounded-lg font-semibold shadow-lg"
              >
                Start Learning Free
              </button>
              <button
                type="submit"
                className="dark:text-white hover:bg-white dark:hover:bg-neutral-800 border border-gray-300 dark:border-neutral-700 transition-all px-8 py-4 bg-white/80 dark:bg-neutral-800/80 text-gray-900 rounded-lg font-semibold"
              >
                Explore Features
              </button>
            </div>
            <div className="items-center pt-4 flex gap-8">
              <div className="flex -space-x-3">
                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-blue-400 to-blue-600 border-2 border-white dark:border-neutral-950"></div>
                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-amber-400 to-amber-600 border-2 border-white dark:border-neutral-950"></div>
                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-blue-500 to-blue-700 border-2 border-white dark:border-neutral-950"></div>
                <div className="w-10 h-10 rounded-full bg-gradient-to-br from-amber-500 to-amber-700 border-2 border-white dark:border-neutral-950"></div>
              </div>
              <div>
                <p className="text-sm font-semibold text-gray-900 dark:text-white">
                  10,000+ Learners
                </p>
                <p className="text-xs text-gray-600 dark:text-neutral-400">
                  Mastering languages daily
                </p>
              </div>
            </div>
          </div>
          <div className="relative animate-float">
            <svg
              viewBox="0 0 600 600"
              className="w-full h-auto"
              xmlns="http://www.w3.org/2000/svg"
              id="Windframe_rrGM9drHT"
            >
              <defs>
                <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop
                    offset="0%"
                    stopColor="rgb(59,130,246)"
                    stopOpacity={1}
                  />
                  <stop
                    offset="100%"
                    stopColor="rgb(251,191,36)"
                    stopOpacity={1}
                  />
                </linearGradient>

                <linearGradient id="grad2" x1="0%" y1="0%" x2="100%" y2="0%">
                  <stop
                    offset="0%"
                    stopColor="rgb(251,191,36)"
                    stopOpacity={0.8}
                  />
                  <stop
                    offset="100%"
                    stopColor="rgb(245,158,11)"
                    stopOpacity={0.8}
                  />
                </linearGradient>

                <linearGradient id="grad3" x1="0%" y1="100%" x2="100%" y2="0%">
                  <stop
                    offset="0%"
                    stopColor="rgb(37,99,235)"
                    stopOpacity={1}
                  />
                  <stop
                    offset="100%"
                    stopColor="rgb(29,78,216)"
                    stopOpacity={1}
                  />
                </linearGradient>
              </defs>

              <circle
                cx="300"
                cy="300"
                r="200"
                fill="url(#grad1)"
                opacity="0.1"
              ></circle>
              <circle
                cx="300"
                cy="300"
                r="150"
                fill="none"
                stroke="url(#grad1)"
                strokeWidth="2"
                opacity="0.3"
              ></circle>

              <rect
                x="200"
                y="180"
                width="80"
                height="110"
                rx="4"
                fill="url(#grad3)"
              ></rect>
              <rect
                x="205"
                y="185"
                width="70"
                height="15"
                rx="2"
                fill="white"
                opacity="0.9"
              ></rect>
              <rect
                x="205"
                y="205"
                width="70"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>
              <rect
                x="205"
                y="212"
                width="60"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>
              <rect
                x="205"
                y="219"
                width="65"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>
              <rect
                x="205"
                y="226"
                width="50"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>

              <rect
                x="320"
                y="200"
                width="80"
                height="110"
                rx="4"
                fill="url(#grad2)"
              ></rect>
              <rect
                x="325"
                y="205"
                width="70"
                height="15"
                rx="2"
                fill="white"
                opacity="0.9"
              ></rect>
              <rect
                x="325"
                y="225"
                width="70"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>
              <rect
                x="325"
                y="232"
                width="60"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>
              <rect
                x="325"
                y="239"
                width="65"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>
              <rect
                x="325"
                y="246"
                width="55"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>

              <rect
                x="260"
                y="220"
                width="80"
                height="110"
                rx="4"
                fill="url(#grad3)"
              ></rect>
              <rect
                x="265"
                y="225"
                width="70"
                height="15"
                rx="2"
                fill="white"
                opacity="0.9"
              ></rect>
              <rect
                x="265"
                y="245"
                width="70"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>
              <rect
                x="265"
                y="252"
                width="60"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>
              <rect
                x="265"
                y="259"
                width="65"
                height="3"
                rx="1"
                fill="white"
                opacity="0.6"
              ></rect>

              <path
                d="M 150 350 Q 200 320 250 350"
                stroke="url(#grad2)"
                strokeWidth="3"
                fill="none"
                opacity="0.6"
              ></path>
              <path
                d="M 350 350 Q 400 320 450 350"
                stroke="url(#grad1)"
                strokeWidth="3"
                fill="none"
                opacity="0.6"
              ></path>

              <circle
                cx="420"
                cy="250"
                r="30"
                fill="url(#grad2)"
                opacity="0.7"
              ></circle>
              <path
                d="M 410 250 L 418 258 L 435 235"
                stroke="white"
                strokeWidth="3"
                fill="none"
                strokeLinecap="round"
              ></path>

              <rect
                x="140"
                y="400"
                width="120"
                height="70"
                rx="8"
                fill="white"
                opacity="0.9"
              ></rect>
              <rect
                x="150"
                y="410"
                width="100"
                height="8"
                rx="2"
                fill="url(#grad1)"
              ></rect>
              <rect
                x="150"
                y="425"
                width="80"
                height="6"
                rx="2"
                fill="#e5e7eb"
              ></rect>
              <rect
                x="150"
                y="435"
                width="90"
                height="6"
                rx="2"
                fill="#e5e7eb"
              ></rect>
              <rect
                x="150"
                y="445"
                width="70"
                height="6"
                rx="2"
                fill="#e5e7eb"
              ></rect>

              <circle
                cx="380"
                cy="420"
                r="40"
                fill="url(#grad3)"
                opacity="0.8"
              ></circle>
              <path
                d="M 380 400 L 380 440 M 360 420 L 400 420"
                stroke="white"
                strokeWidth="4"
                strokeLinecap="round"
              ></path>

              <polygon
                points="500,150 520,190 480,190"
                fill="url(#grad2)"
                opacity="0.6"
              ></polygon>
              <polygon
                points="100,200 120,240 80,240"
                fill="url(#grad1)"
                opacity="0.6"
              ></polygon>

              <path
                d="M 250 130 L 280 140 L 270 160 L 250 130"
                fill="url(#grad2)"
                opacity="0.5"
              ></path>
              <path
                d="M 280 140 L 310 150 L 270 160 L 280 140"
                fill="url(#grad3)"
                opacity="0.5"
              ></path>
            </svg>
          </div>
        </div>
      </div>
    </section>
  );
}
