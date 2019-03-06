import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material';

import { of, Subject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { List, ListService } from '../service/list.service';
import { Item, ItemService } from '../service/item.service';
import { ItemDialogComponent } from '../item-dialog/item-dialog.component';

@Component({
  selector: 'app-list-detail',
  templateUrl: './list-detail.component.html',
  styleUrls: ['./list-detail.component.css']
})
export class ListDetailComponent implements OnInit {

  list: List = { id: 0, createdTime: 0, updatedTime: 0, title: '' };
  items: Subject<Item[]> = new Subject<Item[]>();
  _items: Item[];
  cols = 1;

  openItemDialog(index: number) {
    this.dialog.open(
      ItemDialogComponent,
      {
        maxHeight: '90vh',
        autoFocus: false,
        data: {
          items: this._items,
          index: index
        }
      }
    );
  }

  constructor(
    private route: ActivatedRoute,
    private breakpointObserver: BreakpointObserver,
    private dialog: MatDialog,
    private listService: ListService,
    private itemService: ItemService
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      if (params.get('id') === 'default') {
        this.list = {
          id: 0,
          createdTime: 0,
          updatedTime: 0,
          title: '默认列表'
        };
        this.itemService.getAll('list:none').pipe(
          tap(items => {
            this._items = items;
            this.items.next(items.sort((a, b) => b.updatedTime - a.updatedTime));
          }),
          catchError(err => {
            alert('获取项目时出错!');
            return of(err);
          })
        ).subscribe();
      } else {
        this.listService.get(Number(params.get('id'))).pipe(
          tap(list => this.list = list),
          catchError(err => {
            if (err instanceof HttpErrorResponse && err.status === 404) {
              alert('列表不存在!');
              window.history.go(-1);
            } else {
              alert('获取项目时出错!');
            }
            return of(err);
          })
        ).subscribe();
        this.listService.getItems(Number(params.get('id'))).pipe(
          tap(items => {
            this._items = items;
            this.items.next(items.sort((a, b) => b.updatedTime - a.updatedTime));
          }),
          catchError(err => of(err))
        ).subscribe();
      }
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
