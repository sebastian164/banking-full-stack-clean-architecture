import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AccountsViewComponent } from './accounts-view.component';

describe('AccountsViewComponent', () => {
  let fixture: ComponentFixture<AccountsViewComponent>;
  let component: AccountsViewComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountsViewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AccountsViewComponent);
    component = fixture.componentInstance;
    component.accountForm = { number: '', type: 'Ahorro', initialBalance: 0, status: true, clientId: 1 };
  });

  it('should filter accounts by account number', () => {
    component.search = '478';
    const result = component.filtered([
      { number: '478758', type: 'Ahorro' },
      { number: '225487', type: 'Corriente' },
    ]);

    expect(result.length).toBe(1);
  });

  it('should emit delete event with account id', () => {
    jest.spyOn(component.deleteAccount, 'emit');
    component.deleteAccount.emit(1);

    expect(component.deleteAccount.emit).toHaveBeenCalledWith(1);
  });

  it('should emit numeric field cleanup event', () => {
    const event = new Event('input');
    jest.spyOn(component.digitsOnly, 'emit');

    component.digitsOnly.emit({ field: 'accountNumber', event });

    expect(component.digitsOnly.emit).toHaveBeenCalledWith({ field: 'accountNumber', event });
  });
});
