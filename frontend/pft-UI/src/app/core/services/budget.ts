import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
export interface Budget{
  id: number,
  amount: number,
  month: number,
  year: number,
  categoryId: number,
  categoryName: string
}
@Injectable({
  providedIn: 'root',
})
export class BudgetService {
private apiUrl = `${environment.apiUrl}/budgets`;
constructor(private http: HttpClient){};

createBudget(budget: Budget): Observable<Budget>{
  return this.http.post<Budget>(this.apiUrl, budget);
}
getBudget(month: number, year: number): Observable<Budget[]>{
  return this.http.get<Budget[]>(`${this.apiUrl}?month=${month}&year=${year}`);
}
updateBudget(id: number, budget: Budget): Observable<Budget>{
  return this.http.put<Budget>(`${this.apiUrl}/${id}`, budget);
}

}
