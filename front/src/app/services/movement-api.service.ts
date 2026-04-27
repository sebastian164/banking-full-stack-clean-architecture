import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { MovementRequest, MovementResponse } from '../interfaces/movement.interface';
import { ApiHttpClientService } from './http/api-http-client.service';

@Injectable({ providedIn: 'root' })
export class MovementApiService {
  private readonly http = inject(ApiHttpClientService);
  private readonly endpoint = '/api/movements';

  findAll(): Observable<MovementResponse[]> {
    return this.http.get<MovementResponse[]>(this.endpoint);
  }

  create(movement: MovementRequest): Observable<MovementResponse> {
    return this.http.post<MovementResponse, MovementRequest>(this.endpoint, movement);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.endpoint}/${id}`);
  }
}
