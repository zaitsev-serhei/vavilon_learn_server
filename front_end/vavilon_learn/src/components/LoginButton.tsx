import { Link } from "react-router-dom";

export function LoginButton() {
  return (
    <Link
      to="/login"
      className="ml-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
    >
      Login
    </Link>
  );
}
