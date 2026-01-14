"use client";

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import {
  HomeIcon,
  ChartBarIcon,
  CloudArrowUpIcon,
  FaceSmileIcon,
  ArrowRightOnRectangleIcon
} from '@heroicons/react/24/outline'; // Iconos de Heroicons


import { logout } from "../actions/logout";

const navigation = [
  { name: 'Dashboard', href: '/dashboard', icon: HomeIcon },
  //{ name: 'Análisis de Sentimiento', href: '/dashboard/sentiment', icon: FaceSmileIcon },
  { name: 'Estadísticas', href: '/dashboard/statistics', icon: ChartBarIcon },
  { name: 'Subir CSV', href: '/dashboard/upload-csv', icon: CloudArrowUpIcon },
];

export default function Sidebar() {
  const pathname = usePathname();

  const handleLogout = async () => {
    if (confirm("¿Estás seguro de que quieres cerrar sesión?")) {
      await logout();
    }
  };


  return (
    <aside className="fixed top-0 left-0 h-full w-64 bg-gradient-to-br from-primary via-three to-five text-white p-6 shadow-xl z-50">
      <div className="flex items-center justify-center mb-10 mt-2">
        <span className="text-3xl font-extrabold italic text-white tracking-wide">
          SentimentAPI AI
        </span>
      </div>

      <nav className="space-y-3">
        {navigation.map((item) => {
          const isActive = pathname === item.href;
          return (
            <Link key={item.name} href={item.href} >
              <div
               className={`flex  mt-2 items-center p-3 rounded-lg text-lg font-medium transition-all duration-200
                  ${isActive
                    ? 'bg-three bg-opacity-20 text-white shadow-md border-l-4 border-five/90 pl-2'
                    : 'text-white hover:bg-four     hover:bg-opacity-10'
                  }`}
              >
                   <item.icon className="h-6 w-6 mr-4" />
                {item.name}
              </div>
              
            </Link>
          );
        })}
      </nav>

      <div className="absolute bottom-6 left-6 right-6">
        <button
         onClick={handleLogout}
        className="flex items-center w-full p-3 rounded-lg text-lg font-medium text-white bg-primary/30 hover:bg-primary hover:bg-opacity-10 transition-all duration-200">
          <ArrowRightOnRectangleIcon className="h-6 w-6 mr-4" />
          Cerrar Sesión
        </button>
      </div>
    </aside>
  );
}