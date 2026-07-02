import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface Transaction {
  id?: number;
  amount: number;
  description: string;
  date: string;
  type: string;
  categoryId: number;
  categoryName?: string;
  createdAt?: string;
}
export interface PageResponse<T>{
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}
export interface MonthlySummary{
  totalIncome: number;
  totalExpense: number;
  balance: number;
}
@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  private apiUrl = `${environment.apiUrl}/transactions`;
  constructor(private http: HttpClient) {}
  getAllTransactions(page: number=0, size: number=10): Observable<PageResponse<Transaction>>{
   return this.http.get<PageResponse<Transaction>>(`${this.apiUrl}?page=${page}&size=${size}`);
  }
  getTransactionById(id: number): Observable<Transaction>{
    return this.http.get<Transaction>(`${this.apiUrl}/${id}`);
  }
  createTransaction(transaction: Transaction): Observable<Transaction>{
    return this.http.post<Transaction>(this.apiUrl, transaction);
  }
  updateTransaction(id: number, transaction: Transaction): Observable<Transaction>{
    return this.http.put<Transaction>(`${this.apiUrl}/${id}`, transaction);
  }
  deleteTransaction(id: number): Observable<string>{
    return this.http.delete(`${this.apiUrl}/${id}`, {responseType: 'text'});
  }
  filterByDateRange(start: string, end: string): Observable<Transaction[]>{
    return this.http.get<Transaction[]>(`${this.apiUrl}/filter/date-range?start=${start}&end=${end}`);
  }
    filterByCategory(categoryId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/filter/category/${categoryId}`);
  }
  getMonthlySummaryFromTransactions(year: number, month: number): Observable<MonthlySummary>{
    return this.http.get<MonthlySummary>(`${this.apiUrl}/summary/monthly?year=${year}&month=${month}`)
  }
}
