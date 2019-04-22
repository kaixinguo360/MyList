import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';

import { Observable, of, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { ProxyService } from '../service/proxy.service';
import { Item, ItemService } from '../service/item.service';
import { ItemEditDialogComponent } from '../item-edit/item-edit.component';
import { Message } from '../service/api.service';

@Component({
  selector: 'app-item-detail-dialog',
  templateUrl: './item-detail.component.html',
  styleUrls: ['./item-detail.component.css']
})
export class ItemDetailDialogComponent {

  public item: Item;

  getDomain(url: string) {
    const match = url.match(/:\/\/(www[0-9]?\.)?(.[^/:]+)/i);
    if (match != null && match.length > 2 && typeof match[2] === 'string' && match[2].length > 0) {
      return match[2];
    } else {
      return url;
    }
  }

  edit() {
    this.dialog.closeAll();
    const dialogRef: MatDialogRef<ItemEditDialogComponent> = this.dialog.open(
      ItemEditDialogComponent,
      {
        maxWidth: null,
        maxHeight: null,
        autoFocus: false,
        closeOnNavigation: true,
        panelClass: 'medium-dialog'
      }
    );
    dialogRef.componentInstance.isNew = false;
    dialogRef.componentInstance.itemId = this.item.id;
  }

  delete(): Observable<Message> {
    if (confirm('确定要删除' + (this.item.title ? `收藏'${this.item.title}'` : '这个收藏') + '吗?')) {
      return this.itemService.delete(this.item.id);
    } else {
      return throwError('cancel');
    }
  }

  public updateItem() {
    this.itemService.get(this.item.id).pipe(
      tap(item => {
        this.item = item;
      }),
      catchError(err => {
        this.item = {
          id: 0,
          createdTime: 0,
          updatedTime: 0,
          info: '加载失败'
        };
        alert('获取项目信息时出错!');
        return of(err);
      })
    ).subscribe();
  }
  
  constructor(
    private dialog: MatDialog,
    private proxyService: ProxyService,
    private itemService: ItemService
  ) { }

}

@Component({
  selector: 'app-item-detail',
  template: `
    <div class="icon-box"
         style="position:fixed; bottom:24px; right:24px;">
      <button mat-raised-button
              class="round-btn"
              (click)="delete()">
        <mat-icon>delete</mat-icon>
      </button>
      <button mat-raised-button
              class="round-btn"
              (click)="dialog.edit()">
        <mat-icon>edit</mat-icon>
      </button>
    </div>
    <div class="medium-card">
      <mat-card>
        <app-item-detail-dialog #dialog></app-item-detail-dialog>
      </mat-card>
    </div>
  `,
  styleUrls: ['./item-detail.component.css']
})
export class ItemDetailComponent implements OnInit {

  @ViewChild('dialog')
  dialog: ItemDetailDialogComponent;

  delete() {
    this.dialog.delete().pipe(
      tap(() => {
        if (this.dialog.item.list) {
          this.router.navigate([ '/list', this.dialog.item.list.id ], { replaceUrl: true });
        } else {
          this.router.navigate([ '/list/default' ], { replaceUrl: true });
        }
      }),
      catchError(err => {
        if (err !== 'cancel') {
          alert('删除失败!');
        }
        return of(err);
      })
    ).subscribe();
  }

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.dialog.item = {
      id: Number(this.route.snapshot.paramMap.get('id')),
      createdTime: 0,
      updatedTime: 0,
      info: '正在加载'
    };
    this.dialog.updateItem();
  }

}

@Component({
  selector: 'app-item-detail-popup',
  template: `
    <div class="icon-box" style="position:absolute; bottom:82px; right:24px;">
      <button mat-raised-button
              class="round-btn"
              (click)="dialogService.closeAll()">
        <mat-icon>close</mat-icon>
      </button>
      <a target="_blank" [routerLink]="['/item', items[index].id]">
        <button mat-raised-button
                class="round-btn">
          <mat-icon>open_in_new</mat-icon>
        </button>
      </a>
      <button mat-raised-button
              class="round-btn"
              (click)="delete()">
        <mat-icon>delete</mat-icon>
      </button>
      <button mat-raised-button
              class="round-btn"
              (click)="dialog.edit()">
        <mat-icon>edit</mat-icon>
      </button>
    </div>
    <app-item-detail-dialog #dialog></app-item-detail-dialog>
    <div class="dialog-actions" (window:keydown)="keyDown($event)" *ngIf="items">
      <button mat-raised-button class="dialog-button" (click)="previous()">
        <mat-icon>navigate_before</mat-icon>
      </button>
      <button mat-raised-button class="dialog-button" mat-dialog-close>{{index}}</button>
      <button mat-raised-button class="dialog-button" (click)="next()">
        <mat-icon>navigate_next</mat-icon>
      </button>
    </div>
  `,
  styleUrls: ['./item-detail.component.css']
})
export class ItemDetailPopupComponent implements OnInit {

  public items: Item[];
  public index: number;

  @ViewChild('dialog')
  dialog: ItemDetailDialogComponent;

  previous() {
    this.index = (this.index - 1 + this.items.length) % this.items.length;
    this.dialog.item = this.items[this.index];
    this.dialog.updateItem();
  }

  next() {
    this.index = (this.index + 1 + this.items.length) % this.items.length;
    this.dialog.item = this.items[this.index];
    this.dialog.updateItem();
  }

  keyDown($event) {
    switch ($event.key) {
      case 'ArrowLeft': this.previous(); $event.preventDefault(); break;
      case 'ArrowRight': this.next(); $event.preventDefault(); break;
    }
  }

  delete() {
    this.dialog.delete().pipe(
      tap(() => {
        this.dialogService.closeAll();
      }),
      catchError(err => {
        if (err !== 'cancel') {
          alert('删除失败!');
        }
        return of(err);
      })
    ).subscribe();
  }

  constructor(
    public dialogService: MatDialog
  ) { }

  ngOnInit() {
    this.dialog.item = this.items[this.index];
    this.dialog.updateItem();
  }

}
