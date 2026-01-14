'use server'

import { UserProvider } from '../../context/UserContext';
import { cookies } from 'next/headers';

import Sidebar from '../components/Sidebar';

export default async function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {


  const cookieStore = await cookies();

  interface UserData {
    idUser: number | string;
    name: string;
    role: string;
    token: string;
  }


  // Preparamos los datos iniciales desde el servidor

  const initialUser: UserData = {
    idUser: '',
    name: '',
    role: '',
    token: cookieStore.get('session_token')?.value || '' // Supongamos que obtienes el token de cookies o una variable
  };


  return (
    <UserProvider initialUser={initialUser}>
      <div className="flex">
        <Sidebar />
        <div className="ml-64 w-full min-h-screen bg-gray-50 p-8"> {/* ml-64 para dejar espacio al sidebar */}
          {children}
        </div>
      </div>
    </UserProvider>
  );
}