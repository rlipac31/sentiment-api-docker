"use client"; // Importante: Context solo vive en el cliente

import { createContext, useContext, useState, ReactNode } from 'react';

// 1. Definimos la forma de los datos


interface UserData {
  idUser: number | string;
  name: string;
  role: string;
  token: string;
}

interface UserContextType {
  user: UserData | null;
  setUser: (user: UserData | null) => void;
}

// 2. Creamos el contexto
const UserContext = createContext<UserContextType | undefined>(undefined);

// 3. Proveedor del contexto
export function UserProvider({ children, initialUser }: { children: ReactNode, initialUser: UserData | null }) {
  const [user, setUser] = useState<UserData | null>(initialUser);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
}

// 4. Hook personalizado para usar el contexto fácilmente
export function useUser() {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser debe usarse dentro de un UserProvider");
  }
  return context;
}