import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home.jsx";

// Routes will grow in Phase 5: /login, /dashboard (role-gated),
// /audit-log (admin only), each wrapped in a route guard that checks
// the decoded JWT's role claim.
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
