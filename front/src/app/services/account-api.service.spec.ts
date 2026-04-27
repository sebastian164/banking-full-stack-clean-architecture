import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';
import { AccountApiService } from './account-api.service';

describe('AccountApiService', () => {
  let service: AccountApiService;
  let httpMock: HttpTestingController;
  const endpoint = `${environment.apiUrl}/api/accounts`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AccountApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should create account', () => {
    const payload = { number: '478758', type: 'Ahorro', initialBalance: 2000, status: true, clientId: 1 };

    service.create(payload).subscribe((account) => expect(account.number).toBe('478758'));

    const request = httpMock.expectOne(endpoint);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(payload);
    request.flush({ id: 1, currentBalance: 2000, clientName: 'Jose Lema', ...payload });
  });

  it('should delete account', () => {
    service.delete(1).subscribe((response) => expect(response).toBeNull());

    const request = httpMock.expectOne(`${endpoint}/1`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });
});
