import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ReportRequest, ReportResponse } from '../interfaces/report.interface';
import { ApiHttpClientService, QueryParams } from './http/api-http-client.service';

@Injectable({ providedIn: 'root' })
export class ReportApiService {
  private readonly http = inject(ApiHttpClientService);
  private readonly endpoint = '/api/reports';

  generate(request: ReportRequest): Observable<ReportResponse> {
    return this.http.get<ReportResponse>(this.endpoint, this.toParams(request));
  }

  pdfUrl(request: ReportRequest): string {
    return this.http.url(`${this.endpoint}/pdf`, this.toParams(request));
  }

  private toParams(request: ReportRequest): QueryParams {
    return {
      clientId: request.clientId,
      startDate: request.startDate,
      endDate: request.endDate,
    };
  }
}
