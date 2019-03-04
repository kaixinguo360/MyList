import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

export interface Message {
  timestamp: number,
  status: number,
  message: string
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private root = environment.apiRoot;
  private authService: AuthService;

  public setAuthService(authService: AuthService) {
    this.authService = authService;
  }

  public get<T>(path: string, params?: { [param: string]: string | string[] }, needAuth: boolean = true): Observable<T> {
    const headers = needAuth ? { Authorization: this.authService.getToken() } : null;
    return this.http.get<T>(this.root + path, {
      params: params,
      headers: headers
    });
  }

  public post<T>(path: string, body?: any, needAuth: boolean = true): Observable<T> {
    const headers = needAuth ? { Authorization: this.authService.getToken() } : null;
    return this.http.post<T>(this.root + path,  body, {
      headers: headers
    });
  }

  public put<T>(path: string, body?: any, needAuth: boolean = true): Observable<T> {
    const headers = needAuth ? { Authorization: this.authService.getToken() } : null;
    return this.http.put<T>(this.root + path,  body, {
      headers: headers
    });
  }

  public delete<T>(path: string, body?: any, needAuth: boolean = true): Observable<T> {
    const headers = needAuth ? { Authorization: this.authService.getToken() } : null;
    return this.http.request<T>('delete', this.root + path, {
      body: body,
      headers: headers
    });
  }

  constructor(
    private http: HttpClient
  ) { }
}
