import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReportsViewComponent } from './reports-view.component';

describe('ReportsViewComponent', () => {
  let fixture: ComponentFixture<ReportsViewComponent>;
  let component: ReportsViewComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportsViewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ReportsViewComponent);
    component = fixture.componentInstance;
    component.reportForm = { clientId: 1, startDate: '2026-01-01', endDate: '2026-12-31' };
  });

  it('should filter report statements', () => {
    component.search = '478758';
    component.report = {
      totalCredits: 100,
      totalDebits: 0,
      statements: [
        { date: '2026-04-26', client: 'Jose', accountNumber: '478758', accountType: 'Ahorro', movement: 100, availableBalance: 2100 },
        { date: '2026-04-26', client: 'Ana', accountNumber: '225487', accountType: 'Corriente', movement: 50, availableBalance: 150 },
      ],
    };

    expect(component.filteredStatements().length).toBe(1);
  });

  it('should emit download event', () => {
    jest.spyOn(component.downloadReport, 'emit');
    component.downloadReport.emit();

    expect(component.downloadReport.emit).toHaveBeenCalled();
  });
});
