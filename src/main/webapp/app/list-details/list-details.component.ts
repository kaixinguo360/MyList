import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';

import { of, Subject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { List, ListService } from '../service/listservice';
import { Item } from '../service/item.service';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';

@Component({
  selector: 'app-list-details',
  templateUrl: './list-details.component.html',
  styleUrls: ['./list-details.component.css']
})
export class ListDetailsComponent implements OnInit {

  list: List = { id: 0, createdTime: 0, updatedTime: 0, title: '' };
  items: Subject<Item[]> = new Subject<Item[]>();
  cols = 1;

  constructor(
    private route: ActivatedRoute,
    private breakpointObserver: BreakpointObserver,
    private listService: ListService
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.listService.get(Number(params.get('id'))).pipe(
        tap(list => this.list = list),
        catchError(err => of(err))
      ).subscribe();
      this.listService.getItems(Number(params.get('id'))).pipe(
        tap(items => this.items.next(items)),
        catchError(err => {
          alert('获取项目时出错!');
          return of(err);
        })
      ).subscribe();
    });


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
