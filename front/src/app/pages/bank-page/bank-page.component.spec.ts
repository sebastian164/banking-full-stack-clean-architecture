import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { BankPageComponent } from './bank-page.component';

describe('BankPageComponent', () => {
  let fixture: ComponentFixture<BankPageComponent>;
  let component: BankPageComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BankPageComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(BankPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
