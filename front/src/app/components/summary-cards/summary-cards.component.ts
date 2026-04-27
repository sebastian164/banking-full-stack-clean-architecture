import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-summary-cards',
  standalone: true,
  templateUrl: './summary-cards.component.html',
})
export class SummaryCardsComponent {
  @Input() clients = 0;
  @Input() accounts = 0;
  @Input() movements = 0;
}
