import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { guestGuard } from './core/guards/guest-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
  {
    path: 'auth/login',
    canActivate: [guestGuard],
    loadComponent: () => import('./auth/login/login').then(m => m.Login)
  },
  {
    path: 'auth/register',
    canActivate: [guestGuard],
    loadComponent: () => import('./auth/register/register').then(m => m.Register)
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./shared/main-layout/main-layout').then(m => m.MainLayout),
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./dashboard/dashboard/dashboard').then(m => m.Dashboard)
      },
      {
        path: 'transactions',
        loadComponent: () => import('./transactions/transaction-list/transaction-list').then(m => m.TransactionList)
      },
      {
        path: 'budget',
        loadComponent: () => import('./budget/budget/budget').then(m => m.BudgetComponent)
      },
      {
        path: 'reports',
        loadComponent: () => import('./reports/reports/reports').then(m => m.Reports)
      },
      {
        path: 'categories',
        loadComponent: () => import('./categories/category-list/category-list').then(m => m.CategoryList)
      }
    ]
  }
];