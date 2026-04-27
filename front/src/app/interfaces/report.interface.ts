export interface AccountStatementResponse {
  date: string;
  client: string;
  accountNumber: string;
  accountType: string;
  movement: number;
  availableBalance: number;
}

export interface ReportResponse {
  totalDebits: number;
  totalCredits: number;
  statements: AccountStatementResponse[];
}

export interface ReportRequest {
  clientId: number;
  startDate: string;
  endDate: string;
}
