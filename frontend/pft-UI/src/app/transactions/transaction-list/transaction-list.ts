import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TransactionService, Transaction } from '../../core/services/transaction';
import { CategoryService, Category } from '../../core/services/category';
import { TransactionForm } from '../transaction-form/transaction-form';
import { ToastrService } from 'ngx-toastr';
import { LoadingSpinner } from '../../shared/loading-spinner/loading-spinner';
import { EmptyState } from '../../shared/empty-state/empty-state';


@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [CommonModule, FormsModule, TransactionForm, LoadingSpinner, EmptyState],
  templateUrl: './transaction-list.html',
  styleUrl: './transaction-list.css'
})
export class TransactionList implements OnInit {
  transactions: Transaction[] = [];
  categories: Category[] = [];
  isLoading = true;

  // pagination
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  // filters
  selectedType = '';
  selectedCategoryId = '';
  startDate = '';
  endDate = '';

  // form modal
  showForm = false;
  editingTransaction: Transaction | null = null;

  constructor(
    private transactionService: TransactionService,
    private categoryService: CategoryService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.loadCategories();
    this.loadTransactions();
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }

  loadTransactions() {
    this.isLoading = true;

    if (this.startDate && this.endDate) {
      this.transactionService.filterByDateRange(this.startDate, this.endDate)
        .subscribe({
          next: (data) => {
            this.transactions = this.applyLocalFilters(data);
            this.isLoading = false;
            this.cdr.detectChanges();
          },
          error: (err) => console.error(err)
        });
    } else if (this.selectedCategoryId) {
      this.transactionService.filterByCategory(Number(this.selectedCategoryId))
        .subscribe({
          next: (data) => {
            this.transactions = this.applyLocalFilters(data);
            this.isLoading = false;
            this.cdr.detectChanges();
          },
          error: (err) => console.error(err)
        });
    } else {
      this.transactionService.getAllTransactions(this.currentPage, this.pageSize)
        .subscribe({
          next: (data) => {
            this.transactions = this.applyLocalFilters(data.content);
            this.totalPages = data.totalPages;
            this.totalElements = data.totalElements;
            this.isLoading = false;
            this.cdr.detectChanges();
          },
          error: (err) => console.error(err)
        });
    }
  }

  applyLocalFilters(data: Transaction[]): Transaction[] {
    if (this.selectedType) {
      return data.filter(t => t.type === this.selectedType);
    }
    return data;
  }

  onFilterChange() {
    this.currentPage = 0;
    this.loadTransactions();
  }

  clearFilters() {
    this.selectedType = '';
    this.selectedCategoryId = '';
    this.startDate = '';
    this.endDate = '';
    this.currentPage = 0;
    this.loadTransactions();
  }

  previousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadTransactions();
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadTransactions();
    }
  }

  openAddForm() {
    this.editingTransaction = null;
    this.showForm = true;
  }

  openEditForm(transaction: Transaction) {
    this.editingTransaction = { ...transaction };
    this.showForm = true;
  }

  deleteTransaction(id: number) {
    if (confirm('Are you sure you want to delete this transaction?')) {
      this.transactionService.deleteTransaction(id).subscribe({
        next: () => {
          this.toastr.success('Transaction deleted successfully!', 'Success');
          this.loadTransactions();
        },
        error: (err) => {
        this.toastr.error('Failed to delete transaction', 'Error');
      }
      });
    }
  }

  onFormSaved() {
    this.showForm = false;
    this.editingTransaction = null;
    this.toastr.success('Transaction saved successfully!', 'Success');
    this.loadTransactions();
  }

  onFormCancelled() {
    this.showForm = false;
    this.editingTransaction = null;
  }

  getCategoryName(categoryId: number): string {
    return this.categories.find(c => c.id === categoryId)?.name || '';
  }
}