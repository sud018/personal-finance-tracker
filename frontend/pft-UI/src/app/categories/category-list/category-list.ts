import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService, Category } from '../../core/services/category';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './category-list.html',
  styleUrl: './category-list.css'
})
export class CategoryList implements OnInit {
  categories: Category[] = [];
  isLoading = true;
  showForm = false;
  editingCategory: Category | null = null;
  errorMessage = '';
  form: FormGroup;

  constructor(
    private categoryService: CategoryService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required]],
      type: ['EXPENSE', [Validators.required]],
      color: ['#6366f1'],
      icon: ['']
    });
  }

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.isLoading = true;
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }

  openAddForm() {
    this.editingCategory = null;
    this.form.reset({ type: 'EXPENSE', color: '#6366f1', icon: '' });
    this.showForm = true;
  }

  openEditForm(category: Category) {
    this.editingCategory = { ...category };
    this.form.patchValue({
      name: category.name,
      type: category.type,
      color: category.color,
      icon: category.icon
    });
    this.showForm = true;
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formValue = this.form.value;

    if (this.editingCategory?.id) {
      this.categoryService.updateCategory(this.editingCategory.id, formValue).subscribe({
        next: () => {
          this.showForm = false;
          this.loadCategories();
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to update category.';
          this.cdr.detectChanges();
        }
      });
    } else {
      this.categoryService.createCategory(formValue).subscribe({
        next: () => {
          this.showForm = false;
          this.loadCategories();
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to create category.';
          this.cdr.detectChanges();
        }
      });
    }
  }

  deleteCategory(id: number) {
    if (confirm('Delete this category?')) {
      this.categoryService.deleteCategory(id).subscribe({
        next: () => this.loadCategories(),
        error: (err) => console.error(err)
      });
    }
  }

  cancelForm() {
    this.showForm = false;
    this.editingCategory = null;
    this.errorMessage = '';
  }
}