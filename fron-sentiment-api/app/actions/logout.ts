"use server";

import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export async function logout() {
  const cookieStore = await cookies();

  // Eliminamos todas las cookies relacionadas con la sesión
  cookieStore.delete("session_token");

  // Redirigimos al login
  redirect("/login");
}