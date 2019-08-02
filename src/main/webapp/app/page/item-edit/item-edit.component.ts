import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material';

import { catchError, tap } from 'rxjs/operators';
import { of, Subject } from 'rxjs';

import { StorageService } from '../../service/storage.service';
import { List, ListService } from '../../service/list.service';
import { Item, ItemService } from '../../service/item.service';
import { ProxyService } from '../../service/proxy.service';

@Component({
  selector: 'app-item-edit-dialog',
  templateUrl: './item-edit.component.html',
  styleUrls: ['./item-edit.component.css']
})
export class ItemEditDialogComponent implements OnInit {

  isLoading = true;

  public isNew = true;
  public itemId: number;

  lists: Subject<List[]> = new Subject<List[]>();
  itemData = this.fb.group({
    list: Number(this.storageService.get('defaultList', '0')),
    title: null,
    info: null,
    img: null,
    url: null
  });

  item: Item = {
    title: '',
    info: '',
    img: '',
    url: '',
    images: []
  };

  addImage() {
    if (!this.item.images) {
      this.item.images = [];
    }
    this.item.images.push({ url: '', info: '' });
  }

  update() {
    const itemData = this.itemData.getRawValue();
    this.item.list = itemData.list !== 0 ? {
      id: Number(itemData.list)
    } : null;
    this.item.title = itemData.title;
    this.item.info = itemData.info;
    this.item.img = itemData.img;
    this.item.url = itemData.url;
    this.item.images = this.item.images.filter(image => image.url || image.info);

    if (this.isNew) {
      this.item.id = null;
      this.itemService.add(this.item).pipe(
        tap(item => {
          // alert('添加成功!');
          if (item.list) {
            this.router.navigate([ '/list', item.list.id ], { replaceUrl: true });
          } else {
            this.router.navigate([ '/list/default' ], { replaceUrl: true });
          }
          this.storageService.set('defaultList', item.list ? item.list.id + '' : '0');
          this.dialog.closeAll();
        }),
        catchError(err => {
          alert('添加失败!');
          return of(err);
        })
      ).subscribe();
    } else {
      this.itemService.update(this.item).pipe(
        tap(() => {
          // alert('保存成功!');
          if (itemData.list) {
            this.router.navigate([ '/list', itemData.list ], { replaceUrl: true });
          } else {
            this.router.navigate([ '/list/default' ], { replaceUrl: true });
          }
          this.dialog.closeAll();
        }),
        catchError(err => {
          alert('保存失败!');
          alert(err);
          this.dialog.closeAll();
          return of(err);
        })
      ).subscribe();
    }
  }

  delete() {
    if (confirm('确定要删除' + (this.item.title ? `收藏'${this.item.title}'` : '这个收藏') + '吗?')) {
      this.itemService.delete(this.item.id).pipe(
        tap(() => {
          // alert('删除成功!');
          if (this.item.list) {
            this.router.navigate([ '/list', this.item.list.id ], { replaceUrl: true });
          } else {
            this.router.navigate([ '/list/default' ], { replaceUrl: true });
          }
          this.dialog.closeAll();
        }),
        catchError(err => {
          alert('删除失败!');
          this.dialog.closeAll();
          return of(err);
        })
      ).subscribe();
    }
  }

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private storageService: StorageService,
    private listService: ListService,
    private proxyService: ProxyService,
    private itemService: ItemService,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.listService.getAll().pipe(
      tap(list => {
        this.lists.next(list);
      }),
      catchError(err => of(err))
    ).subscribe();

    if (this.isNew) {
      this.isLoading = false;
    } else {
      this.itemService.get(this.itemId).pipe(
        tap(item => {
          this.isLoading = false;
          this.item = item;
          this.itemData.setValue({
            list: item.list ? item.list.id : 0,
            title: item.title,
            info: item.info,
            img: item.img,
            url: item.url
          });
        }),
        catchError(err => {
          this.isLoading = false;
          alert('获取项目信息时出错!');
          return of(err);
        })
      ).subscribe();
    }
  }

}


@Component({
  selector: 'app-item-edit',
  template: `
    <div class="medium-card">
      <mat-card>
        <app-item-edit-dialog #dialog></app-item-edit-dialog>
      </mat-card>
    </div>
  `
})
export class ItemEditComponent implements OnInit {

  @ViewChild('dialog')
  dialog: ItemEditDialogComponent;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private storageService: StorageService,
  ) { }

  ngOnInit(): void {
    if (this.router.url === '/item/new') {
      this.dialog.isNew = true;
    } else if (this.router.url === '/item/fromPage') {
      this.dialog.isNew = true;
      const tmpItemStr = this.storageService.get('tmpItem');
      if (tmpItemStr) {
        const item = JSON.parse(tmpItemStr);
        this.dialog.item = item;
        this.dialog.itemData.setValue({
          list: item.list ? item.list.id : 0,
          title: item.title ? item.title : '',
          info: item.info ? item.info : '',
          img: item.img ? item.img : '',
          url: item.url ? item.url : ''
        });
        this.storageService.set('tmpItem', null);
      }
    } else {
      this.dialog.itemId = Number(this.route.snapshot.paramMap.get('id'));
    }
  }
}
