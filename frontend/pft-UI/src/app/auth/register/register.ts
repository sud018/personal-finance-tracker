import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  registerForm: FormGroup;
  errorMessage = "";
  isLoading = false;

  constructor(private fb: FormBuilder, private router: Router, private authService: AuthService, private cdr: ChangeDetectorRef, private toastr: ToastrService){
    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['',[Validators.required, Validators.email]],
      password: ['',[Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      fullname: ['', [Validators.required]]
    }, {validators: this.passwordMatchValidator});
  }

  passwordMatchValidator(form: AbstractControl){
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    if(password !== confirmPassword){
      form.get('confirmPassword')?.setErrors({passwordMismatch: true});
    }
    return null;
  }
  onSubmit() {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    this.authService.register(this.registerForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
         this.toastr.error(this.errorMessage, 'Registration Failed');
        this.cdr.detectChanges();
      }
    });
  }
}
