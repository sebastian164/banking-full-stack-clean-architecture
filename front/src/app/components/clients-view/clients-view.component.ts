import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClientResponse } from '../../interfaces/client.interface';

@Component({
  selector: 'app-clients-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clients-view.component.html',
})
export class ClientsViewComponent {
  @Input({ required: true }) clients: ClientResponse[] = [];
  @Input({ required: true }) clientForm!: ClientResponse;
  @Input() search = '';

  @Output() searchChange = new EventEmitter<string>();
  @Output() saveClient = new EventEmitter<void>();
  @Output() editClient = new EventEmitter<ClientResponse>();
  @Output() deleteClient = new EventEmitter<number | undefined>();
  @Output() integerKey = new EventEmitter<KeyboardEvent>();
  @Output() digitsOnly = new EventEmitter<{ field: 'clientAge' | 'clientIdentification' | 'clientPhone'; event: Event }>();

  filtered<T>(items: T[]): T[] {
    const term = this.search.trim().toLowerCase();
    if (!term) {
      return items;
    }
    return items.filter((item) => JSON.stringify(item).toLowerCase().includes(term));
  }
}
