import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog, MatDialogRef } from '@angular/material';

import { of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { NgxMasonryComponent, NgxMasonryOptions } from 'ngx-masonry';

import { environment } from '../../../environments/environment';
import { AppComponent } from '../../app.component';
import { StorageService } from '../../service/storage.service';
import { ProxyService } from '../../service/proxy.service';
import { List, ListService } from '../../service/list.service';
import { Item, ItemService } from '../../service/item.service';
import { ItemDetailPopupComponent } from '../item-detail/item-detail.component';
import { ItemEditDialogComponent } from '../item-edit/item-edit.component';
import { ListSelectorComponent } from '../../com/list-selector/list-selector.component';

interface SelectableItem extends Item {
  selected?: boolean;
}

@Component({
  selector: 'app-list-detail',
  templateUrl: './list-detail.component.html',
  styleUrls: ['./list-detail.component.css']
})
export class ListDetailComponent implements OnInit {

  isLoading = true;
  isMobile = false;
  column = 1;

  list: List = { id: 0, createdTime: 0, updatedTime: 0, title: '' };
  loadedItems: SelectableItem[] = [];
  items: SelectableItem[];

  @ViewChild('masonry')
  masonry: NgxMasonryComponent;
  masonryWidth: number;
  columnWidth: number;
  masonryOptions: NgxMasonryOptions = {
    transitionDuration: '0.2s',
    gutter: 0,
    resize: false,
    initLayout: true
  };

  selectMode = false;

  openItemPopup(index: number) {
    const dialogRef: MatDialogRef<ItemDetailPopupComponent> = this.dialog.open(
      ItemDetailPopupComponent,
      {
        maxWidth: null,
        maxHeight: null,
        autoFocus: false,
        panelClass: 'flex-dialog'
      }
    );
    dialogRef.componentInstance.items = this.items;
    dialogRef.componentInstance.index = index;
  }

  openAddItemPopup() {
    this.storageService.set('defaultList', this.list.id === 0 ? null : this.list.id + '');
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

  loadMoreItems() {
    let num;

    if (this.loadedItems.length === 0) {
      num = this.column * 8;
    } else {
      num = this.column * 4;
    }

    const left = this.items.length - this.loadedItems.length;
    num = (left < num) ? left : num;

    if (num !== 0) {
      for (let i = 0; i < num; i++) {
        this.loadedItems.push(this.items[this.loadedItems.length]);
      }
    }

    this.updateLayout();
  }

  toggleSelectMode() {
    this.selectMode = !this.selectMode;
    if (!this.selectMode) {
      this.items.forEach(item => item.selected = false);
    }
  }

  selectAll() {
    if (this.items.find(i => !i.selected)) {
      this.items.forEach(item => item.selected = true);
    } else {
      this.items.forEach(item => item.selected = false);
    }
  }

  deleteSelected() {
    const selectedItems = this.items.filter(i => i.selected);
    if (selectedItems.length && confirm('确定要删除这些收藏吗?')) {
      this.selectMode = !this.selectMode;
      this.itemService.deleteAll(selectedItems).pipe(
        tap(() => {
          this.loadedItems = this.loadedItems.filter(i => !i.selected);
          this.items = this.items.filter(i => !i.selected);
        }),
        catchError(err => {
          alert('删除失败!');
          this.items.forEach(item => item.selected = false);
          return of(err);
        })
      ).subscribe();
    } else {
      this.toggleSelectMode();
    }
  }

  moveSelected() {
    const selectedItems = this.items.filter(i => i.selected);
    if (selectedItems.length) {
      ListSelectorComponent.getList(this.dialog,
        `移动${selectedItems.length}个项目到指定列表`,
        l => l.id !== this.list.id
      ).subscribe(list => {
        if (list) {
          this.selectMode = !this.selectMode;
          this.listService.addItems(list.id, selectedItems).pipe(
            tap(() => {
              this.loadedItems = this.loadedItems.filter(i => !i.selected);
              this.items = this.items.filter(i => !i.selected);
            }),
            catchError(err => {
              alert('移动失败!');
              this.items.forEach(item => item.selected = false);
              return of(err);
            })
          ).subscribe();
        }
      });
    }
  }

  createList() {

  }

  updateLayout() {
    this.masonry.layout();
  }

  private changeItems(items: Item[]) {
    if (this.isLoading) {
      this.isLoading = false;
      this.items = items;
      this.loadMoreItems();
    } else {
      this.items = items;
      const loadedNum = this.loadedItems.length;
      this.loadedItems.length = 0;
      for (let i = 0; i < loadedNum; i++) {
        this.loadedItems.push(this.items[this.loadedItems.length]);
      }
      this.items = items;
    }
  }

  private updateData(id: number) {
    if (this.list && this.list.id !== id) {
      this.isLoading = true;
      this.loadedItems.length = 0;
    }
    if (id === null) {
      this.list = {
        id: 0,
        createdTime: 0,
        updatedTime: 0,
        title: '默认列表'
      };
      this.itemService.getAll('list:none').pipe(
        tap(items => {
          this.changeItems(items);
        }),
        catchError(err => {
          this.isLoading = false;
          alert('获取项目时出错!');
          return of(err);
        })
      ).subscribe();
    } else {
      this.listService.get(id).pipe(
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
      this.listService.getItems(id).pipe(
        tap(items => {
          this.changeItems(items);
        }),
        catchError(err => {
          this.isLoading = false;
          return of(err);
        })
      ).subscribe();
    }
  }

  @HostListener('window:resize')
  private resize() {
    if (this.isMobile) {
      this.column = Number(this.storageService.get('mobileColumn', '2'));
      this.columnWidth = (window.innerWidth - 18) / this.column;
      this.masonryWidth = (this.column === 0) ? this.columnWidth : this.column * this.columnWidth;
    } else {
      this.column = Math.round(window.innerWidth / this.columnWidth) - 1;
      this.columnWidth = environment.defaultColumnWidth;
      this.masonryWidth = (this.column === 0) ? this.columnWidth : this.column * this.columnWidth;
    }
    this.updateLayout();
  }

  constructor(
    private route: ActivatedRoute,
    private breakpointObserver: BreakpointObserver,
    private dialog: MatDialog,
    private storageService: StorageService,
    private proxyService: ProxyService,
    private listService: ListService,
    private itemService: ItemService,
    public appComponent: AppComponent
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      const id = params.get('id');
      this.updateData(id === 'default' ? null : Number(id));
    });

    this.itemService.onUpdate.subscribe(event => {
      if (event.type !== 'deleteAll') {
        if (
          (event.item.list && event.item.list.id === this.list.id) || (!event.item.list && this.list.id === 0) ||
          this.items.find(i => i.id === event.item.id)
        ) {
          if (event.type === 'add') {
            // Add
            this.updateData(this.list.id === 0 ? null : this.list.id);
          } else if (event.type === 'update') {
            // Update
            this.loadedItems.forEach(item => {
              if (item.id === event.item.id) {
                item.title = event.item.title;
                item.info = event.item.info;
                item.img = event.item.img;
                item.url = event.item.url;
              }
            });
            this.items.forEach(item => {
              if (item.id === event.item.id) {
                item.title = event.item.title;
                item.info = event.item.info;
                item.img = event.item.img;
                item.url = event.item.url;
              }
            });
          } else if (event.type === 'delete') {
            // Delete
            this.loadedItems = this.loadedItems.filter(item => item.id !== event.item.id);
            this.items = this.items.filter(item => item.id !== event.item.id);
          }
        }
      }
    });

    this.breakpointObserver.observe(Breakpoints.XSmall).pipe(
      tap(({ matches }) => {
        this.isMobile = matches;
        this.resize();
      })
    ).subscribe();

    this.resize();
  }
}
