import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ToastComponent } from './toast.component';

describe('ToastComponent', () => {
  let fixture: ComponentFixture<ToastComponent>;
  let component: ToastComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToastComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ToastComponent);
    component = fixture.componentInstance;
    component.message = 'Operacion exitosa';
  });

  it('should render the message', () => {
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Operacion exitosa');
  });

  it('should emit dismiss event', () => {
    jest.spyOn(component.dismiss, 'emit');

    component.dismiss.emit();

    expect(component.dismiss.emit).toHaveBeenCalled();
  });
});
