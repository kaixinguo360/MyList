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
export class ItemDetailDialogComponent implements OnInit {

  public item: Item;
  public items: Item[];
  public index: number;

  getDomain(url: string) {
    const match = url.match(/:\/\/(www[0-9]?\.)?(.[^/:]+)/i);
    if (match != null && match.length > 2 && typeof match[2] === 'string' && match[2].length > 0) {
      return match[2];
    } else {
      return url;
    }
  }

  before() {
    this.index = (this.index - 1 + this.items.length) % this.items.length;
    this.item = this.items[this.index];
  }

  next() {
    this.index = (this.index + 1 + this.items.length) % this.items.length;
    this.item = this.items[this.index];
  }

  keyDown($event) {
    switch ($event.key) {
      case 'ArrowLeft': this.before(); $event.preventDefault(); break;
      case 'ArrowRight': this.next(); $event.preventDefault(); break;
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

  ngOnInit() {
    if (this.items) {
      this.item = this.items[this.index ? this.index : 0];
    }
  }

}

@Component({
  selector: 'app-item-detail',
  template: `
    <div class="large-card">
      <mat-card>
        <app-item-detail-dialog #dialog></app-item-detail-dialog>
      </mat-card>
    </div>
  `
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
