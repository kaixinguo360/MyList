import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef, MatListOption} from '@angular/material';

import {catchError, tap} from 'rxjs/operators';
import {Observable, of} from 'rxjs';

import {Tag, TagService} from '../../service/tag.service';

@Component({
  selector: 'app-tag-selector',
  templateUrl: './tag-selector.component.html',
  styleUrls: ['./tag-selector.component.css']
})
export class TagSelectorComponent implements OnInit {

  title: string;
  filter: (item: Tag, index: number, array: Tag[]) => boolean;
  hasClear = false;
  tags: Tag[];
  showCreate = false;
  isCreating = false;

  static getTags(dialog: MatDialog,
                 title?: string, filter?: (item: Tag, index: number, array: Tag[]) => boolean,
                 hasClear = true):
    Observable<{ tags: Tag[], isClear: boolean }> {
    const dialogRef: MatDialogRef<TagSelectorComponent> = dialog.open(
      TagSelectorComponent,
      {
        width: '300px',
        maxWidth: null,
        maxHeight: null,
        panelClass: 'flex-dialog',
      }
    );
    dialogRef.componentInstance.title = title;
    dialogRef.componentInstance.filter = filter;
    dialogRef.componentInstance.hasClear = hasClear;
    return dialogRef.afterClosed();
  }

  create(title: string) {
    if (title) {
      this.isCreating = true;
      this.tagService.add({ title: title }).subscribe(tag => {
        tag.info = '新增标签';
        this.tags.unshift(tag);
        this.showCreate = false;
        this.isCreating = false;
      });
    } else {
      alert('标签名称不能为空!');
    }
  }

  select(options: MatListOption[]) {
    const tags = options.map(o => o.value);
    let isClear = false;
    if (tags[0] === 'clear') {
      isClear = true;
      tags.shift();
    }
    this.dialogRef.close({
      tags: tags,
      isClear: isClear
    });
  }

  constructor(
    private dialog: MatDialog,
    private dialogRef: MatDialogRef<TagSelectorComponent>,
    private tagService: TagService
  ) { }

  ngOnInit() {
    this.tagService.getAll().pipe(
      tap(tags => {
        this.tags = this.filter ? tags.filter(this.filter) : tags;
      }),
      catchError(err => {
        alert('获取标签信息时出错!');
        return of(err);
      })
    ).subscribe();
  }

}
