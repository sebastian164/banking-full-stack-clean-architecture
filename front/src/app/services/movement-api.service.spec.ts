import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';
import { MovementApiService } from './movement-api.service';

describe('MovementApiService', () => {
  let service: MovementApiService;
  let httpMock: HttpTestingController;
  const endpoint = `${environment.apiUrl}/api/movements`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(MovementApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should list movements', () => {
    service.findAll().subscribe((movements) => expect(movements[0].accountNumber).toBe('478758'));

    const request = httpMock.expectOne(endpoint);
    expect(request.request.method).toBe('GET');
    request.flush([{ id: 1, date: '2026-04-26', type: 'CREDITO', value: 100, balance: 2100, accountNumber: '478758' }]);
  });

  it('should create movement', () => {
    const payload = { date: '2026-04-26', type: 'DEBITO', value: -50, accountNumber: '478758' };

    service.create(payload).subscribe((movement) => expect(movement.balance).toBe(1950));

    const request = httpMock.expectOne(endpoint);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(payload);
    request.flush({ id: 1, balance: 1950, ...payload });
  });
});
