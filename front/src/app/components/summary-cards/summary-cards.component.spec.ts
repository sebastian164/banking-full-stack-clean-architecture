import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SummaryCardsComponent } from './summary-cards.component';

describe('SummaryCardsComponent', () => {
  let fixture: ComponentFixture<SummaryCardsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SummaryCardsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SummaryCardsComponent);
  });

  it('should render counters', () => {
    fixture.componentInstance.clients = 3;
    fixture.componentInstance.accounts = 4;
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('3');
    expect(fixture.nativeElement.textContent).toContain('4');
  });
});
