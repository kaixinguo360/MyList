import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { catchError, tap } from 'rxjs/operators';
import { Observable, of } from 'rxjs';

import { ProxyService } from '../../service/proxy.service';
import { List, ListService } from '../../service/list.service';

@Component({
  selector: 'app-list-selector',
  templateUrl: './list-selector.component.html',
  styleUrls: ['./list-selector.component.css']
})
export class ListSelectorComponent implements OnInit {

  title: string;
  filter: (item: List, index: number, array: List[]) => boolean;
  lists: List[];
  showCreate = false;
  isCreating = false;

  static getList(dialog: MatDialog, title?: string, filter?: (item: List, index: number, array: List[]) => boolean): Observable<List> {
    const dialogRef: MatDialogRef<ListSelectorComponent> = dialog.open(
      ListSelectorComponent,
      {
        width: '300px',
        maxWidth: null,
        maxHeight: null,
        panelClass: 'flex-dialog',
      }
    );
    dialogRef.componentInstance.title = title;
    dialogRef.componentInstance.filter = filter;
    return dialogRef.afterClosed();
  }

  create(title: string) {
    if (title) {
      this.isCreating = true;
      this.listService.add({ title: title }).subscribe(list => {
        list.info = '新增列表';
        this.lists.unshift(list);
        this.showCreate = false;
        this.isCreating = false;
      });
    } else {
      alert('列表名称不能为空!');
    }
  }

  select(list: List) {
    this.dialogRef.close(list);
  }

  constructor(
    private dialog: MatDialog,
    private dialogRef: MatDialogRef<ListSelectorComponent>,
    private listService: ListService,
    private proxyService: ProxyService,
  ) { }

  ngOnInit() {
    this.listService.getAll().pipe(
      tap(lists => {
        lists.unshift({ id: 0, title: '默认列表' });
        this.lists = this.filter ? lists.filter(this.filter) : lists;
      }),
      catchError(err => {
        alert('获取列表信息时出错!');
        return of(err);
      })
    ).subscribe();
  }

}
