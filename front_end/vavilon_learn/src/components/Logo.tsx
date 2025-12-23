import React from "react";
import logo50 from "../assets/vavilonLearn_logo_1_50.png";
import logo100 from "../assets/vavilonLearn_logo_1_100.png";
import logo200 from "../assets/vavilonLearn_logo_1_200.png";

export default function Logo({
  className = "w-12 h-12",
}: {
  className?: string;
}) {
  return (
    <picture>
      <source
        type="image/png"
        srcSet={`${logo50} 50w, ${logo100} 100w, ${logo200} 200w`}
        sizes="(max-width: 640px) 50px, 100px"
      />
      <img
        src={logo100}
        alt="LearningPlatform logo"
        width="100"
        height="100"
        className={className}
      />
    </picture>
  );
}
