import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Transaction {
  id: string;
  date: string;
  description: string;
  category: string;
  type: 'CREDIT' | 'DEBIT';
  amount: number;
  balance: number;
}

export interface TransactionFilter {
  type?: 'CREDIT' | 'DEBIT';
  startDate?: string;
  endDate?: string;
  search?: string;
  page?: number;
  size?: number;
}

export interface TransactionPage {
  content: Transaction[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getTransactions(filter: TransactionFilter = {}): Observable<TransactionPage> {
    let params = new HttpParams();
    
    if (filter.type) {
      params = params.append('type', filter.type);
    }
    if (filter.startDate) {
      params = params.append('startDate', filter.startDate);
    }
    if (filter.endDate) {
      params = params.append('endDate', filter.endDate);
    }
    if (filter.search) {
      params = params.append('search', filter.search);
    }
    
    params = params.append('page', filter.page?.toString() || '0');
    params = params.append('size', filter.size?.toString() || '10');

    return this.http.get<TransactionPage>(`${this.apiUrl}/transactions`, { params });
  }

  getRecentTransactions(limit: number = 5): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/transactions/recent?limit=${limit}`);
  }

  createTransaction(transaction: Omit<Transaction, 'id' | 'balance'>): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.apiUrl}/transactions`, transaction);
  }

  getTransactionById(id: string): Observable<Transaction> {
    return this.http.get<Transaction>(`${this.apiUrl}/transactions/${id}`);
  }
}
