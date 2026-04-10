# Refinamento Técnico - Front-end Angular para Sistema de Controle Financeiro

## 1. Contexto do Projeto

Sistema de controle financeiro pessoal e doméstico com funcionalidades de gestão de despesas, receitas, contas a pagar, cofrinhos (piggy banks) e lista de compras. O front-end Angular consumirá uma API REST backend com autenticação JWT.

---

## 2. Arquitetura e Organização de Código

### 2.1 Estrutura de Pastas

```
src/
├── app/
│   ├── core/                     # Módulo singleton (serviços globais, guards, interceptors)
│   │   ├── guards/
│   │   ├── interceptors/
│   │   ├── models/              # Interfaces e tipos compartilhados
│   │   └── services/            # Serviços singleton (auth, http, storage)
│   ├── shared/                   # Componentes, pipes e diretivas reutilizáveis
│   │   ├── components/
│   │   ├── pipes/
│   │   └── directives/
│   ├── features/                 # Módulos de funcionalidades (lazy-loaded)
│   │   ├── auth/
│   │   ├── dashboard/
│   │   ├── finances/
│   │   ├── bills/
│   │   ├── piggy-bank/
│   │   └── shopping-list/
│   └── layout/                   # Componentes de layout (header, sidebar, footer)
├── assets/
├── environments/
└── styles/
```

### 2.2 Padrões de Arquitetura

**REGRA OBRIGATÓRIA**: Utilize **Arquitetura em Camadas** com separação clara de responsabilidades:

1. **Presentation Layer (Components)**
   - Apenas lógica de apresentação e interação com usuário
   - Não deve conter lógica de negócio
   - Delega operações para Services

2. **Service Layer (Services)**
   - Lógica de negócio e comunicação com API
   - Gerenciamento de estado quando necessário
   - Transformação de dados entre DTOs e Models

3. **Data Layer (Models/DTOs)**
   - Interfaces TypeScript para tipagem forte
   - DTOs para comunicação com API
   - Models para uso interno na aplicação

---

## 3. Padrões de Código e Clean Code

### 3.1 Naming Conventions

```typescript
// Classes e Interfaces - PascalCase
export class AuthService { }
export interface User { }

// Variáveis e funções - camelCase
const userName: string;
function getUserProfile() { }

// Constantes - UPPER_SNAKE_CASE
const API_BASE_URL = 'http://localhost:8080';

// Arquivos - kebab-case
auth.service.ts
user-profile.component.ts
```

### 3.2 Princípios SOLID

**Single Responsibility**: Cada componente/serviço deve ter uma única responsabilidade bem definida.

```typescript
// ❌ ERRADO - Componente com múltiplas responsabilidades
export class DashboardComponent {
  fetchData() { /* HTTP call */ }
  calculateTotal() { /* business logic */ }
  saveToStorage() { /* storage logic */ }
}

// ✅ CORRETO - Responsabilidades separadas
export class DashboardComponent {
  constructor(
    private financeService: FinanceService,
    private calculationService: CalculationService
  ) { }
}
```

**Dependency Inversion**: Dependa de abstrações, não de implementações concretas.

```typescript
// ✅ Interface para abstração
export interface IStorageService {
  set(key: string, value: any): void;
  get(key: string): any;
  remove(key: string): void;
}

// Implementação pode variar (LocalStorage, SessionStorage, etc)
export class LocalStorageService implements IStorageService {
  // implementation
}
```

### 3.3 Código Limpo e Legível

```typescript
// ❌ EVITAR - Código confuso e sem tipagem
function getData(id) {
  return this.http.get('/api/data/' + id).pipe(
    map((res: any) => res.data)
  );
}

// ✅ PREFERIR - Código claro, tipado e auto-documentado
function getFinanceById(financeId: string): Observable<Finance> {
  const endpoint = `${this.baseUrl}/finances/${financeId}`;
  
  return this.http.get<FinanceResponse>(endpoint).pipe(
    map(response => this.mapToFinance(response)),
    catchError(this.handleError)
  );
}
```

### 3.4 DRY (Don't Repeat Yourself)

```typescript
// ✅ Extrair lógica repetida para funções reutilizáveis
export abstract class BaseHttpService {
  protected handleError(error: HttpErrorResponse): Observable<never> {
    console.error('API Error:', error);
    const errorMessage = this.getErrorMessage(error);
    return throwError(() => new Error(errorMessage));
  }
  
  protected getErrorMessage(error: HttpErrorResponse): string {
    if (error.error instanceof ErrorEvent) {
      return `Client Error: ${error.error.message}`;
    }
    return `Server Error: ${error.status} - ${error.message}`;
  }
}
```

---

## 4. Gerenciamento de Estado

### 4.1 Estratégia

Para aplicações de médio porte, utilize **Services com BehaviorSubject** (padrão simples e eficaz).

```typescript
@Injectable({ providedIn: 'root' })
export class UserStateService {
  private userSubject = new BehaviorSubject<User | null>(null);
  public user$ = this.userSubject.asObservable();
  
  setUser(user: User): void {
    this.userSubject.next(user);
  }
  
  clearUser(): void {
    this.userSubject.next(null);
  }
  
  getCurrentUser(): User | null {
    return this.userSubject.value;
  }
}
```

**OBSERVAÇÃO**: Para aplicações maiores, considere NgRx ou Akita apenas se a complexidade justificar.

---

## 5. Segurança

### 5.1 Autenticação JWT

```typescript
@Injectable({ providedIn: 'root' })
export class AuthInterceptor implements HttpInterceptor {
  constructor(private storageService: StorageService) { }
  
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.storageService.get('access_token');
    
    if (token) {
      const cloned = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(cloned);
    }
    
    return next.handle(req);
  }
}
```

### 5.2 Guards para Rotas

```typescript
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }
  
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {
    if (this.authService.isAuthenticated()) {
      return true;
    }
    
    // Redireciona para login salvando a URL tentada
    return this.router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url }
    });
  }
}
```

### 5.3 Sanitização de Dados

```typescript
// ✅ SEMPRE sanitize inputs do usuário
import { DomSanitizer } from '@angular/platform-browser';

export class ProfileComponent {
  constructor(private sanitizer: DomSanitizer) { }
  
  sanitizeInput(input: string): string {
    // Remove caracteres perigosos
    return input.replace(/[<>]/g, '');
  }
}
```

### 5.4 Variáveis de Ambiente

```typescript
// ❌ NUNCA hardcode credenciais ou URLs
const apiKey = 'abc123';

// ✅ Use environment files
// environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  appVersion: '1.0.0'
};
```

---

## 6. Performance e Otimização

### 6.1 Lazy Loading

```typescript
// app-routing.module.ts
const routes: Routes = [
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.module')
      .then(m => m.DashboardModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'finances',
    loadChildren: () => import('./features/finances/finances.module')
      .then(m => m.FinancesModule),
    canActivate: [AuthGuard]
  }
];
```

### 6.2 OnPush Change Detection

```typescript
@Component({
  selector: 'app-finance-card',
  templateUrl: './finance-card.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush // 🚀 Performance boost
})
export class FinanceCardComponent {
  @Input() finance!: Finance;
}
```

### 6.3 Unsubscribe de Observables

```typescript
// ✅ OPÇÃO 1: takeUntil pattern
export class DashboardComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  
  ngOnInit(): void {
    this.financeService.getFinances()
      .pipe(takeUntil(this.destroy$))
      .subscribe(finances => this.finances = finances);
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// ✅ OPÇÃO 2: Async pipe (preferível quando possível)
// No template: {{ finances$ | async }}
```

### 6.4 TrackBy em *ngFor

```typescript
// template
<div *ngFor="let finance of finances; trackBy: trackByFinanceId">
  {{ finance.description }}
</div>

// component
trackByFinanceId(index: number, finance: Finance): string {
  return finance.id;
}
```

---

## 7. Validação de Formulários

### 7.1 Reactive Forms (OBRIGATÓRIO)

```typescript
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  
  constructor(private fb: FormBuilder) { }
  
  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }
  
  get email() {
    return this.loginForm.get('email');
  }
  
  get password() {
    return this.loginForm.get('password');
  }
  
  onSubmit(): void {
    if (this.loginForm.valid) {
      const credentials = this.loginForm.value;
      this.authService.login(credentials).subscribe();
    }
  }
}
```

### 7.2 Custom Validators

```typescript
export class CustomValidators {
  static cpf(control: AbstractControl): ValidationErrors | null {
    const value = control.value?.replace(/\D/g, '');
    
    if (!value || value.length !== 11) {
      return { cpf: true };
    }
    
    // Lógica de validação de CPF
    return null;
  }
  
  static futureDate(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    
    if (value && new Date(value) <= new Date()) {
      return { futureDate: true };
    }
    
    return null;
  }
}
```

---

## 8. Tratamento de Erros

### 8.1 Global Error Handler

```typescript
@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  constructor(private notificationService: NotificationService) { }
  
  handleError(error: Error | HttpErrorResponse): void {
    console.error('Global Error:', error);
    
    if (error instanceof HttpErrorResponse) {
      this.handleHttpError(error);
    } else {
      this.handleClientError(error);
    }
  }
  
  private handleHttpError(error: HttpErrorResponse): void {
    const message = error.error?.message || 'Erro ao comunicar com o servidor';
    this.notificationService.showError(message);
  }
  
  private handleClientError(error: Error): void {
    this.notificationService.showError('Erro inesperado na aplicação');
  }
}
```

### 8.2 HTTP Error Interceptor

```typescript
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService
  ) { }
  
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
          this.notificationService.showError('Sessão expirada. Faça login novamente.');
        }
        
        if (error.status === 403) {
          this.notificationService.showError('Você não tem permissão para esta ação.');
        }
        
        return throwError(() => error);
      })
    );
  }
}
```

---

## 9. Testes

### 9.1 Testes Unitários (OBRIGATÓRIO)

```typescript
describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  
  afterEach(() => {
    httpMock.verify();
  });
  
  it('should authenticate user and store token', () => {
    const mockResponse = { token: 'fake-jwt-token' };
    
    service.login({ email: 'test@test.com', password: '123456' }).subscribe(
      response => {
        expect(response.token).toBe('fake-jwt-token');
      }
    );
    
    const req = httpMock.expectOne(`${service.apiUrl}/auth/login`);
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });
});
```

### 9.2 Cobertura Mínima

- **Services**: 80% de cobertura
- **Components**: 60% de cobertura
- **Guards/Interceptors**: 90% de cobertura

---

## 10. Acessibilidade (A11y)

### 10.1 Diretrizes WCAG

```html
<!-- ✅ Sempre use labels e ARIA attributes -->
<label for="email">Email</label>
<input 
  id="email" 
  type="email" 
  aria-required="true"
  aria-describedby="email-error"
/>
<span id="email-error" role="alert" *ngIf="email?.invalid">
  Email inválido
</span>

<!-- ✅ Use semantic HTML -->
<nav aria-label="Menu principal">
  <ul>
    <li><a href="/dashboard">Dashboard</a></li>
  </ul>
</nav>

<!-- ✅ Keyboard navigation -->
<button (click)="submit()" (keydown.enter)="submit()">
  Salvar
</button>
```

---

## 11. Estilização e UI/UX

### 11.1 Design System

Crie um design system consistente com:
- **Paleta de cores** (primária, secundária, sucesso, erro, aviso)
- **Tipografia** (tamanhos, pesos, famílias)
- **Espaçamento** (padding, margin em escala: 4px, 8px, 16px, 24px, 32px)
- **Componentes reutilizáveis** (botões, cards, modals)

### 11.2 CSS Architecture

```scss
// ✅ Use BEM ou SCSS com namespacing
.finance-card {
  &__header {
    display: flex;
    justify-content: space-between;
  }
  
  &__title {
    font-size: 1.25rem;
    font-weight: 600;
  }
  
  &__amount {
    color: var(--color-primary);
    
    &--negative {
      color: var(--color-danger);
    }
  }
}
```

### 11.3 Responsividade

```scss
// Mobile-first approach
.dashboard {
  padding: 1rem;
  
  @media (min-width: 768px) {
    padding: 2rem;
  }
  
  @media (min-width: 1024px) {
    padding: 3rem;
  }
}
```

---

## 12. Documentação de Código

### 12.1 JSDoc para Classes e Métodos Complexos

```typescript
/**
 * Serviço responsável pela gestão de finanças do usuário.
 * Fornece métodos para criar, listar, atualizar e deletar registros financeiros.
 */
@Injectable({ providedIn: 'root' })
export class FinanceService {
  
  /**
   * Calcula o balanço total de um período específico.
   * 
   * @param startDate - Data inicial do período
   * @param endDate - Data final do período
   * @returns Observable com o valor total do balanço
   * @throws Error se as datas forem inválidas
   */
  calculateBalance(startDate: Date, endDate: Date): Observable<number> {
    // implementation
  }
}
```

---

## 13. CI/CD e Build

### 13.1 Scripts no package.json

```json
{
  "scripts": {
    "start": "ng serve",
    "build": "ng build --configuration production",
    "test": "ng test --code-coverage",
    "lint": "ng lint",
    "format": "prettier --write \"src/**/*.{ts,html,scss}\"",
    "analyze": "ng build --stats-json && webpack-bundle-analyzer dist/stats.json"
  }
}
```

### 13.2 Linting Rules (ESLint + Prettier)

Configure regras estritas para manter consistência no código.

---

## 14. Checklist de Implementação

### Para cada Feature Module:

- [ ] Estrutura de pastas seguindo o padrão definido
- [ ] Services com tipagem forte (TypeScript strict mode)
- [ ] Models/Interfaces para todas as entidades
- [ ] Tratamento de erros com feedback ao usuário
- [ ] Validação de formulários (client-side)
- [ ] Guards de autenticação/autorização quando necessário
- [ ] Lazy loading do módulo
- [ ] Testes unitários com cobertura mínima
- [ ] Componentes com OnPush quando possível
- [ ] Unsubscribe de Observables (takeUntil ou async pipe)
- [ ] Acessibilidade (ARIA, labels, semantic HTML)
- [ ] Responsividade (mobile-first)
- [ ] Loading states e skeleton screens
- [ ] Empty states (quando não há dados)
- [ ] Documentação de código complexo

---

## 15. Prompt Guidelines para LLM

Ao solicitar código à LLM, sempre especifique:

1. **Contexto**: "Estou desenvolvendo um sistema de controle financeiro em Angular 17+"
2. **Arquitetura**: "Seguindo arquitetura em camadas com separação de responsabilidades"
3. **Padrões**: "Use Reactive Forms, TypeScript strict, OnPush change detection"
4. **Requisitos**: "Implemente tratamento de erros, validação e testes unitários"
5. **Clean Code**: "Aplique princípios SOLID, DRY e nomenclatura clara"
6. **Segurança**: "Inclua sanitização de inputs e guards de autenticação"

### Exemplo de Prompt Efetivo:

```
Crie um componente Angular para listagem de finanças seguindo:
- Arquitetura em camadas (component + service + models)
- OnPush change detection
- Reactive patterns com RxJS
- Tratamento de erros e loading states
- Validação de formulários com Reactive Forms
- Testes unitários básicos
- Acessibilidade (ARIA labels)
- Clean code e TypeScript strict
```

---

## 16. Integração com API Backend

### 16.1 Mapeamento de Endpoints

| Funcionalidade | Método | Endpoint | Autenticação |
|----------------|--------|----------|--------------|
| Login | POST | `/auth/login` | ❌ |
| Register | POST | `/auth/register` | ❌ |
| Get User | GET | `/users/me` | ✅ |
| List Finances | GET | `/users/me/finances` | ✅ |
| Create Finance | POST | `/users/me/finances` | ✅ |
| List Bills | GET | `/bills` | ✅ |
| Create Bill | POST | `/bills` | ✅ |
| List Piggy Banks | GET | `/piggy-banks` | ✅ |
| Create Deposit | POST | `/piggy-banks/{id}/deposits` | ✅ |
| Shopping List | GET | `/shopping-list` | ✅ |

### 16.2 DTOs Consistentes

```typescript
// Request DTO
export interface CreateFinanceRequest {
  description: string;
  amount: number;
  type: 'INCOME' | 'EXPENSE';
  category: string;
  date: string; // ISO 8601
}

// Response DTO
export interface FinanceResponse {
  id: string;
  description: string;
  amount: number;
  type: 'INCOME' | 'EXPENSE';
  category: string;
  date: string;
  createdAt: string;
}

// Domain Model
export interface Finance {
  id: string;
  description: string;
  amount: number;
  type: FinanceType;
  category: Category;
  date: Date;
  createdAt: Date;
}
```

### 16.3 Exemplos de Requisições e Respostas

#### 16.3.1 Autenticação - Login

**Request:**
```http
POST /auth/login HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "senha123"
}
```

**Response - Sucesso (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "email": "usuario@example.com",
    "role": "USER"
  }
}
```

**Response - Erro (401 Unauthorized):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Credenciais inválidas",
  "path": "/auth/login"
}
```

**Implementação no Service:**
```typescript
@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => this.storeToken(response.token)),
      catchError(this.handleError)
    );
  }
}
```

---

#### 16.3.2 Autenticação - Registro

**Request:**
```http
POST /auth/register HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "name": "John Doe",
  "email": "novo@example.com",
  "password": "senha123",
  "cpf": "123.456.789-00"
}
```

**Response - Sucesso (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "John Doe",
  "email": "novo@example.com",
  "role": "USER",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**Response - Erro (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Email já cadastrado",
  "path": "/auth/register"
}
```

---

#### 16.3.3 Usuário - Obter Perfil

**Request:**
```http
GET /users/me HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response - Sucesso (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "usuario@example.com",
  "cpf": "123.456.789-00",
  "role": "USER",
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

**Implementação no Service:**
```typescript
@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;
  
  getProfile(): Observable<User> {
    return this.http.get<UserResponse>(`${this.apiUrl}/me`).pipe(
      map(response => this.mapToUser(response)),
      catchError(this.handleError)
    );
  }
}
```

---

#### 16.3.4 Finanças - Listar Finanças

**Request:**
```http
GET /users/me/finances?startDate=2024-01-01&endDate=2024-01-31&type=EXPENSE HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response - Sucesso (200 OK):**
```json
{
  "data": [
    {
      "id": "fin-001",
      "description": "Compra supermercado",
      "amount": 350.50,
      "type": "EXPENSE",
      "category": "FOOD",
      "date": "2024-01-15T00:00:00Z",
      "createdAt": "2024-01-15T10:30:00Z"
    },
    {
      "id": "fin-002",
      "description": "Salário",
      "amount": 5000.00,
      "type": "INCOME",
      "category": "SALARY",
      "date": "2024-01-05T00:00:00Z",
      "createdAt": "2024-01-05T08:00:00Z"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

**Implementação no Service:**
```typescript
getFinances(filters?: FinanceFilters): Observable<FinanceList> {
  let params = new HttpParams();
  
  if (filters?.startDate) {
    params = params.set('startDate', filters.startDate.toISOString());
  }
  if (filters?.endDate) {
    params = params.set('endDate', filters.endDate.toISOString());
  }
  if (filters?.type) {
    params = params.set('type', filters.type);
  }
  
  return this.http.get<FinanceListResponse>(`${this.apiUrl}/finances`, { params }).pipe(
    map(response => this.mapToFinanceList(response)),
    catchError(this.handleError)
  );
}
```

---

#### 16.3.5 Finanças - Criar Finança

**Request:**
```http
POST /users/me/finances HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "description": "Pagamento aluguel",
  "amount": 1500.00,
  "type": "EXPENSE",
  "category": "HOUSING",
  "date": "2024-01-15T00:00:00Z"
}
```

**Response - Sucesso (201 Created):**
```json
{
  "id": "fin-003",
  "description": "Pagamento aluguel",
  "amount": 1500.00,
  "type": "EXPENSE",
  "category": "HOUSING",
  "date": "2024-01-15T00:00:00Z",
  "createdAt": "2024-01-15T11:00:00Z"
}
```

**Response - Erro (422 Unprocessable Entity):**
```json
{
  "timestamp": "2024-01-15T11:00:00Z",
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Validation failed",
  "errors": [
    {
      "field": "amount",
      "message": "O valor deve ser maior que zero"
    },
    {
      "field": "description",
      "message": "A descrição é obrigatória"
    }
  ],
  "path": "/users/me/finances"
}
```

**Implementação no Service:**
```typescript
createFinance(finance: CreateFinanceRequest): Observable<Finance> {
  return this.http.post<FinanceResponse>(`${this.apiUrl}/finances`, finance).pipe(
    map(response => this.mapToFinance(response)),
    catchError(this.handleError)
  );
}
```

---

#### 16.3.6 Contas (Bills) - Listar Contas

**Request:**
```http
GET /bills?status=PENDING&accountType=PERSONAL HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response - Sucesso (200 OK):**
```json
{
  "data": [
    {
      "id": "bill-001",
      "description": "Conta de luz",
      "amount": 180.50,
      "dueDate": "2024-01-25T00:00:00Z",
      "status": "PENDING",
      "accountType": "HOUSE",
      "recurrent": true,
      "category": "UTILITIES",
      "createdAt": "2024-01-01T10:00:00Z"
    },
    {
      "id": "bill-002",
      "description": "Netflix",
      "amount": 39.90,
      "dueDate": "2024-01-20T00:00:00Z",
      "status": "PENDING",
      "accountType": "PERSONAL",
      "recurrent": true,
      "category": "ENTERTAINMENT",
      "createdAt": "2024-01-01T10:00:00Z"
    }
  ]
}
```

---

#### 16.3.7 Contas (Bills) - Criar Conta

**Request:**
```http
POST /bills HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "description": "Conta de água",
  "amount": 95.00,
  "dueDate": "2024-01-28T00:00:00Z",
  "accountType": "HOUSE",
  "recurrent": true,
  "category": "UTILITIES"
}
```

**Response - Sucesso (201 Created):**
```json
{
  "id": "bill-003",
  "description": "Conta de água",
  "amount": 95.00,
  "dueDate": "2024-01-28T00:00:00Z",
  "status": "PENDING",
  "accountType": "HOUSE",
  "recurrent": true,
  "category": "UTILITIES",
  "createdAt": "2024-01-15T11:30:00Z"
}
```

---

#### 16.3.8 Cofrinhos (Piggy Banks) - Listar Cofrinhos

**Request:**
```http
GET /piggy-banks HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response - Sucesso (200 OK):**
```json
{
  "data": [
    {
      "id": "piggy-001",
      "name": "Viagem para Europa",
      "icon": "✈️",
      "targetAmount": 15000.00,
      "currentAmount": 5250.00,
      "progress": 35.0,
      "targetDate": "2024-12-31T00:00:00Z",
      "status": "ACTIVE",
      "createdAt": "2024-01-01T00:00:00Z"
    },
    {
      "id": "piggy-002",
      "name": "Fundo de emergência",
      "icon": "🏦",
      "targetAmount": 10000.00,
      "currentAmount": 7500.00,
      "progress": 75.0,
      "targetDate": "2024-06-30T00:00:00Z",
      "status": "ACTIVE",
      "createdAt": "2024-01-01T00:00:00Z"
    }
  ]
}
```

---

#### 16.3.9 Cofrinhos - Criar Depósito

**Request:**
```http
POST /piggy-banks/piggy-001/deposits HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "amount": 500.00,
  "description": "Depósito mensal"
}
```

**Response - Sucesso (201 Created):**
```json
{
  "id": "deposit-001",
  "piggyBankId": "piggy-001",
  "amount": 500.00,
  "description": "Depósito mensal",
  "createdAt": "2024-01-15T12:00:00Z",
  "piggyBank": {
    "id": "piggy-001",
    "name": "Viagem para Europa",
    "currentAmount": 5750.00,
    "targetAmount": 15000.00,
    "progress": 38.33
  }
}
```

**Response - Erro (403 Forbidden):**
```json
{
  "timestamp": "2024-01-15T12:00:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Você não tem permissão para fazer depósitos neste cofrinho",
  "path": "/piggy-banks/piggy-001/deposits"
}
```

---

#### 16.3.10 Lista de Compras - Listar Itens

**Request:**
```http
GET /shopping-list?includePurchased=false HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response - Sucesso (200 OK):**
```json
{
  "data": [
    {
      "id": "item-001",
      "name": "Arroz 5kg",
      "quantity": 2,
      "category": "GRAINS",
      "isPurchased": false,
      "createdAt": "2024-01-15T10:00:00Z"
    },
    {
      "id": "item-002",
      "name": "Feijão 1kg",
      "quantity": 3,
      "category": "GRAINS",
      "isPurchased": false,
      "createdAt": "2024-01-15T10:00:00Z"
    }
  ]
}
```

---

#### 16.3.11 Lista de Compras - Adicionar Item

**Request:**
```http
POST /shopping-list HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "Leite integral 1L",
  "quantity": 6,
  "category": "DAIRY"
}
```

**Response - Sucesso (201 Created):**
```json
{
  "id": "item-003",
  "name": "Leite integral 1L",
  "quantity": 6,
  "category": "DAIRY",
  "isPurchased": false,
  "createdAt": "2024-01-15T12:30:00Z"
}
```

---

#### 16.3.12 Dashboard - Obter Resumo Financeiro

**Request:**
```http
GET /users/me/dashboard/summary?month=2024-01 HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response - Sucesso (200 OK):**
```json
{
  "period": {
    "month": 1,
    "year": 2024
  },
  "summary": {
    "totalIncome": 5000.00,
    "totalExpenses": 3250.50,
    "balance": 1749.50,
    "savingsRate": 34.99
  },
  "categories": [
    {
      "category": "FOOD",
      "amount": 850.00,
      "percentage": 26.15
    },
    {
      "category": "HOUSING",
      "amount": 1500.00,
      "percentage": 46.14
    },
    {
      "category": "TRANSPORT",
      "amount": 450.50,
      "percentage": 13.86
    },
    {
      "category": "ENTERTAINMENT",
      "amount": 250.00,
      "percentage": 7.69
    },
    {
      "category": "UTILITIES",
      "amount": 200.00,
      "percentage": 6.15
    }
  ],
  "upcomingBills": [
    {
      "id": "bill-001",
      "description": "Conta de luz",
      "amount": 180.50,
      "dueDate": "2024-01-25T00:00:00Z",
      "daysRemaining": 10
    }
  ],
  "piggyBanksProgress": [
    {
      "id": "piggy-001",
      "name": "Viagem para Europa",
      "progress": 38.33,
      "currentAmount": 5750.00,
      "targetAmount": 15000.00
    }
  ]
}
```

---

### 16.4 Tratamento de Erros Padronizado

Todos os erros da API seguem o mesmo formato de resposta:

```typescript
export interface ApiErrorResponse {
  timestamp: string;       // ISO 8601
  status: number;          // HTTP status code
  error: string;           // Status text
  message: string;         // Mensagem descritiva
  path: string;            // Endpoint que gerou o erro
  errors?: FieldError[];   // Opcional: erros de validação
}

export interface FieldError {
  field: string;
  message: string;
}
```

**Implementação de Tratamento de Erros no Service:**

```typescript
protected handleError(error: HttpErrorResponse): Observable<never> {
  let errorMessage = 'Erro desconhecido';
  
  if (error.error instanceof ErrorEvent) {
    // Erro do lado do cliente
    errorMessage = `Erro: ${error.error.message}`;
  } else {
    // Erro do lado do servidor
    const apiError = error.error as ApiErrorResponse;
    
    if (apiError?.errors && apiError.errors.length > 0) {
      // Erro de validação
      errorMessage = apiError.errors.map(e => e.message).join(', ');
    } else if (apiError?.message) {
      errorMessage = apiError.message;
    } else {
      errorMessage = `Erro ${error.status}: ${error.statusText}`;
    }
  }
  
  console.error('API Error:', error);
  this.notificationService.showError(errorMessage);
  
  return throwError(() => new Error(errorMessage));
}
```

---

## 17. Conclusão

Este refinamento técnico estabelece as bases para um front-end Angular robusto, escalável e mantível. Siga rigorosamente estes padrões para garantir:

- ✅ **Código limpo e legível**
- ✅ **Arquitetura bem definida**
- ✅ **Segurança e performance**
- ✅ **Testabilidade e manutenibilidade**
- ✅ **Acessibilidade e UX**

**LEMBRE-SE**: Qualidade de código não é negociável. Prefira fazer menos funcionalidades com alta qualidade do que muitas funcionalidades com código técnico deficiente.
