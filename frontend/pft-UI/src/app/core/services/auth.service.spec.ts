import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { AuthService } from './auth';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('login() should save token to localStorage', () => {
    const mockResponse = {
      token: 'mock-token-123',
      email: 'test@test.com',
      username: 'test',
      fullname: 'Test User'
    };

    service.login({ email: 'test@test.com', password: 'password123' })
      .subscribe(response => {
        expect(response.token).toBe('mock-token-123');
        expect(localStorage.getItem('token')).toBe('mock-token-123');
      });

    const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });

  it('isLoggedIn() should return false when no token', () => {
    localStorage.removeItem('token');
    expect(service.isLoggedIn()).toBeFalsy();
  });

  it('isLoggedIn() should return false for expired token', () => {
    // expired token (exp in past)
    const expiredToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjAwMDAwMDAwLCJleHAiOjE2MDAwMDAwMDF9.signature';
    localStorage.setItem('token', expiredToken);
    expect(service.isLoggedIn()).toBeFalsy();
  });

  it('logout() should remove token from localStorage', () => {
    localStorage.setItem('token', 'some-token');
    service.logout();
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('getToken() should return stored token', () => {
    localStorage.setItem('token', 'test-token');
    expect(service.getToken()).toBe('test-token');
  });
});