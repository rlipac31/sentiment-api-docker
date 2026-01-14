

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

interface SentimentResult {
  texto: string;
  prevision: 'POSITIVO'  | 'NEGATIVO';
  probabilidad: number;
}