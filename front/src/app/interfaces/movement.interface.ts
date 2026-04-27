export interface MovementResponse {
  id?: number;
  date: string;
  type: string;
  value: number;
  balance?: number;
  accountNumber: string;
}

export type MovementRequest = Omit<MovementResponse, 'id' | 'balance'>;
