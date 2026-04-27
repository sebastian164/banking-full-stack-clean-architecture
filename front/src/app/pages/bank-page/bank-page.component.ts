import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, ViewEncapsulation, inject } from '@angular/core';
import { forkJoin } from 'rxjs';
import { AccountResponse } from '../../interfaces/account.interface';
import { ClientResponse } from '../../interfaces/client.interface';
import { MovementResponse } from '../../interfaces/movement.interface';
import { ReportRequest, ReportResponse } from '../../interfaces/report.interface';
import { AccountApiService } from '../../services/account-api.service';
import { ClientApiService } from '../../services/client-api.service';
import { MovementApiService } from '../../services/movement-api.service';
import { ReportApiService } from '../../services/report-api.service';
import { AccountsViewComponent } from '../../components/accounts-view/accounts-view.component';
import { BankSection, BankSidebarComponent } from '../../components/bank-sidebar/bank-sidebar.component';
import { ClientEditModalComponent } from '../../components/client-edit-modal/client-edit-modal.component';
import { ClientsViewComponent } from '../../components/clients-view/clients-view.component';
import { MovementsViewComponent } from '../../components/movements-view/movements-view.component';
import { ReportsViewComponent } from '../../components/reports-view/reports-view.component';
import { SummaryCardsComponent } from '../../components/summary-cards/summary-cards.component';
import { ToastComponent, ToastType } from '../../components/toast/toast.component';

@Component({
  selector: 'app-bank-page',
  standalone: true,
  imports: [
    CommonModule,
    AccountsViewComponent,
    BankSidebarComponent,
    ClientEditModalComponent,
    ClientsViewComponent,
    MovementsViewComponent,
    ReportsViewComponent,
    SummaryCardsComponent,
    ToastComponent,
  ],
  templateUrl: './bank-page.component.html',
  styleUrls: ['./bank-page.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class BankPageComponent implements OnInit, OnDestroy {
  private static readonly TOAST_DURATION_MS = 3500;
  private readonly clientApi = inject(ClientApiService);
  private readonly accountApi = inject(AccountApiService);
  private readonly movementApi = inject(MovementApiService);
  private readonly reportApi = inject(ReportApiService);

  section: BankSection = 'clientes';
  search = '';
  loading = false;
  message = '';
  messageType: ToastType = 'success';
  private toastTimer?: ReturnType<typeof setTimeout>;

  clients: ClientResponse[] = [];
  accounts: AccountResponse[] = [];
  movements: MovementResponse[] = [];
  report?: ReportResponse;
  editingClient?: ClientResponse;

  clientForm: ClientResponse = this.emptyClient();
  accountForm: AccountResponse = this.emptyAccount();
  movementForm: MovementResponse = this.emptyMovement();
  reportForm: ReportRequest = { clientId: 1, startDate: '2026-01-01', endDate: '2026-12-31' };

  ngOnInit(): void {
    this.loadAll();
  }

  ngOnDestroy(): void {
    this.stopToastTimer();
  }

  loadAll(): void {
    this.loading = true;
    forkJoin({
      clients: this.clientApi.findAll(),
      accounts: this.accountApi.findAll(),
      movements: this.movementApi.findAll(),
    }).subscribe({
      next: ({ clients, accounts, movements }) => {
        this.clients = clients;
        this.accounts = accounts;
        this.movements = movements;
        this.syncSelectedClient();
        if (!this.movementForm.accountNumber && accounts.length) {
          this.movementForm.accountNumber = accounts[0].number;
        }
        this.loading = false;
      },
      error: (error) => {
        this.loading = false;
        this.showError(error);
      },
    });
  }

  saveClient(): void {
    const client = this.normalizeClient(this.clientForm);
    if (!this.isValidClient(client)) {
      return;
    }
    this.clientApi.create(client).subscribe({
      next: () => {
        this.showSuccess('Cliente guardado correctamente');
        this.clientForm = this.emptyClient();
        this.loadAll();
      },
      error: (error) => this.showError(error),
    });
  }

  openClientEditor(client: ClientResponse): void {
    this.editingClient = { ...client };
    this.clearToast();
  }

  closeClientEditor(): void {
    this.editingClient = undefined;
  }

  updateClient(): void {
    if (!this.editingClient?.id) {
      return;
    }
    const client = this.normalizeClient(this.editingClient);
    if (!this.isValidClient(client, false)) {
      return;
    }
    this.clientApi.update(this.editingClient.id, client).subscribe({
      next: () => {
        this.showSuccess('Cliente actualizado correctamente');
        this.closeClientEditor();
        this.loadAll();
      },
      error: (error) => this.showError(error),
    });
  }

  saveAccount(): void {
    const account = this.normalizeAccount(this.accountForm);
    if (!this.isValidAccount(account)) {
      return;
    }
    this.accountApi.create(account).subscribe({
      next: () => {
        this.showSuccess('Cuenta guardada correctamente');
        this.accountForm = this.emptyAccount();
        this.loadAll();
      },
      error: (error) => this.showError(error),
    });
  }

  saveMovement(): void {
    const movement = this.normalizeMovement(this.movementForm);
    if (!this.isValidMovement(movement)) {
      return;
    }
    this.movementApi.create(movement).subscribe({
      next: () => {
        this.showSuccess('Movimiento registrado correctamente');
        this.movementForm = this.emptyMovement();
        this.loadAll();
      },
      error: (error) => this.showError(error),
    });
  }

  delete(path: 'clientes' | 'cuentas' | 'movimientos', id?: number): void {
    if (!id) {
      return;
    }
    const request = path === 'clientes'
      ? this.clientApi.delete(id)
      : path === 'cuentas'
        ? this.accountApi.delete(id)
        : this.movementApi.delete(id);
    request.subscribe({
      next: () => {
        this.showSuccess('Registro eliminado');
        this.loadAll();
      },
      error: (error) => this.showError(error),
    });
  }

  generateReport(): void {
    this.reportApi.generate(this.reportForm).subscribe({
      next: (data) => {
        this.report = data;
        this.showSuccess('Reporte generado');
      },
      error: (error) => this.showError(error),
    });
  }

  downloadReport(): void {
    window.open(this.reportApi.pdfUrl(this.reportForm), '_blank');
  }

  selectSection(section: BankSection): void {
    this.section = section;
    this.clearToast();
  }

  clearToast(): void {
    this.stopToastTimer();
    this.message = '';
  }

  onlyIntegerKey(event: KeyboardEvent): void {
    const allowedKeys = ['Backspace', 'Delete', 'Tab', 'Escape', 'Enter', 'ArrowLeft', 'ArrowRight', 'Home', 'End'];
    if (allowedKeys.includes(event.key) || ((event.ctrlKey || event.metaKey) && ['a', 'c', 'v', 'x'].includes(event.key.toLowerCase()))) {
      return;
    }
    if (!/^\d$/.test(event.key)) {
      event.preventDefault();
    }
  }

  keepDigitsOnly(field: 'clientAge' | 'clientIdentification' | 'clientPhone' | 'editAge' | 'editPhone' | 'accountNumber' | 'accountInitialBalance' | 'movementValue', event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/\D/g, '');
    input.value = value;
    if (field === 'clientAge') {
      this.clientForm.age = Number(value || 0);
    }
    if (field === 'clientIdentification') {
      this.clientForm.identification = value;
    }
    if (field === 'clientPhone') {
      this.clientForm.phone = value;
    }
    if (field === 'editAge' && this.editingClient) {
      this.editingClient.age = Number(value || 0);
    }
    if (field === 'editPhone' && this.editingClient) {
      this.editingClient.phone = value;
    }
    if (field === 'accountNumber') {
      this.accountForm.number = value;
    }
    if (field === 'accountInitialBalance') {
      this.accountForm.initialBalance = Number(value || 0);
    }
    if (field === 'movementValue') {
      this.movementForm.value = Number(value || 0);
    }
  }

  private syncSelectedClient(): void {
    if (this.clients.length === 0) {
      return;
    }
    const firstClientId = this.clients[0].id ?? 1;
    this.accountForm.clientId = this.accountForm.clientId || firstClientId;
    this.reportForm.clientId = this.reportForm.clientId || firstClientId;
  }

  private showSuccess(message: string): void {
    this.showToast(message, 'success');
  }

  private showError(error: any): void {
    const details = Array.isArray(error?.error?.details) && error.error.details.length
      ? `: ${error.error.details.join(', ')}`
      : '';
    this.showToast(`${error?.error?.message || error?.message || 'No se pudo completar la operacion'}${details}`, 'error');
  }

  private normalizeClient(client: ClientResponse): ClientResponse {
    return {
      ...client,
      age: Math.trunc(Number(client.age) || 0),
      identification: String(client.identification || '').replace(/\D/g, ''),
      phone: String(client.phone || '').replace(/\D/g, ''),
      gender: client.gender || 'Masculino',
    };
  }

  private isValidClient(client: ClientResponse, validateIdentification = true): boolean {
    if (!Number.isInteger(client.age) || client.age < 0) {
      this.showErrorMessage('La edad debe ser un numero entero');
      return false;
    }
    if (validateIdentification && !/^\d+$/.test(client.identification)) {
      this.showErrorMessage('La identificacion debe contener solo numeros');
      return false;
    }
    if (client.phone && !/^\d+$/.test(client.phone)) {
      this.showErrorMessage('El telefono debe contener solo numeros');
      return false;
    }
    return true;
  }

  private normalizeAccount(account: AccountResponse): AccountResponse {
    return {
      ...account,
      number: String(account.number || '').replace(/\D/g, ''),
      initialBalance: Math.trunc(Number(account.initialBalance) || 0),
    };
  }

  private isValidAccount(account: AccountResponse): boolean {
    if (!/^\d+$/.test(account.number)) {
      this.showErrorMessage('El numero de cuenta debe contener solo numeros');
      return false;
    }
    if (!Number.isInteger(account.initialBalance) || account.initialBalance < 0) {
      this.showErrorMessage('El saldo inicial debe contener solo numeros');
      return false;
    }
    return true;
  }

  private normalizeMovement(movement: MovementResponse): MovementResponse {
    return {
      ...movement,
      value: Math.trunc(Number(movement.value) || 0),
    };
  }

  private isValidMovement(movement: MovementResponse): boolean {
    if (!Number.isInteger(movement.value) || movement.value < 0) {
      this.showErrorMessage('El valor debe contener solo numeros');
      return false;
    }
    return true;
  }

  private showErrorMessage(message: string): void {
    this.showToast(message, 'error');
  }

  private showToast(message: string, type: ToastType): void {
    this.stopToastTimer();
    this.message = message;
    this.messageType = type;
    this.toastTimer = setTimeout(() => this.clearToast(), BankPageComponent.TOAST_DURATION_MS);
  }

  private stopToastTimer(): void {
    if (this.toastTimer) {
      clearTimeout(this.toastTimer);
      this.toastTimer = undefined;
    }
  }

  private emptyClient(): ClientResponse {
    return {
      name: '',
      gender: 'Masculino',
      age: 18,
      identification: '',
      address: '',
      phone: '',
      password: '1234',
      status: true,
    };
  }

  private emptyAccount(): AccountResponse {
    return {
      number: '',
      type: 'Ahorro',
      initialBalance: 0,
      status: true,
      clientId: 1,
    };
  }

  private emptyMovement(): MovementResponse {
    return {
      date: new Date().toISOString().slice(0, 10),
      type: 'CREDITO',
      value: 0,
      accountNumber: this.accounts[0]?.number || '',
    };
  }
}
