import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ExportService {
    private apiUrl = `${environment.apiUrl}/export`;

  constructor(private http: HttpClient) {}

    downloadCSV(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/transactions/csv`, { responseType: 'blob' });
  }
}
