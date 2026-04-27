import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClientsViewComponent } from './clients-view.component';

describe('ClientsViewComponent', () => {
  let fixture: ComponentFixture<ClientsViewComponent>;
  let component: ClientsViewComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientsViewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ClientsViewComponent);
    component = fixture.componentInstance;
    component.clientForm = { name: '', gender: 'Masculino', age: 18, identification: '', address: '', phone: '', password: '1234', status: true };
  });

  it('should filter clients by visible text', () => {
    component.search = 'jose';
    const result = component.filtered([
      { name: 'Jose Lema', identification: '123' },
      { name: 'Ana Ruiz', identification: '456' },
    ]);

    expect(result.length).toBe(1);
  });

  it('should emit save event', () => {
    jest.spyOn(component.saveClient, 'emit');
    component.saveClient.emit();

    expect(component.saveClient.emit).toHaveBeenCalled();
  });
});
