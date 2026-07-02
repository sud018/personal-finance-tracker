import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient, withInterceptors, HttpClient } from '@angular/common/http';
import { authInterceptor } from '../interceptors/auth-interceptor';
import { AuthService } from '../services/auth';

describe('authInterceptor', () => {
  let httpMock: HttpTestingController;
  let http: HttpClient;
  let authService: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting()
      ]
    });
    httpMock = TestBed.inject(HttpTestingController);
    http = TestBed.inject(HttpClient);
    authService = TestBed.inject(AuthService);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should add Authorization header when token exists', () => {
    localStorage.setItem('token', 'test-jwt-token');

    http.get('/api/transactions').subscribe();

    const req = httpMock.expectOne('/api/transactions');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-jwt-token');
  });

  it('should NOT add Authorization header when no token', () => {
    localStorage.removeItem('token');

    http.get('/api/transactions').subscribe();

    const req = httpMock.expectOne('/api/transactions');
    expect(req.request.headers.get('Authorization')).toBeNull();
  });
});