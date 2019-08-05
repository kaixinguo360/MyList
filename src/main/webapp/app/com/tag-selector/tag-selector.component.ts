import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef, MatListOption} from '@angular/material';

import {catchError, tap} from 'rxjs/operators';
import {Observable, of} from 'rxjs';

import {Tag, TagService} from '../../service/tag.service';

interface TagItem extends Tag {
  isNew?: boolean;
}

@Component({
  selector: 'app-tag-selector',
  templateUrl: './tag-selector.component.html',
  styleUrls: ['./tag-selector.component.css']
})
export class TagSelectorComponent implements OnInit {

  title: string;
  filter: (item: Tag, index: number, array: Tag[]) => boolean;
  hasClear = false;
  tags: TagItem[];
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
        this.tags.unshift(tag);
        this.tags[0].isNew = true;
        this.showCreate = false;
        this.isCreating = false;
      });
    } else {
      alert('标签名称不能为空!');
    }
  }

  delete(tag: Tag) {
    if (confirm(`确定要删除'${tag.title}'标签吗?`)) {
      this.tagService.delete(tag.id).pipe(
        tap(() => {
          this.tags = this.tags.filter(t => t.id !== tag.id);
        }),
        catchError(err => {
          alert('删除失败!');
          return of(err);
        })
      ).subscribe();
    }
  }

  edit(tag: Tag) {
    const newName = prompt(`重命名'${tag.title}'标签: `);
    if (newName) {
      tag.title = newName;
      this.tagService.update(tag).pipe(
        catchError(err => {
          alert('重命名失败!');
          return of(err);
        })
      ).subscribe();
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
