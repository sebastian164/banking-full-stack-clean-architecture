import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

export type BankSection = 'clientes' | 'cuentas' | 'movimientos' | 'reportes';

@Component({
  selector: 'app-bank-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './bank-sidebar.component.html',
})
export class BankSidebarComponent {
  @Input({ required: true }) section!: BankSection;
  @Output() sectionChange = new EventEmitter<BankSection>();
}
