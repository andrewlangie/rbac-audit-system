import axios from "axios";

// Central place for the backend base URL. Once Phase 2 (JWT auth) is in,
// this is also where we'll attach the Authorization header via an axios
// interceptor, rather than repeating it in every request call site.
const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;
