import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';

import { catchError, tap } from 'rxjs/operators';
import { of, Subject } from 'rxjs';

import { List, ListService } from '../service/list.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {

  isLoading = true;

  lists: Subject<List[]> = new Subject<List[]>();
  cols = 1;

  constructor(
    private listService: ListService,
    private breakpointObserver: BreakpointObserver
  ) { }

  ngOnInit() {
    this.listService.getAll().pipe(
      tap(lists => {
        this.isLoading = false;
        this.lists.next(lists.sort((a, b) => b.updatedTime - a.updatedTime));
      }),
      catchError(err => {
        this.isLoading = false;
        alert('获取列表时出错!');
        return of(err);
      })
    ).subscribe();

    this.breakpointObserver.observe([
      Breakpoints.XSmall
    ]).pipe(
      tap(({ matches }) => {
        if (matches) {
          this.cols = 1;
        }
      })
    ).subscribe();
    this.breakpointObserver.observe([
      Breakpoints.Small,
      Breakpoints.Medium
    ]).pipe(
      tap(({ matches }) => {
        if (matches) {
          this.cols = 2;
        }
      })
    ).subscribe();
    this.breakpointObserver.observe([
      Breakpoints.Large
    ]).pipe(
      tap(({ matches }) => {
        if (matches) {
          this.cols = 3;
        }
      })
    ).subscribe();
  }

}
