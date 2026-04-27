import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';
import { ClientApiService } from './client-api.service';

describe('ClientApiService', () => {
  let service: ClientApiService;
  let httpMock: HttpTestingController;
  const endpoint = `${environment.apiUrl}/api/clients`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ClientApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should list clients', () => {
    service.findAll().subscribe((clients) => expect(clients.length).toBe(1));

    const request = httpMock.expectOne(endpoint);
    expect(request.request.method).toBe('GET');
    request.flush([{ id: 1, name: 'Jose Lema', gender: 'Masculino', age: 30, identification: '123', address: 'Quito', phone: '099', password: '1234', status: true }]);
  });

  it('should create client', () => {
    const payload = { name: 'Ana', gender: 'Femenino', age: 28, identification: '456', address: 'Quito', phone: '098', password: '1234', status: true };

    service.create(payload).subscribe((client) => expect(client.id).toBe(2));

    const request = httpMock.expectOne(endpoint);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(payload);
    request.flush({ id: 2, ...payload });
  });
});
