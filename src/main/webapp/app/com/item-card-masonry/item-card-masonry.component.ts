import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { NgxMasonryComponent, NgxMasonryOptions } from 'ngx-masonry';

import { environment } from '../../../environments/environment';
import { StorageService } from '../../service/storage.service';
import { Item } from '../../service/item.service';
import { ItemDetailPopupComponent } from '../../page/item-detail/item-detail.component';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { tap } from 'rxjs/operators';

interface SelectableItem extends Item {
  selected?: boolean;
}

@Component({
  selector: 'app-item-card-masonry',
  templateUrl: './item-card-masonry.component.html',
  styleUrls: ['./item-card-masonry.component.css']
})
export class ItemCardMasonryComponent implements OnInit {

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

  items: SelectableItem[];
  loadedItems: SelectableItem[] = [];
  selectMode = false;
  isMobile = false;
  column = 1;

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

  @HostListener('window:resize')
  resize() {
    if (this.isMobile) {
      this.column = Number(this.storageService.get('mobileColumn', '2'));
      this.columnWidth = (window.innerWidth - 18) / this.column;
      this.masonryWidth = (this.column === 0) ? this.columnWidth : this.column * this.columnWidth;
    } else {
      this.column = Math.round(window.innerWidth / this.columnWidth) - 1;
      this.columnWidth = environment.defaultColumnWidth;
      this.masonryWidth = (this.column === 0) ? this.columnWidth : this.column * this.columnWidth;
    }
    this.masonry.layout();
  }

  public enableSelectMode(mode: boolean) {
    this.selectMode = mode;
    if (!mode) {
      this.items.forEach(item => item.selected = false);
    }
  }

  public selectAll() {
    if (this.items.find(i => !i.selected)) {
      this.items.forEach(item => item.selected = true);
    } else {
      this.items.forEach(item => item.selected = false);
    }
  }

  public getSelectedItems(): Item[] {
    return this.items.filter(i => i.selected);
  }

  public loadMoreItems() {
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

    this.masonry.layout();
  }

  public changeItems(items: Item[]) {
    this.items = items;
    if (this.loadedItems.length === 0) {
      this.loadMoreItems();
    } else {
      const loaded = this.loadedItems.length < items.length ? this.loadedItems.length : items.length;
      this.loadedItems.length = 0;
      for (let i = 0; i < loaded; i++) {
        this.loadedItems.push(this.items[this.loadedItems.length]);
      }
    }
    this.resize();
  }

  constructor(
    private dialog: MatDialog,
    private breakpointObserver: BreakpointObserver,
    private storageService: StorageService
  ) { }

  ngOnInit() {
    this.breakpointObserver.observe(Breakpoints.XSmall).pipe(
      tap(({ matches }) => {
        this.isMobile = matches;
        this.resize();
      })
    ).subscribe();

    this.resize();
  }
}
