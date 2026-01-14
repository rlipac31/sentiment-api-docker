import type { NextConfig } from "next";

const nextConfig: NextConfig = {

  /* config options here */
    images: {
    domains: ['https://lipa-sentiment-api.azurewebsites.net'], // Si usas imágenes externas
  },
  // Esto ayuda a depurar fallos de build en Vercel
  typescript: {
    ignoreBuildErrors: false, 
  },
};

export default nextConfig;

