export interface ClientResponse {
  id?: number;
  name: string;
  gender: string;
  age: number;
  identification: string;
  address: string;
  phone: string;
  password: string;
  status: boolean;
}

export type ClientRequest = Omit<ClientResponse, 'id'>;
