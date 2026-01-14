
"use client";

import { useState } from 'react';
import Link from 'next/link';
import {
  FaceSmileIcon,
  FaceFrownIcon,
  ChatBubbleLeftRightIcon,
  ChatBubbleBottomCenterTextIcon,
  ExclamationCircleIcon
} from '@heroicons/react/24/outline';
//coockies
import { useUser } from '@/context/UserContext';


interface SentimentResult1 {
  comentario: string;
  prevision: 'POSITIVO' | 'NEGATIVO';
  provabilidad: number;
}



export default function StatisticsPage() {

  const { user } = useUser();

  const token = user?.token;
  // const token = await cookieStore.get("session_token");
  const [comment, setComment] = useState('');
  const [results, setResults] = useState<SentimentResult1[]>([]);
  const [midata, setMidata] = useState<{ total_en_pagina: number; positivos: number; negativos: number } | null>(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

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
  // console.log("tokennnn  ", token)

  const handleAnalyze = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    // 1. Convertimos el comentario único en el formato de lista que espera tu API
    // Formato: [ { "texto": "valor" } ]
    const dataToAnalyze = [
      { texto: comment }
    ];
    const nunComent = parseInt(comment);
    console.log("numero de comentarios ", nunComent)

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/stats?size=${nunComent}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}` // Asegúrate de obtener tu token
        },
        // body: JSON.stringify(payload),
      });
      // console.log("payload:: ", payload);

      const data = await response.json();
      console.log("data  :::  ", data.total_en_pagina)
       console.log("data 2 :::  ", data)
      setResults(data.content);
      setMidata(data);
    } catch (err: any) {
      // Si el error es de conexión (Servidor apagado)
      if (err.message === "Failed to fetch") {
        setMessage("No se pudo conectar con el servidor. Verifica tu conexión.");
      } else {
        setMessage(err.message);
      }
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



      <section className="flex flex-col md:flex-row gap-8 bg-white rounded-2xl shadow-sm p-8 border border-gray-100">

        {/* SECCIÓN 1: Formulario (Máximo 30% de ancho) */}
        <div className="w-full md:w-[30%] border-r border-gray-100 pr-4">
          <h3 className="text-gray-500 text-sm font-semibold mb-4 uppercase tracking-wider">Configuración</h3>
          <form onSubmit={handleAnalyze} className="space-y-4">
            <input
              type="number"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="Cantidad de comentarios..."
              className="w-full p-4 rounded-xl bg-gray-50 border-none focus:ring-2 focus:ring-primary outline-none text-gray-700 transition-all shadow-inner"
            />
            <button
              disabled={loading}
              className="w-full bg-linear-to-r from-primary to-three text-white px-6 py-3 rounded-xl
               font-bold hover:shadow-lg hover:shadow-primary/30 transition-all active:scale-95 disabled:opacity-50"
            >
              {loading ? "Procesando..." : "Analizar"}
            </button>
          </form>


          {message && (
            <div className={`mt-6 p-4 rounded-xl flex items-center border `}>
              <span className="text-sm font-semibold">{message}</span>
            </div>
          )}
        </div>

        {/* SECCIÓN 2: Estadísticas (Resto del ancho dividido en 3) */}
        <div className="flex-1 grid grid-cols-1 sm:grid-cols-3 gap-6 items-center">

          {/* Card: Total Comentarios */}
          <div className="flex flex-col items-center p-4 rounded-2xl bg-blue-50/50 border border-blue-100">
            <div className="p-3 bg-blue-100 rounded-full mb-3 text-blue-600">
              <ChatBubbleBottomCenterTextIcon className="w-6 h-6" />
            </div>
            <span className="text-gray-500 text-xs font-medium uppercase">Total Comentarios</span>
            <p className="text-3xl font-bold text-gray-800">{midata?.total_en_pagina ?? 0}</p>
          </div>

          {/* Card: Positivos */}
          <div className="flex flex-col items-center p-4 rounded-2xl bg-green-50/50 border border-green-100">
            <div className="p-3 bg-green-100 rounded-full mb-3 text-green-600">
              <FaceSmileIcon className="w-6 h-6" />
            </div>
            <span className="text-gray-500 text-xs font-medium uppercase">Positivos</span>
            <p className="text-3xl font-bold text-green-700">{midata?.positivos}</p>
          </div>

          {/* Card: Negativos */}
          <div className="flex flex-col items-center p-4 rounded-2xl bg-red-50/50 border border-red-100">
            <div className="p-3 bg-red-100 rounded-full mb-3 text-red-600">
              <FaceFrownIcon className="w-6 h-6" />
            </div>
            <span className="text-gray-500 text-xs font-medium uppercase">Negativos</span>
            <p className="text-3xl font-bold text-red-700">{midata?.negativos}</p>
          </div>

        </div>
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
                  <th className="px-6 py-4 font-semibold text-center">Prevision</th>
                  <th className="px-6 py-4 font-semibold text-center">Probabilidad</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {results.map((res, index) => (
                  <tr key={index} className="hover:bg-gray-50/80 transition-colors">
                    <td className="px-6 py-4 text-gray-700 max-w-md truncate">
                      {res.comentario}
                    </td>
                    <td className="px-6 py-4 text-center">
                      <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-bold uppercase ${res.prevision === 'POSITIVO'
                        ? 'bg-green-100 text-green-700'
                        : 'bg-red-100 text-red-700'
                        }`}>
                        {res.prevision === 'POSITIVO' ? <FaceSmileIcon className="h-4 w-4 mr-1" /> : <FaceFrownIcon className="h-4 w-4 mr-1" />}
                        {res.prevision}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-center">
                      <div className="flex flex-col items-center">
                        <span className="text-sm font-mono font-bold text-gray-600">
                          {(res.provabilidad * 100).toFixed(1)}%
                        </span>
                        {/* Barra de progreso miniatura */}
                        <div className="w-16 h-1.5 bg-gray-200 rounded-full mt-1 overflow-hidden">
                          <div
                            className={`h-full rounded-full ${res.prevision === 'POSITIVO' ? 'bg-green-500' : 'bg-red-500'}`}
                            style={{ width: `${res.provabilidad * 100}%` }}
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