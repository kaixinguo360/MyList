import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';

import { List, ListService } from '../service/list.service';

@Component({
  selector: 'app-list-edit',
  templateUrl: './list-edit.component.html',
  styleUrls: ['./list-edit.component.css']
})
export class ListEditComponent implements OnInit {

  isLoading = true;

  listData = this.fb.group({
    title: [null, Validators.required],
    info: [null],
    img: [null],
  });
  isNew = true;
  list: List;

  update() {
    if (this.listData.invalid) {
      alert('数据不符合要求!');
      return;
    }
    const listData = this.listData.getRawValue();
    if (this.isNew) {
      this.listService.add({
        title: listData.title,
        info: listData.info,
        img: listData.img
      }).pipe(
        tap(list => {
          // alert('添加成功!');
          this.router.navigate([ '/list', list.id ], { replaceUrl: true });
        }),
        catchError(err => {
          alert('添加失败!');
          return of(err);
        })
      ).subscribe();
    } else {
      this.list.title = listData.title;
      this.list.info = listData.info;
      this.list.img = listData.img;
      this.listService.update(this.list).pipe(
        tap(list => {
          // alert('保存成功!');
          this.router.navigate([ '/list', list.id ], { replaceUrl: true });
        }),
        catchError(err => {
          alert('保存失败!');
          return of(err);
        })
      ).subscribe();
    }
  }

  delete() {
    if (confirm(`确定要删除列表'${this.list.title}'吗?`)) {
      this.listService.delete(this.list.id).pipe(
        tap(() => {
          // alert('删除成功!');
          this.router.navigate([ '/list' ], { replaceUrl: true });
        }),
        catchError(err => {
          alert('删除失败!');
          return of(err);
        })
      ).subscribe();
    }
  }

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private listService: ListService
  ) { }

  ngOnInit() {
    this.isNew = this.router.url === '/list/new';
    if (this.isNew) {
      this.isLoading = false;
    } else {
      const listId = Number(this.route.snapshot.paramMap.get('id'));
      this.listService.get(listId).pipe(
        tap(list => {
          this.isLoading = false;
          this.list = list;
          this.listData.setValue({
            title: list.title,
            info: list.info,
            img: list.img
          });
        }),
        catchError(err => {
          this.isLoading = false;
          alert('获取列表信息时出错!');
          return of(err);
        })
      ).subscribe();
    }
  }

}
