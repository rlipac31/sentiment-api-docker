"use server";

import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export async function loginAction(prevState: any, formData: FormData) {
  const email = formData.get("email");
  const contrasenia = formData.get("contrasenia");

  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, contrasenia }),
    });

    if (!response.ok) {
      return { error: "Credenciales incorrectas. Intenta de nuevo." };
    }

    const data = await response.json();

    if (data.token) {
      const cookieStore = await cookies();
      cookieStore.set("session_token", data.token, {
        httpOnly: true,
        secure: process.env.NODE_ENV === "production",
        sameSite: "lax",
        maxAge: 60 * 60 * 24,
        path: "/",
      });
    } else {
      return { error: "No se recibió un token válido." };
    }
  } catch (error) {
    // Aquí atrapamos si el servidor de Spring Boot está caído
    console.error("Error de conexión:", error);
    return { error: "El servicio de autenticación no está disponible." };
  }

  // El redirect debe ir FUERA del bloque try/catch en Next.js
  // porque redirect() lanza internamente un error que Next usa para saltar de página
  redirect("/dashboard");
}