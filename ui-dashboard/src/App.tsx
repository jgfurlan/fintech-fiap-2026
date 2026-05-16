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
    <div className="App flex flex-col items-center justify-center p-4 md:p-8">
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
        <div className="text-center flex flex-col items-center w-full max-w-sm">
          <p className="text-text mb-2">Logged in as {session.user.email}</p>
          <button
            onClick={handleLogout}
            className="bg-love text-base px-4 py-2 rounded hover:opacity-80 mb-8 min-h-[44px] focus:outline-none focus:ring-2 focus:ring-love focus:ring-offset-2 focus:ring-offset-surface"
            aria-label="Logout"
          >
            Logout
          </button>

          {/* Anticipatory Banking Widget */}
          <div className="p-6 bg-surface border border-highlight-med rounded-lg w-full text-left shadow-xl">
            <div className="flex items-center gap-2 mb-4">
              <span role="img" aria-label="AI Sparkles">✨</span>
              <h2 className="text-gold font-bold uppercase tracking-wider text-xs">AI Agentic Insight</h2>
            </div>
            <p className="text-text mb-6 leading-relaxed">
              You are trending <span className="text-gold font-bold">15% over</span> your dining budget this month. 
              Based on your current spending velocity, we suggest adjusting your weekend entertainment allocation to stay on track.
            </p>
            <button
              className="w-full bg-foam text-surface font-bold py-3 px-4 rounded-md min-h-[44px] hover:opacity-90 transition-opacity shadow-sm focus:outline-none focus:ring-2 focus:ring-foam focus:ring-offset-2 focus:ring-offset-surface"
              aria-label="Adjust your budget based on AI suggestion"
              onClick={() => alert("Anticipatory flow initiated: Recalculating budget... (Coming soon)")}
            >
              Adjust Budget Plan
            </button>
          </div>
        </div>
      ) : (
        <form
          onSubmit={handleLogin}
          className="flex flex-col gap-3 w-full max-w-sm"
        >
          <input
            className="bg-surface text-text border border-highlight-med rounded px-3 py-2 placeholder:text-muted min-h-[44px] focus:outline-none focus:ring-2 focus:ring-pine focus:ring-offset-2 focus:ring-offset-surface"
            placeholder="Email"
            aria-label="Email Address"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            className="bg-surface text-text border border-highlight-med rounded px-3 py-2 placeholder:text-muted min-h-[44px] focus:outline-none focus:ring-2 focus:ring-pine focus:ring-offset-2 focus:ring-offset-surface"
            type="password"
            placeholder="Password"
            aria-label="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button
            type="submit"
            className="bg-foam text-base px-4 py-2 rounded hover:opacity-80 min-h-[44px] focus:outline-none focus:ring-2 focus:ring-foam focus:ring-offset-2 focus:ring-offset-surface"
          >
            Sign In
          </button>
        </form>
      )}
    </div>
  );
}

export default App;