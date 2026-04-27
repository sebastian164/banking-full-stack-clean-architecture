import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClientResponse } from '../../interfaces/client.interface';
import { ReportRequest, ReportResponse } from '../../interfaces/report.interface';

@Component({
  selector: 'app-reports-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reports-view.component.html',
})
export class ReportsViewComponent {
  @Input({ required: true }) clients: ClientResponse[] = [];
  @Input({ required: true }) reportForm!: ReportRequest;
  @Input() report?: ReportResponse;
  @Input() search = '';

  @Output() searchChange = new EventEmitter<string>();
  @Output() generateReport = new EventEmitter<void>();
  @Output() downloadReport = new EventEmitter<void>();

  filteredStatements() {
    const statements = this.report?.statements ?? [];
    const term = this.search.trim().toLowerCase();
    return term ? statements.filter((item) => JSON.stringify(item).toLowerCase().includes(term)) : statements;
  }
}
