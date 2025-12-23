import React from "react";

import { NavigationBar } from "./NavigationBar";

export default function LandingNav() {
  return (
    <div className="w-full h-10 shadow-blue-400 shadow-lg bg-babylon-blue ">
      <div className="w-full flex items-start justify-between">
        <div className=" md:hidden bg-babylon-gold px-1 h-10 flex items-center ">
          IMG LOGO
        </div>
        <NavigationBar />
      </div>
    </div>
  );
}
