import React, { useEffect, useState } from "react";
import { supabase } from "./lib/supabase";
import { Session } from "@supabase/supabase-js";
import "./App.css";

const API_URL = process.env.REACT_APP_API_URL || "";

function App() {
  const [session, setSession] = useState<Session | null>(null);
  const [apiStatus, setApiStatus] = useState<string>("checking...");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  useEffect(() => {
    supabase.auth.getSession().then(({ data: { session } }) => {
      setSession(session);
    });

    const {
      data: { subscription },
    } = supabase.auth.onAuthStateChange((_event, session) => {
      setSession(session);
    });

    return () => subscription.unsubscribe();
  }, []);

  useEffect(() => {
    const token = session?.access_token;
    const headers: Record<string, string> = {};
    if (token) headers["Authorization"] = `Bearer ${token}`;

    fetch(`${API_URL}/api/health`, { headers })
      .then((res) => res.json())
      .then((data) => setApiStatus(data.status ?? "ok"))
      .catch(() => setApiStatus("unreachable"));
  }, [session]);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    const { error } = await supabase.auth.signInWithPassword({
      email,
      password,
    });
    if (error) alert(error.message);
  };

  const handleLogout = async () => {
    await supabase.auth.signOut();
  };

  return (
    <div className="App flex flex-col items-center justify-center p-8">
      <h1 className="text-4xl font-bold text-pine mb-4">Fintech FIAP 2026</h1>
      <p className="text-subtle mb-8">Tracer Bullet — Phase 1</p>

      <div className="flex gap-2 items-center mb-8">
        <span className="text-muted">API Health:</span>
        <span
          className={`font-mono text-sm px-2 py-0.5 rounded ${
            apiStatus === "ok"
              ? "bg-pine/20 text-pine"
              : apiStatus === "checking..."
                ? "bg-gold/20 text-gold"
                : "bg-love/20 text-love"
          }`}
        >
          {apiStatus}
        </span>
      </div>

      {session ? (
        <div className="text-center">
          <p className="text-text mb-2">Logged in as {session.user.email}</p>
          <button
            onClick={handleLogout}
            className="bg-love text-base px-4 py-2 rounded hover:opacity-80"
          >
            Logout
          </button>
        </div>
      ) : (
        <form
          onSubmit={handleLogin}
          className="flex flex-col gap-3 w-72"
        >
          <input
            className="bg-surface text-text border border-highlight-med rounded px-3 py-2 placeholder:text-muted"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            className="bg-surface text-text border border-highlight-med rounded px-3 py-2 placeholder:text-muted"
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button
            type="submit"
            className="bg-foam text-base px-4 py-2 rounded hover:opacity-80"
          >
            Sign In
          </button>
        </form>
      )}
    </div>
  );
}

export default App;