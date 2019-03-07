import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog, MatDialogRef } from '@angular/material';

import { of, Subject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { List, ListService } from '../service/list.service';
import { Item, ItemService } from '../service/item.service';
import { ItemDetailDialogComponent } from '../item-detail/item-detail.component';

@Component({
  selector: 'app-list-detail',
  templateUrl: './list-detail.component.html',
  styleUrls: ['./list-detail.component.css']
})
export class ListDetailComponent implements OnInit {

  isLoading = true;

  list: List = { id: 0, createdTime: 0, updatedTime: 0, title: '' };
  items: Subject<Item[]> = new Subject<Item[]>();
  _items: Item[];
  cols = 1;

  openItemDialog(index: number) {
    const dialogRef: MatDialogRef<ItemDetailDialogComponent> = this.dialog.open(
      ItemDetailDialogComponent,
      {
        maxWidth: null,
        maxHeight: null,
        autoFocus: false,
        panelClass: 'flex-dialog',
        data: {
          items: this._items,
          index: index
        }
      }
    );
    dialogRef.componentInstance.items = this._items;
    dialogRef.componentInstance.index = index;
  }

  private getDomain(url: string) {
    const match = url.match(/:\/\/(www[0-9]?\.)?(.[^/:]+)/i);
    if (match != null && match.length > 2 && typeof match[2] === 'string' && match[2].length > 0) {
      return match[2];
    } else {
      return url;
    }
  }

  getTitle(item: Item): string {
    if (item.title) {
      return item.title;
    } else if (item.info || item.img) {
      return '';
    } else if (item.url) {
      return '来自' + this.getDomain(item.url) + '的收藏';
    } else {
      return '未命名收藏';
    }
  }

  getFooter(item: Item): string {
    if (item.img) {
      if (item.title) {
        return item.title;
      } else if (item.info) {
        return item.info;
      } else if (item.url) {
        return this.getDomain(item.url);
      } else {
        return '来自' + this.getDomain(item.img) + '的图片';
      }
    } else {
      if (item.url) {
        return this.getDomain(item.url);
      } else {
        return '';
      }
    }
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
            this.isLoading = false;
            this._items = items;
            this.items.next(items.sort((a, b) => b.updatedTime - a.updatedTime));
          }),
          catchError(err => {
            this.isLoading = false;
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
            this.isLoading = false;
            this._items = items;
            this.items.next(items.sort((a, b) => b.updatedTime - a.updatedTime));
          }),
          catchError(err => {
            this.isLoading = false;
            return of(err);
          })
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
