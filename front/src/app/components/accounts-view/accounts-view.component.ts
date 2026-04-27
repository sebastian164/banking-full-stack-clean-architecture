import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AccountResponse } from '../../interfaces/account.interface';
import { ClientResponse } from '../../interfaces/client.interface';

@Component({
  selector: 'app-accounts-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './accounts-view.component.html',
})
export class AccountsViewComponent {
  @Input({ required: true }) accounts: AccountResponse[] = [];
  @Input({ required: true }) clients: ClientResponse[] = [];
  @Input({ required: true }) accountForm!: AccountResponse;
  @Input() search = '';

  @Output() searchChange = new EventEmitter<string>();
  @Output() saveAccount = new EventEmitter<void>();
  @Output() deleteAccount = new EventEmitter<number | undefined>();
  @Output() integerKey = new EventEmitter<KeyboardEvent>();
  @Output() digitsOnly = new EventEmitter<{ field: 'accountNumber' | 'accountInitialBalance'; event: Event }>();

  filtered<T>(items: T[]): T[] {
    const term = this.search.trim().toLowerCase();
    return term ? items.filter((item) => JSON.stringify(item).toLowerCase().includes(term)) : items;
  }
}
