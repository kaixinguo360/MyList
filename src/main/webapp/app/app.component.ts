import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material';

import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';

import { Order } from './service/order.service';
import { AuthService } from './service/auth.service';
import { StorageService } from './service/storage.service';
import { ItemEditDialogComponent } from './item-edit/item-edit.component';

class OrderMenuItem {
  title: string;
  isSelected: boolean;
  order: Order;
  icon: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public title = '';
  public orders: OrderMenuItem[];

  back() {
    window.stop();
    this.location.back();
  }

  addItem() {
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
    dialogRef.componentInstance.isNew = true;
  }

  changeOrder(order: Order) {
    this.storageService.set('order', order);
    location.reload();
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
    private dialog: MatDialog,
    private authService: AuthService,
    private storageService: StorageService
  ) {
    const currentOrder = storageService.get('order', Order.UPDATE_ASC);
    this.orders  = [
      { title: '↓ 更新时间', icon: 'access_time', isSelected: (currentOrder === Order.UPDATE_ASC) , order: Order.UPDATE_ASC },
      { title: '↑ 更新时间', icon: 'access_time', isSelected: (currentOrder === Order.UPDATE_DESC) , order: Order.UPDATE_DESC },
      { title: '↓ 创建时间', icon: 'create_new_folder', isSelected: (currentOrder === Order.CREATE_ASC) , order: Order.CREATE_ASC },
      { title: '↑ 创建时间', icon: 'create_new_folder', isSelected: (currentOrder === Order.CREATE_DESC) , order: Order.CREATE_DESC },
      { title: '↓ 名称', icon: 'sort_by_alpha', isSelected: (currentOrder === Order.NAME_ASC) , order: Order.NAME_ASC },
      { title: '↑ 名称', icon: 'sort_by_alpha', isSelected: (currentOrder === Order.NAME_DESC) , order: Order.NAME_DESC },
      { title: '随机', icon: 'blur_on', isSelected: (currentOrder === Order.RANDOM) , order: Order.RANDOM }
    ];
  }

  ngOnInit() { }
}
