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
    const list = this.listData.getRawValue();
    if (this.isNew) {
      this.listService.add({
        title: list.title,
        info: list.info,
        img: list.img
      }).pipe(
        tap(list => {
          alert('添加成功!');
          this.router.navigate([ '/list', list.id ], { replaceUrl:true });
        }),
        catchError(err => {
          alert('添加失败!');
          return of(err);
        })
      ).subscribe();
    } else {
      this.list.title = list.title;
      this.list.info = list.info;
      this.list.img = list.img;
      this.listService.update(this.list).pipe(
        tap(() => {
          alert('保存成功!');
          window.history.go(-1);
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
          alert('删除成功!');
          window.history.go(-2);
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
    this.isNew = this.router.url == '/list/new';
    if (!this.isNew) {
      const listId = Number(this.route.snapshot.paramMap.get('id'));
      this.listService.get(listId).pipe(
        tap(list => {
          this.list = list;
          this.listData.setValue({
            title: list.title,
            info: list.info,
            img: list.img
          });
        }),
        catchError(err => {
          alert('获取列表信息时出错!');
          return of(err);
        })
      ).subscribe();
    }
  }

}
