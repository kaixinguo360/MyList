import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material';

import { of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { AppComponent } from '../../app.component';
import { StorageService } from '../../service/storage.service';
import { ProxyService } from '../../service/proxy.service';
import { ItemCardMasonryComponent } from '../../com/item-card-masonry/item-card-masonry.component';
import { ListSelectorComponent } from '../../com/list-selector/list-selector.component';
import { TagSelectorComponent } from '../../com/tag-selector/tag-selector.component';
import { List, ListService } from '../../service/list.service';
import { TagService } from '../../service/tag.service';
import { ItemService } from '../../service/item.service';

@Component({
  selector: 'app-list-detail',
  templateUrl: './list-detail.component.html',
  styleUrls: ['./list-detail.component.css']
})
export class ListDetailComponent implements OnInit {

  isLoading = true;
  list: List;

  @ViewChild('masonry') masonry: ItemCardMasonryComponent;

  toggleSelectMode() {
    this.masonry.enableSelectMode(!this.masonry.selectMode);
  }

  deleteSelected() {
    const selectedItems = this.masonry.getSelectedItems();
    if (selectedItems.length && confirm('确定要删除这些收藏吗?')) {
      this.masonry.enableSelectMode(false);
      this.itemService.deleteAll(selectedItems).pipe(
        catchError(err => {
          alert('删除失败!');
          return of(err);
        })
      ).subscribe();
    }
  }

  tagSelected() {
    const selectedItems = this.masonry.getSelectedItems();
    if (selectedItems.length) {
      TagSelectorComponent.getTags(this.dialog,
        `为${selectedItems.length}个项目指定标签`
      ).subscribe(tag => {
        if (tag) {
          this.masonry.enableSelectMode(false);
          this.tagService.addItems(tag, selectedItems).pipe(
            catchError(err => {
              alert('指定标签失败!');
              return of(err);
            })
          ).subscribe();
        }
      });
    }
  }

  moveSelected() {
    const selectedItems = this.masonry.getSelectedItems();
    if (selectedItems.length) {
      ListSelectorComponent.getList(this.dialog,
        `移动${selectedItems.length}个项目到指定列表`,
        l => l.id !== this.list.id
      ).subscribe(list => {
        if (list) {
          this.masonry.enableSelectMode(false);
          this.listService.addItems(list, selectedItems).pipe(
            catchError(err => {
              alert('移动失败!');
              return of(err);
            })
          ).subscribe();
        }
      });
    }
  }

  updateData(id: number) {
    if (!this.list || this.list.id !== id) {
      this.isLoading = true;
      this.listService.get(id).pipe(
        tap(list => this.list = list),
        catchError(err => {
          if (err instanceof HttpErrorResponse && err.status === 404) {
            alert('列表不存在!');
            window.history.go(-1);
          } else {
            alert('获取列表时出错!');
          }
          return of(err);
        })
      ).subscribe();
      this.listService.getItems(id).pipe(
        tap(items => {
          this.isLoading = false;
          this.masonry.changeItems(items);
        }),
        catchError(err => {
          alert('获取项目时出错!');
          this.isLoading = false;
          return of(err);
        })
      ).subscribe();
    }
  }

  constructor(
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private storageService: StorageService,
    private proxyService: ProxyService,
    private listService: ListService,
    private tagService: TagService,
    private itemService: ItemService,
    public appComponent: AppComponent
  ) { }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      const id = params.get('id');
      this.updateData(id === 'default' ? 0 : Number(id));
    });

    this.itemService.onUpdate.subscribe($event => {
      let items = this.masonry.items;
      const updatedItems = $event.items;
      switch ($event.action) {
        case 'add': {
          updatedItems.forEach(u => {
            if ((!u.list && this.list.id == 0) || (u.list && u.list.id === this.list.id)) {
              location.reload();
            }
          });
          break;
        }
        case 'update': {
          items = items.filter(item => {
            for (const u of updatedItems) {
              if (u.id === item.id) {
                if ((!u.list && this.list.id == 0) || (u.list && u.list.id === this.list.id)) {
                  item.title = u.title;
                  item.info = u.info;
                  item.url = u.url;
                  item.img = u.img;
                  item.createdTime = u.createdTime;
                  item.updatedTime = u.updatedTime;
                  return true;
                } else {
                  return false;
                }
              }
            }
            return true;
          });
          this.masonry.changeItems(items);
          break;
        }
        case 'delete': {
          items = items.filter(i => {
            let notFound = true;
            updatedItems.forEach(u => notFound = notFound && u.id !== i.id);
            return notFound;
          });
          this.masonry.changeItems(items);
          break;
        }
      }
    });
  }
}
