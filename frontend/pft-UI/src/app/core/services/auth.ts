import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface AuthResponse{
  token: string;
  email: string;
  username: string;
  fullname: string;
}
export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullname: string;
}
export interface LoginRequest {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  constructor(private http: HttpClient){}

  register(request: RegisterRequest): Observable<AuthResponse>{
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request)
    .pipe(tap(response => this.saveToken(response.token)));
  }
    login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request)
      .pipe(tap(response => this.saveToken(response.token)));
  }
  saveToken(token: string): void{
    localStorage.setItem('token',token);
  }
  getToken(): string | null{
    return localStorage.getItem('token');
  }

  logout(): void{
    localStorage.removeItem('token');
  }

   isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    return !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string): boolean{
    try{
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiry = payload.exp *1000;
      return Date.now()>expiry;
    }
    catch(e){
      return true;
    }
  }

  getCurrentUser(): any {
  const token = this.getToken();
  if (!token) return null;
  
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return {
      email: payload.sub,
      issuedAt: payload.iat,
      expiresAt: payload.exp
    };
  } catch (e) {
    return null;
  }
}
}
