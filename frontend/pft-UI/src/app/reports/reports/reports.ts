import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReportService, MonthlySummary } from '../../core/services/report';
import { ExportService } from '../../core/services/export';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reports.html',
  styleUrl: './reports.css'
})
export class Reports implements OnInit {
  summary: MonthlySummary = { totalIncome: 0, totalExpense: 0, balance: 0 };
  categoryBreakdown: any[] = [];
  trendData: any[] = [];
  isLoading = true;

  currentMonth = new Date().getMonth() + 1;
  currentYear = new Date().getFullYear();

  months = [
    { value: 1, label: 'January' }, { value: 2, label: 'February' },
    { value: 3, label: 'March' }, { value: 4, label: 'April' },
    { value: 5, label: 'May' }, { value: 6, label: 'June' },
    { value: 7, label: 'July' }, { value: 8, label: 'August' },
    { value: 9, label: 'September' }, { value: 10, label: 'October' },
    { value: 11, label: 'November' }, { value: 12, label: 'December' }
  ];

  constructor(
    private reportService: ReportService,
    private exportService: ExportService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.isLoading = true;

    this.reportService.getMonthlySummary(this.currentMonth, this.currentYear)
      .subscribe({
        next: (data) => {
          this.summary = data;
          this.cdr.detectChanges();
        },
        error: (err) => console.error(err)
      });

    this.reportService.getCategoryBreakdown(this.currentMonth, this.currentYear)
      .subscribe({
        next: (data) => {
          this.categoryBreakdown = data;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: (err) => console.error(err)
      });

    this.reportService.getTrend()
      .subscribe({
        next: (data) => {
          this.trendData = data;
          this.cdr.detectChanges();
        },
        error: (err) => console.error(err)
      });
  }

  onMonthChange() {
    this.loadData();
  }

  get savingsRate(): number {
    if (this.summary.totalIncome === 0) return 0;
    return Math.round((this.summary.balance / this.summary.totalIncome) * 100);
  }

  getBarWidth(amount: number): number {
    const max = Math.max(...this.categoryBreakdown.map(c => c.total));
    return max > 0 ? (amount / max) * 100 : 0;
  }

  getMonthLabel(month: number, year: number): string {
    return this.months.find(m => m.value === month)?.label?.substring(0, 3) + ' ' + year;
  }

  getMaxTrend(): number {
    return Math.max(...this.trendData.map(t => Math.max(t.totalIncome, t.totalExpense)));
  }

  downloadCSV() {
    this.exportService.downloadCSV().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'transactions.csv';
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => console.error(err)
    });
  }
}