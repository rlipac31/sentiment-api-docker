
'use client'
import { useActionState } from "react"; // Next.js 15+
import { loginAction } from "../actions/auth";


export default function LoginPage() {

    // El segundo argumento es el estado inicial
  const [state, formAction, isPending] = useActionState(loginAction, null);
  return (
    <main className="min-h-screen flex items-center justify-center bg-linear-to-br from-primary via-three to-five p-4">
      <div className="bg-white w-full max-w-md p-8 rounded-2xl shadow-2xl">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-primary italic">Bienvenido a Sentiment IA</h1>
          <p className="text-gray-500">Ingresa a tu cuenta</p>
        </div>

        <form action={formAction} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Correo Electrónico
            </label>
            <input
              name="email"
              type="email"
              required
              className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent focus:border-transparent outline-none transition-all"
              placeholder="tu@correo.com"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Contraseña
            </label>
            <input
              name="contrasenia"
              type="password"
              required
              className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent focus:border-transparent outline-none transition-all"
              placeholder="••••••••"
            />
          </div>

            {/* Si hay un error, lo mostramos aquí de forma elegante */}
              {state?.error && (
                <p style={{ color: "red", marginTop: "10px" }}>
                  {state.error}
                </p>
              )}

          <button
            disabled={isPending}
            type="submit"
            className="w-full py-3 px-4 bg-primary hover:bg-secondary text-white font-semibold rounded-lg shadow-lg hover:shadow-primary/30 transition-all transform active:scale-95"
          >
           {isPending ? "Cargando..." : "Iniciar Sesión"}
          </button>
        </form>

        <div className="mt-8 pt-6 border-t border-gray-100 text-center">
          <p className="text-sm text-gray-600">
            ¿No tienes cuenta?{" "}
            <span className="text-four font-bold cursor-pointer hover:underline">
              Regístrate aquí
            </span>
          </p>
        </div>
      </div>
    </main>
  );
}