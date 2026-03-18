import React from "react"
import ReactDOM from "react-dom/client";
import './styles.css'
import App from './App.tsx'

if (import.meta.env.DEV) {
  const { worker } = await import("./mocks/browser");
  worker.start();
}

async function enableMocking() {
  if (import.meta.env.DEV) {
    const { worker } = await import("./mocks/browser");
    await worker.start();
  }
}

enableMocking().then(() => {
  ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
});