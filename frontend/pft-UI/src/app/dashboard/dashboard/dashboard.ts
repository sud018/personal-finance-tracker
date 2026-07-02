import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReportService, MonthlySummary } from '../../core/services/report';
import { TransactionService, Transaction } from '../../core/services/transaction';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  summary: MonthlySummary = { totalIncome: 0, totalExpense: 0, balance: 0 };
  recentTransactions: Transaction[] = [];
  categoryBreakdown: any[] = [];
  trendData: any[] = [];
  isLoading = true;

  currentMonth = new Date().getMonth() + 1;
  currentYear = new Date().getFullYear();

  constructor(
    private reportService: ReportService,
    private transactionService: TransactionService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.isLoading = true;

    this.reportService.getMonthlySummary(this.currentMonth, this.currentYear)
      .subscribe({
        next: (data) => {
          this.summary = data;
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Summary error:', err)
      });

    this.transactionService.getAllTransactions(0, 5)
      .subscribe({
        next: (data) => {
          this.recentTransactions = data.content;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Transactions error:', err)
      });

    this.reportService.getCategoryBreakdown(this.currentMonth, this.currentYear)
      .subscribe({
        next: (data) => {
          this.categoryBreakdown = data;
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Breakdown error:', err)
      });

    this.reportService.getTrend()
      .subscribe({
        next: (data) => {
          this.trendData = data;
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Trend error:', err)
      });
  }

  goToTransactions() {
    this.router.navigate(['/transactions']);
  }

  goToBudget() {
    this.router.navigate(['/budget']);
  }
}