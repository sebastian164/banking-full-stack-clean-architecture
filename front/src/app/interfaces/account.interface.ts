export interface AccountResponse {
  id?: number;
  number: string;
  type: string;
  initialBalance: number;
  currentBalance?: number;
  status: boolean;
  clientId: number;
  clientName?: string;
}

export type AccountRequest = Omit<AccountResponse, 'id' | 'clientName'>;
