import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BankSidebarComponent } from './bank-sidebar.component';

describe('BankSidebarComponent', () => {
  let fixture: ComponentFixture<BankSidebarComponent>;
  let component: BankSidebarComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BankSidebarComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BankSidebarComponent);
    component = fixture.componentInstance;
    component.section = 'clientes';
  });

  it('should emit selected section', () => {
    jest.spyOn(component.sectionChange, 'emit');
    component.sectionChange.emit('cuentas');

    expect(component.sectionChange.emit).toHaveBeenCalledWith('cuentas');
  });
});
