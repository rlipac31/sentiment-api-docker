"use client";

import { useState } from 'react';

import Link from 'next/link';
import { FaceSmileIcon, FaceFrownIcon,
  ExclamationCircleIcon, ChatBubbleLeftRightIcon } from '@heroicons/react/24/outline';
//coockies
import { useUser } from '../../context/UserContext';


interface SentimentAnalysisResult {
  texto: string;
  prevision: 'positivo' | 'negativo';
  probabilidad: number;
}

export default  function DashboardPage() {

  const { user }= useUser();
  
  const token = user?.token;


  if (!token) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[400px] bg-white rounded-2xl border border-dashed border-red-200 p-8 shadow-sm">
        <ExclamationCircleIcon className="h-12 w-12 text-red-400 mb-4" />
        <h2 className="text-xl font-bold text-gray-800">No autenticado</h2>
        <p className="text-gray-500 text-center mt-2">
          Tu sesión ha expirado o no has iniciado sesión. Por favor, vuelve a ingresar.
        </p>
        <Link href={`/login`}>

         <button
          // O usa router.push de Next.js
          className="mt-6 px-6 py-2 bg-primary text-white rounded-lg font-bold hover:bg-three transition-colors"
        >
          Ir al Login
        </button>

        </Link>
  
      </div>
    );
  }

 // const token = await cookieStore.get("session_token");
  const [comment, setComment] = useState('');
  const [results, setResults] = useState<SentimentAnalysisResult[]>([]);
  const [loading, setLoading] = useState(false);
 /// console.log("tokennnn  ", token)

  const handleAnalyze = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

  if (!token) {
     return { error: "No estás autenticado. Por favor inicia sesión." };
    }
  // 1. Convertimos el comentario único en el formato de lista que espera tu API
  // Formato: [ { "texto": "valor" } ]
  const dataToAnalyze = [
    { texto: comment } 
  ];

    // Convertimos el input en el formato [ { "texto": "..." } ]
 /*    const payload = comment.split('\n')
      .filter(line => line.trim() !== "")
      .map(line => ({ texto: line })); */

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/sentiment`, {
        method: "POST",
        headers: { 
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}` // Asegúrate de obtener tu token
        },
        body: JSON.stringify(dataToAnalyze),
      });
      console.log("payload:: ", dataToAnalyze);

      const data = await response.json();
      setResults(data);
      console.log("data:: ", data);
    } catch (error) {
      console.error("Error:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-8">
      <header>
        <h1 className="text-4xl font-extrabold text-gray-800">Panel de Análisis</h1>
        <p className="text-gray-500">Procesa comentarios individuales o masivos.</p>
      </header>

      {/* Sección de Input */}
      <section className="bg-white rounded-2xl shadow-sm p-6 border border-gray-100">
        <form onSubmit={handleAnalyze} className="space-y-4">
          <input
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="Escribe comentarios (uno por línea para análisis masivo)..."
            className="w-full  p-4 rounded-xl bg-gray-50 border-none focus:ring-2 focus:ring-primary outline-none text-gray-700 transition-all"
          />
          <button
            disabled={loading}
            className="bg-linear-to-r from-primary to-three text-white px-6 py-3 rounded-xl font-bold hover:shadow-lg hover:shadow-primary/30 transition-all active:scale-95 disabled:opacity-50"
          >
            {loading ? "Procesando..." : "Analizar Sentimientos"}
          </button>
        </form>
      </section>

      {/* Tabla de Resultados */}
      {results.length > 0 && (
        <section className="bg-white rounded-2xl shadow-xl overflow-hidden border border-gray-100 animate-in fade-in slide-in-from-bottom-4 duration-500">
          <div className="p-6 border-b border-gray-50 bg-gray-50/50">
            <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
              <ChatBubbleLeftRightIcon className="h-6 w-6 text-primary" />
              Resultados del Modelo
            </h2>
          </div>
          
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-gray-50 text-gray-500 text-sm uppercase tracking-wider">
                  <th className="px-6 py-4 font-semibold">Comentario</th>
                  <th className="px-6 py-4 font-semibold text-center">Sentimiento</th>
                  <th className="px-6 py-4 font-semibold text-center">Confianza</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {results.map((res, index) => (
                  <tr key={index} className="hover:bg-gray-50/80 transition-colors">
                    <td className="px-6 py-4 text-gray-700 max-w-md truncate">
                      {res.texto}
                    </td>
                    <td className="px-6 py-4 text-center">
                      <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-bold uppercase ${res.prevision === 'positivo'
                          ? 'bg-green-100 text-green-700'
                          : 'bg-red-100 text-red-700'
                        }`}>
                        {res.prevision === 'positivo' ? <FaceSmileIcon className="h-4 w-4 mr-1" /> : <FaceFrownIcon className="h-4 w-4 mr-1" />}
                        {res.prevision}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-center">
                      <div className="flex flex-col items-center">
                        <span className="text-sm font-mono font-bold text-gray-600">
                          {(res.probabilidad * 100).toFixed(1)}%
                        </span>
                        {/* Barra de progreso miniatura */}
                        <div className="w-16 h-1.5 bg-gray-200 rounded-full mt-1 overflow-hidden">
                          <div 
                            className={`h-full rounded-full ${res.prevision === 'positivo' ? 'bg-green-500' : 'bg-red-500'}`}
                            style={{ width: `${res.probabilidad * 100}%` }}
                          />
                        </div>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      )}
    </div>
  );
}