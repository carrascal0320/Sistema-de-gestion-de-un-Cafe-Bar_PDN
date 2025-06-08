export interface CrudAuditoria {
  id: number;
  email: string;
  metodoAutenticacion: string;
  // Añadimos 'name' y 'phone' para tener más datos del usuario en el momento del login.
  // Son opcionales porque no siempre estarán disponibles (ej. ciertos OAuth).
  name?: string;
  phone?: string | null;
  // La fecha y hora se recibirán como string en formato ISO del backend,
  // y se pueden formatear en el frontend para una mejor visualización.
  fechaRegistroServidor: string;
}
