// /home/richard/Escritorio/Proyectos_practica/frontend/next.js/sentiment-api/app/page.tsx

import Link from "next/link";

export default function Home() {
  return (
    <div className="min-h-screen bg-[var(--color-primary)] text-[var(--color-white)] flex flex-col items-center justify-center">
      <header className="text-center mb-8">
        <h1 className="text-4xl font-bold">Welcome to Sentiment IA</h1>
        <p className="text-lg mt-2 text-[var(--color-secondary)]">
          Discover amazing features and join us today!
        </p>
      </header>
      <main>
      <Link href={`/login`}>
              <button
          className="px-6 py-3 bg-[var(--color-accent)] text-[var(--color-white)] rounded-lg text-lg font-medium hover:bg-[var(--color-five)] transition"
         
        >  
          Go to Login
        </button>
      </Link>

      </main>
    </div>
  );
}