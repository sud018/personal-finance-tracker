import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BudgetService, Budget } from '../../core/services/budget';
import { CategoryService, Category } from '../../core/services/category';

@Component({
  selector: 'app-budget',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './budget.html',
  styleUrl: './budget.css'
})
export class BudgetComponent implements OnInit {
  budgets: Budget[] = [];
  categories: Category[] = [];
  isLoading = true;
  showForm = false;
  editingBudget: Budget | null = null;
  errorMessage = '';
  form: FormGroup;

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
    private budgetService: BudgetService,
    private categoryService: CategoryService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      amount: ['', [Validators.required, Validators.min(0.01)]],
      month: [this.currentMonth, [Validators.required]],
      year: [this.currentYear, [Validators.required]],
      categoryId: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    this.loadCategories();
    this.loadBudgets();
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

  loadBudgets() {
    this.isLoading = true;
    this.budgetService.getBudget(this.currentMonth, this.currentYear).subscribe({
      next: (data) => {
        this.budgets = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }

  onMonthChange() {
    this.loadBudgets();
  }

  openAddForm() {
    this.editingBudget = null;
    this.form.reset({
      month: this.currentMonth,
      year: this.currentYear
    });
    this.showForm = true;
  }

  openEditForm(budget: Budget) {
    this.editingBudget = { ...budget };
    this.form.patchValue({
      amount: budget.amount,
      month: budget.month,
      year: budget.year,
      categoryId: budget.categoryId
    });
    this.showForm = true;
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formValue = this.form.value;

    if (this.editingBudget?.id) {
      this.budgetService.updateBudget(this.editingBudget.id, formValue).subscribe({
        next: () => {
          this.showForm = false;
          this.loadBudgets();
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to update budget.';
          this.cdr.detectChanges();
        }
      });
    } else {
      this.budgetService.createBudget(formValue).subscribe({
        next: () => {
          this.showForm = false;
          this.loadBudgets();
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to create budget.';
          this.cdr.detectChanges();
        }
      });
    }
  }

  cancelForm() {
    this.showForm = false;
    this.editingBudget = null;
    this.errorMessage = '';
  }

  getCategoryName(categoryId: number): string {
    return this.categories.find(c => c.id === categoryId)?.name || '';
  }

  getMonthName(month: number): string {
    return this.months.find(m => m.value === month)?.label || '';
  }
}