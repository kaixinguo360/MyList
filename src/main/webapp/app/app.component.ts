import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';

import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  public title = '';

  back() {
    window.stop();
    this.location.back();
  }

  logout() {
    this.authService.logout().pipe(
      tap(() => this.router.navigate([ '' ])),
      catchError(err => {
        alert('未知错误!');
        return of(err);
      })
    ).subscribe();
  }

  constructor(
    private location: Location,
    public router: Router,
    private authService: AuthService
  ) { }

  ngOnInit() { }
}
