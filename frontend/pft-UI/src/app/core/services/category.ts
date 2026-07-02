import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
export interface Category{
  id?: number;
  name: string;
  color: string;
  icon: string;
  type: string;
}
@Injectable({
  providedIn: 'root',
})
export class CategoryService {
private apiUrl = `${environment.apiUrl}/categories`;

constructor(private http: HttpClient){};
createCategory(category: Category): Observable<Category>{
  return this.http.post<Category>(this.apiUrl, category);
}
getCategories(): Observable<Category[]>{
  return this.http.get<Category[]>(this.apiUrl);
}
updateCategory(id: number, category: Category): Observable<Category>{
  return this.http.put<Category>(`${this.apiUrl}/${id}`,category);
}
deleteCategory(id: number): Observable<string>{
  return this.http.delete(`${this.apiUrl}/${id}`, {responseType: 'text'});
}

}
