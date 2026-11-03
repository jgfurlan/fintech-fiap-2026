/// <reference types="vitest/config" />
import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');

  return {
    plugins: [react()],
    // Support REACT_APP_ prefix alongside standard VITE_
    envPrefix: ['VITE_', 'REACT_APP_'],
    define: {
      'process.env.REACT_APP_SUPABASE_URL': JSON.stringify(
        env.REACT_APP_SUPABASE_URL || env.VITE_SUPABASE_URL || ''
      ),
      'process.env.REACT_APP_SUPABASE_ANON_KEY': JSON.stringify(
        env.REACT_APP_SUPABASE_ANON_KEY || env.VITE_SUPABASE_ANON_KEY || ''
      ),
      'process.env.REACT_APP_API_URL': JSON.stringify(
        env.REACT_APP_API_URL || env.VITE_API_URL || ''
      ),
      'process.env': JSON.stringify(env),
    },
    build: {
      outDir: 'build',
      emptyOutDir: true,
    },
    server: {
      port: 3000,
      proxy: {
        '/api': {
          target: env.REACT_APP_API_URL || env.VITE_API_URL || 'http://localhost:8080',
          changeOrigin: true,
        },
      },
    },
    test: {
      globals: true,
      environment: 'jsdom',
      setupFiles: './src/setupTests.ts',
    },
  };
});
