import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { ApiHttpClientService } from './api-http-client.service';

describe('ApiHttpClientService', () => {
  let service: ApiHttpClientService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ApiHttpClientService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should resolve the base url and query params in one place', () => {
    service.get<{ ok: boolean }>('/api/test', { clientId: 1, startDate: '2026-01-01' })
      .subscribe((response) => expect(response.ok).toBe(true));

    const request = httpMock.expectOne((req) => req.url === `${environment.apiUrl}/api/test`);
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('clientId')).toBe('1');
    expect(request.request.params.get('startDate')).toBe('2026-01-01');
    request.flush({ ok: true });
  });

  it('should build urls for external browser actions', () => {
    expect(service.url('/api/reports/pdf', { clientId: 1 }))
      .toBe(`${environment.apiUrl}/api/reports/pdf?clientId=1`);
  });
});
