import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  constructor(private authService: AuthService, private router: Router) {}

  get currentUser() {
    return this.authService.getCurrentUser();
  }
   logout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }
}
