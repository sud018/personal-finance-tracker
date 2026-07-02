import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  loginForm: FormGroup;
  errorMessage= '';
  isLoading= false;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router, private cdr: ChangeDetectorRef,private toastr: ToastrService){
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  };

  onSubmit(){
    if(this.loginForm.invalid){
      this.loginForm.markAllAsTouched();
      return;
    }
    this.isLoading=true;
    this.errorMessage='';
    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.isLoading=false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Login failed. Please try again.';
        this.toastr.error(this.errorMessage, 'Login Failed');
        this.cdr.detectChanges();
      }
    });
  }


}
