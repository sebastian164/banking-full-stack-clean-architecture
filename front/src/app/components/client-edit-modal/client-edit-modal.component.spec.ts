import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClientEditModalComponent } from './client-edit-modal.component';

describe('ClientEditModalComponent', () => {
  let fixture: ComponentFixture<ClientEditModalComponent>;
  let component: ClientEditModalComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientEditModalComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ClientEditModalComponent);
    component = fixture.componentInstance;
    component.client = { id: 1, name: 'Jose', gender: 'Masculino', age: 30, identification: '123', address: 'Quito', phone: '099', password: '1234', status: true };
  });

  it('should emit close event', () => {
    jest.spyOn(component.close, 'emit');
    component.close.emit();

    expect(component.close.emit).toHaveBeenCalled();
  });
});
