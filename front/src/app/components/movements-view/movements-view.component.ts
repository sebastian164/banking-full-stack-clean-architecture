import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AccountResponse } from '../../interfaces/account.interface';
import { MovementResponse } from '../../interfaces/movement.interface';

@Component({
  selector: 'app-movements-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './movements-view.component.html',
})
export class MovementsViewComponent {
  @Input({ required: true }) accounts: AccountResponse[] = [];
  @Input({ required: true }) movements: MovementResponse[] = [];
  @Input({ required: true }) movementForm!: MovementResponse;
  @Input() search = '';

  @Output() searchChange = new EventEmitter<string>();
  @Output() saveMovement = new EventEmitter<void>();
  @Output() deleteMovement = new EventEmitter<number | undefined>();
  @Output() integerKey = new EventEmitter<KeyboardEvent>();
  @Output() digitsOnly = new EventEmitter<Event>();

  displayedMovements(): MovementResponse[] {
    return [...this.filtered(this.movements)]
      .sort((left, right) => this.toTimestamp(right.date) - this.toTimestamp(left.date) || (right.id ?? 0) - (left.id ?? 0));
  }

  filtered<T>(items: T[]): T[] {
    const term = this.search.trim().toLowerCase();
    return term ? items.filter((item) => JSON.stringify(item).toLowerCase().includes(term)) : items;
  }

  private toTimestamp(date?: string): number {
    return date ? new Date(`${date}T00:00:00`).getTime() : 0;
  }
}
