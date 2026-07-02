import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TransactionService, Transaction } from '../../core/services/transaction';
import { Category } from '../../core/services/category';

@Component({
  selector: 'app-transaction-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transaction-form.html',
  styleUrl: './transaction-form.css'
})
export class TransactionForm implements OnInit {
  @Input() transaction: Transaction | null = null;
  @Input() categories: Category[] = [];
  @Output() saved = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  form: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      amount: ['', [Validators.required, Validators.min(0.01)]],
      description: [''],
      date: ['', [Validators.required]],
      type: ['EXPENSE', [Validators.required]],
      categoryId: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    if (this.transaction) {
      this.form.patchValue({
        amount: this.transaction.amount,
        description: this.transaction.description,
        date: this.transaction.date,
        type: this.transaction.type,
        categoryId: this.transaction.categoryId
      });
    }
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    const formValue = this.form.value;

    if (this.transaction?.id) {
      this.transactionService.updateTransaction(this.transaction.id, formValue)
        .subscribe({
          next: () => {
            this.isLoading = false;
            this.saved.emit();
          },
          error: (err) => {
            this.isLoading = false;
            this.errorMessage = err.error?.message || 'Failed to update transaction.';
            this.cdr.detectChanges();
          }
        });
    } else {
      this.transactionService.createTransaction(formValue)
        .subscribe({
          next: () => {
            this.isLoading = false;
            this.saved.emit();
          },
          error: (err) => {
            this.isLoading = false;
            this.errorMessage = err.error?.message || 'Failed to create transaction.';
            this.cdr.detectChanges();
          }
        });
    }
  }

  onCancel() {
    this.cancelled.emit();
  }
}