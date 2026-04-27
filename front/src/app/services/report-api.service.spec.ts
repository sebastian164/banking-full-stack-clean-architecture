import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';
import { ReportApiService } from './report-api.service';

describe('ReportApiService', () => {
  let service: ReportApiService;
  let httpMock: HttpTestingController;
  const endpoint = `${environment.apiUrl}/api/reports`;
  const request = { clientId: 1, startDate: '2026-01-01', endDate: '2026-12-31' };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ReportApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should generate report with query params', () => {
    service.generate(request).subscribe((report) => expect(report.totalCredits).toBe(100));

    const httpRequest = httpMock.expectOne((req) => req.url === endpoint);
    expect(httpRequest.request.method).toBe('GET');
    expect(httpRequest.request.params.get('clientId')).toBe('1');
    expect(httpRequest.request.params.get('startDate')).toBe('2026-01-01');
    expect(httpRequest.request.params.get('endDate')).toBe('2026-12-31');
    httpRequest.flush({ totalCredits: 100, totalDebits: 0, statements: [] });
  });

  it('should build pdf url', () => {
    expect(service.pdfUrl(request)).toBe(`${endpoint}/pdf?clientId=1&startDate=2026-01-01&endDate=2026-12-31`);
  });
});
