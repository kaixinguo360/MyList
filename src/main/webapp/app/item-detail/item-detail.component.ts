import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';

import { of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { Item, ItemService } from '../service/item.service';
import { ItemEditDialogComponent } from '../item-edit/item-edit.component';

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

  constructor(
    private dialog: MatDialog
  ) { }

}

@Component({
  selector: 'app-item-detail',
  template: `
    <div class="icon-box">
      <button mat-raised-button
              class="round-btn"
              style="position:fixed; top:88px; right:24px;"
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

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private itemService: ItemService
  ) { }

  ngOnInit() {
    this.dialog.item = {
      id: 0,
      createdTime: 0,
      updatedTime: 0,
      info: '正在加载'
    };
    this.itemService.get(Number(this.route.snapshot.paramMap.get('id'))).pipe(
      tap(item => {
        this.dialog.item = item;
      }),
      catchError(err => {
        this.dialog.item = {
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

}

@Component({
  selector: 'app-item-detail-popup',
  template: `
    <div class="icon-box"
         style="position:absolute; top:24px; right:24px;">
      <button mat-raised-button
              class="round-btn"
              (click)="dialogService.closeAll()">
        <mat-icon>close</mat-icon>
      </button>
      <button mat-raised-button
              class="round-btn"
              (click)="dialog.edit()">
        <mat-icon>edit</mat-icon>
      </button>
      <button mat-raised-button
              class="round-btn"
              [routerLink]="['/item', items[index].id]"
              (click)="dialogService.closeAll()">
        <mat-icon>open_in_new</mat-icon>
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
  }

  next() {
    this.index = (this.index + 1 + this.items.length) % this.items.length;
    this.dialog.item = this.items[this.index];
  }

  keyDown($event) {
    switch ($event.key) {
      case 'ArrowLeft': this.previous(); $event.preventDefault(); break;
      case 'ArrowRight': this.next(); $event.preventDefault(); break;
    }
  }

  constructor(
    public dialogService: MatDialog
  ) { }

  ngOnInit() {
    this.dialog.item = this.items[this.index];
  }

}
