import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClientResponse } from '../../interfaces/client.interface';

@Component({
  selector: 'app-client-edit-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './client-edit-modal.component.html',
})
export class ClientEditModalComponent {
  @Input({ required: true }) client!: ClientResponse;
  @Output() save = new EventEmitter<void>();
  @Output() close = new EventEmitter<void>();
  @Output() integerKey = new EventEmitter<KeyboardEvent>();
  @Output() digitsOnly = new EventEmitter<{ field: 'editAge' | 'editPhone'; event: Event }>();
}
