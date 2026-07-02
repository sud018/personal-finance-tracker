import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MonthlySummary {
  totalIncome: number;
  totalExpense: number;
  balance: number;
}
@Injectable({
  providedIn: 'root',
})
export class ReportService{
  private apiUrl = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) {}
    getMonthlySummary(month: number, year: number): Observable<MonthlySummary> {
    return this.http.get<MonthlySummary>(`${this.apiUrl}/monthly-summary?month=${month}&year=${year}`);
  }

    getCategoryBreakdown(month: number,year: number): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiUrl}/category-breakdown?month=${month}&year=${year}`);
  }

    getTrend(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/trend`);
  }

}
