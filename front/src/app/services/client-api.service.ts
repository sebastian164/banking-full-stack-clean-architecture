import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientRequest, ClientResponse } from '../interfaces/client.interface';
import { ApiHttpClientService } from './http/api-http-client.service';

@Injectable({ providedIn: 'root' })
export class ClientApiService {
  private readonly http = inject(ApiHttpClientService);
  private readonly endpoint = '/api/clients';

  findAll(): Observable<ClientResponse[]> {
    return this.http.get<ClientResponse[]>(this.endpoint);
  }

  create(client: ClientRequest): Observable<ClientResponse> {
    return this.http.post<ClientResponse, ClientRequest>(this.endpoint, client);
  }

  update(id: number, client: ClientRequest): Observable<ClientResponse> {
    return this.http.put<ClientResponse, ClientRequest>(`${this.endpoint}/${id}`, client);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.endpoint}/${id}`);
  }
}
