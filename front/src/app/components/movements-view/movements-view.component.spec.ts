import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MovementsViewComponent } from './movements-view.component';

describe('MovementsViewComponent', () => {
  let fixture: ComponentFixture<MovementsViewComponent>;
  let component: MovementsViewComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovementsViewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MovementsViewComponent);
    component = fixture.componentInstance;
    component.movementForm = { date: '2026-04-26', type: 'CREDITO', value: 0, accountNumber: '' };
  });

  it('should filter movements by type', () => {
    component.search = 'debito';
    const result = component.filtered([
      { type: 'CREDITO', accountNumber: '478758' },
      { type: 'DEBITO', accountNumber: '225487' },
    ]);

    expect(result.length).toBe(1);
  });

  it('should display the newest movements first', () => {
    component.movements = [
      { id: 1, date: '2026-04-20', type: 'CREDITO', value: 100, balance: 100, accountNumber: '478758' },
      { id: 2, date: '2026-04-26', type: 'DEBITO', value: 50, balance: 50, accountNumber: '478758' },
      { id: 3, date: '2026-04-22', type: 'CREDITO', value: 20, balance: 70, accountNumber: '225487' },
    ];

    expect(component.displayedMovements().map((movement) => movement.id)).toEqual([2, 3, 1]);
  });

  it('should emit save movement event', () => {
    jest.spyOn(component.saveMovement, 'emit');
    component.saveMovement.emit();

    expect(component.saveMovement.emit).toHaveBeenCalled();
  });
});
