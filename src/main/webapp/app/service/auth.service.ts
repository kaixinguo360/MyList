import { Injectable } from '@angular/core';

import { catchError, tap } from 'rxjs/operators';
import { Observable, Subject } from 'rxjs';

import { StorageService } from './storage.service';
import { ApiService, Message } from './api.service';

export interface Token {
  token: string,
  user: {
    id: number,
    name: string
  }
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public login(name: string, password: string): Observable<boolean> {
    const subject = new Subject<boolean>();
    this.apiService.post<Token>(`token?name=${name}&password=${password}`, null, false).pipe(
      tap(token => {
        this.storageService.set("token", token.token);
        subject.next(true)
      }),
      catchError(err => {
        subject.next(false);
        return err;
      })
    ).subscribe();
    return subject;
  }

  public logout(): Observable<boolean> {
    const subject = new Subject<boolean>();
    this.apiService.delete<Message>("token").pipe(
      tap(() => {
        this.storageService.set("token", null);
        subject.next(true)
      }),
      catchError(err => {
        subject.next(false);
        return err;
      })
    ).subscribe();
    return subject;
  }

  public isLogin(): boolean {
    return Boolean(this.getToken());
  }

  public getToken(): string {
    return this.storageService.get('token');
  }

  constructor(
    private storageService: StorageService,
    private apiService: ApiService
  ) {
    apiService.setAuthService(this);
  }
}
