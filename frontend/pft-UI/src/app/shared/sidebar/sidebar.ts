import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  navItems = [
  { label: 'Dashboard', icon: '📊', route: '/dashboard' },
  { label: 'Transactions', icon: '💳', route: '/transactions' },
  { label: 'Categories', icon: '🏷️', route: '/categories' },
  { label: 'Budget', icon: '💰', route: '/budget' },
  { label: 'Reports', icon: '📈', route: '/reports' }
];
}
