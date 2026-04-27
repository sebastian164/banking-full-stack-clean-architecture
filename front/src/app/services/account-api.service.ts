import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountRequest, AccountResponse } from '../interfaces/account.interface';
import { ApiHttpClientService } from './http/api-http-client.service';

@Injectable({ providedIn: 'root' })
export class AccountApiService {
  private readonly http = inject(ApiHttpClientService);
  private readonly endpoint = '/api/accounts';

  findAll(): Observable<AccountResponse[]> {
    return this.http.get<AccountResponse[]>(this.endpoint);
  }

  create(account: AccountRequest): Observable<AccountResponse> {
    return this.http.post<AccountResponse, AccountRequest>(this.endpoint, account);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.endpoint}/${id}`);
  }
}
