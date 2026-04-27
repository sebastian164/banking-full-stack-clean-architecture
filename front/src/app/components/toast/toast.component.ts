import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

export type ToastType = 'success' | 'error';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.component.html',
})
export class ToastComponent {
  @Input({ required: true }) message = '';
  @Input() type: ToastType = 'success';
  @Output() dismiss = new EventEmitter<void>();
}
