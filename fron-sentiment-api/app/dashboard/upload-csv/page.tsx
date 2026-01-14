"use client";

import { useState } from 'react';
import Link from 'next/link';
import { 
  CloudArrowUpIcon, 
  CheckCircleIcon, 
  ExclamationCircleIcon, 
  ChatBubbleLeftRightIcon, 
  FaceSmileIcon, 
  FaceFrownIcon,
  ChartBarIcon 
} from '@heroicons/react/24/outline';
import { useUser } from '@/context/UserContext';

// Interfaz para el tipado de los datos del backend
interface AnalisisResultado {
  texto: string;
  prevision: 'positivo' | 'negativo';
  probabilidad: number;
}

export default function UploadCsvPage() {
  const { user } = useUser();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [uploadStatus, setUploadStatus] = useState<'success' | 'error' | null>(null);
  const [message, setMessage] = useState('');
  const [resultados, setResultados] = useState<AnalisisResultado[]>([]);
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



  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setSelectedFile(event.target.files[0]);
      setUploadStatus(null);
      setMessage('');
    }
  };

  const handleUpload = async (e: React.FormEvent) => {
  e.preventDefault();
 



  if (!token) {
    setMessage("Sesión expirada. Por favor, inicia sesión nuevamente.");
    setUploadStatus('error');
    return;
  }

  if (!selectedFile) {
    setMessage("Por favor, selecciona un archivo primero.");
    setUploadStatus('error');
    return;
  }

  // VALIDACIÓN PREVIA: Evita enviar el archivo si ya sabemos que es muy grande
  const MAX_SIZE_MB = 3 * 1024 * 1024;
  if (selectedFile.size > MAX_SIZE_MB ) {
    setMessage(`El archivo es demasiado pesado (${(selectedFile.size / (1024 * 1024)).toFixed(2)} MB). Por favor, sube uno menor a 3MB.`);
    setUploadStatus('error');
    return;
  }

  setUploading(true);
  setUploadStatus(null);
  setMessage('');

  const formData = new FormData();
  formData.append('file', selectedFile);

  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/sentiment/upload-csv`, {
      method: "POST",
      headers: { "Authorization": `Bearer ${token}` },
      body: formData,
    });

    // MANEJO AMIGABLE DE STATUS CODES
    if (!response.ok) {
      switch (response.status) {
        case 401:
          // Aquí manejamos tu caso específico de "archivo pesado"
          throw new Error("El archivo supera el límite de 3MB. Intenta con un archivo más pequeño.");
        case 413:
          throw new Error("El servidor indica que el archivo es demasiado grande.");
        case 403:
          throw new Error("No tienes permisos para realizar esta acción.");
        case 500:
          throw new Error("Error interno del servidor. Inténtalo más tarde.");
        default:
          throw new Error(`Ocurrió un problema inesperado (Error ${response.status})`);
      }
    }

    const data = await response.json();
    setResultados(data);
    console.log("data:: ", data)
    setMessage('¡Excelente! El archivo se procesó correctamente.');
    setUploadStatus('success');
    setSelectedFile(null); 
    
  } catch (err: any) {
    // Si el error es de conexión (Servidor apagado)
    if (err.message === "Failed to fetch") {
      setMessage("No se pudo conectar con el servidor. Verifica tu conexión.");
    } else {
      setMessage(err.message);
    }
    setUploadStatus('error');
  } finally {
    setUploading(false);
  }
};

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <h1 className="text-4xl font-extrabold text-gray-800 mb-8">Análisis de Reseñas</h1>
      
      {/* SECCIÓN 1: FORMULARIO (Diseño limpio anterior) */}
      <div className="bg-white rounded-xl shadow-lg p-8 border-l-4 border-three mb-10">
        <form onSubmit={handleUpload} className="space-y-6">
          <div className="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-xl hover:border-primary transition-all">
            <div className="space-y-1 text-center">
              <CloudArrowUpIcon className="mx-auto h-12 w-12 text-gray-400" />
              <div className="flex text-sm text-gray-600">
                <label htmlFor="file" className="relative cursor-pointer font-bold text-accent hover:text-five transition-colors">
                  <span>Seleccionar CSV</span>
                  <input id="file" name="file" type="file" className="sr-only" accept=".csv" onChange={handleFileChange} />
                </label>
              </div>
              <p className="text-xs text-gray-500 font-medium">
                {selectedFile ? selectedFile.name : 'Formatos aceptados: .csv'}
              </p>
            </div>
          </div>

          <button
            type="submit"
              className="w-full bg-linear-to-r from-primary to-three text-white px-6 py-3 rounded-xl
               font-bold hover:shadow-lg hover:shadow-primary/30 transition-all active:scale-95 disabled:opacity-50"
            disabled={!selectedFile || uploading}
          >
            {uploading ? 'Analizando con IA...' : 'Iniciar Procesamiento Masivo'}
          </button>
        </form>

        {message && (
          <div className={`mt-6 p-4 rounded-xl flex items-center border ${
            uploadStatus === 'success' ? 'bg-green-50 border-green-200 text-green-700' : 'bg-red-50 border-red-200 text-red-700'
          }`}>
            {uploadStatus === 'success' ? <CheckCircleIcon className="h-5 w-5 mr-2" /> : <ExclamationCircleIcon className="h-5 w-5 mr-2" />}
            <span className="text-sm font-semibold">{message}</span>
          </div>
        )}
      </div>

      {/* SECCIÓN 2: TABLA DE RESULTADOS ESTADÍSTICOS */}
      {resultados.length > 0 && (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden animate-in fade-in slide-in-from-bottom-4 duration-700">
          <div className="px-6 py-5 bg-gray-50 border-b border-gray-100 flex justify-between items-center">
            <h2 className="text-lg font-bold text-gray-700 flex items-center gap-2">
              <ChartBarIcon className="h-5 w-5 text-three" />
              Detalle del Análisis Masivo
            </h2>
            <span className="text-xs font-bold uppercase tracking-widest text-gray-400 bg-white px-3 py-1 rounded-full border border-gray-200">
              {resultados.length} Comentarios encontrados
            </span>
          </div>
          
          <div className="overflow-x-auto">
            <table className="min-w-full">
              <thead>
                <tr className="bg-white border-b border-gray-100">
                  <th className="px-6 py-4 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">Comentario</th>
                  <th className="px-6 py-4 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">Presvision</th>
                  <th className="px-6 py-4 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">Probabilidad</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {resultados.map((item, index) => (
                  <tr key={index} className="hover:bg-gray-50/50 transition-colors">
                    <td className="px-6 py-4">
                      <div className="flex items-start gap-3">
                        <ChatBubbleLeftRightIcon className="h-5 w-5 text-gray-300 mt-0.5" />
                        <p className="text-sm text-gray-600 line-clamp-2">{item.texto}</p>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold uppercase ${
                        item.prevision === 'positivo' 
                          ? 'bg-green-50 text-green-600 border border-green-100' 
                          : 'bg-red-50 text-red-600 border border-red-100'
                      }`}>
                        {item.prevision === 'positivo' ? <FaceSmileIcon className="h-4 w-4" /> : <FaceFrownIcon className="h-4 w-4" />}
                        {item.prevision}
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <div className="w-16 bg-gray-100 rounded-full h-1.5">
                          <div 
                            className={`h-1.5 rounded-full ${item.prevision === 'positivo' ? 'bg-green-400' : 'bg-red-400'}`} 
                            style={{ width: `${item.probabilidad * 100}%` }}
                          ></div>
                        </div>
                        <span className="text-xs font-mono font-bold text-gray-500">
                          {(item.probabilidad * 100).toFixed(1)}%
                        </span>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}