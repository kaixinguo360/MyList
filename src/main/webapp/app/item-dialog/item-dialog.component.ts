import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

import { Item } from '../service/item.service';

@Component({
  selector: 'app-item-dialog',
  templateUrl: './item-dialog.component.html',
  styleUrls: ['./item-dialog.component.css']
})
export class ItemDialogComponent implements OnInit {

  items: Item[] = this.data.items;
  index: number = this.data.index;

  before() {
    this.index = (this.index - 1 + this.items.length) % this.items.length;
  }

  next() {
    this.index = (this.index + 1 + this.items.length) % this.items.length
  }

  keyDown($event) {
    switch ($event.key) {
      case 'ArrowLeft': this.before(); $event.preventDefault(); break;
      case 'ArrowRight': this.next(); $event.preventDefault(); break;
    }
  }
  
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<ItemDialogComponent>
  ) { }

  ngOnInit() {
  }

}
