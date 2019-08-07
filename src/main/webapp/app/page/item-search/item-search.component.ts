import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {MatDialog} from '@angular/material';

import {of} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';

import {ItemService} from '../../service/item.service';
import {ListService} from '../../service/list.service';
import {TagService} from '../../service/tag.service';

import {ItemCardMasonryComponent} from '../../com/item-card-masonry/item-card-masonry.component';
import {TagSelectorComponent} from '../../com/tag-selector/tag-selector.component';
import {ListSelectorComponent} from '../../com/list-selector/list-selector.component';
import {AppComponent} from '../../app.component';

@Component({
  selector: 'app-item-search',
  templateUrl: './item-search.component.html',
  styleUrls: ['./item-search.component.css']
})
export class ItemSearchComponent implements OnInit {

  isLoading = true;
  text = '';
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
      ).subscribe(res => {
        if (res) {
          this.masonry.enableSelectMode(false);
          this.tagService.addItems(res.tags, selectedItems, res.isClear).pipe(
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
        `移动${selectedItems.length}个项目到指定列表`
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

  update() {
    this.router.navigate(['/item/search'], { queryParams: { text: this.text } });
  }

  search(text: string) {
    this.isLoading = true;
    const tagTitles = text.split(' ').map(line => {
      if (line.startsWith('+')) {
        return {
          title: line.slice(1),
          type: 'or'
        };
      } else if (line.startsWith('-')) {
        return {
          title: line.slice(1),
          type: 'not'
        };
      } else {
        return {
          title: line,
          type: 'and'
        };
      }
    });
    const andIds = [], orIds = [], notIds = [];
    this.tagService.getAll().pipe(tap(tags => {
      tags.forEach(tag => {
        tagTitles.forEach(tagTitle => {
          if (tag.title === tagTitle.title) {
            switch (tagTitle.type) {
              case 'and': {
                andIds.push(tag.id);
                break;
              }
              case 'or': {
                orIds.push(tag.id);
                break;
              }
              case 'not': {
                notIds.push(tag.id);
                break;
              }
            }
          }
        });
      });
      this.tagService.searchItems(andIds, orIds, notIds).pipe(
        tap(items => {
          this.isLoading = false;
          this.masonry.changeItems(items);
        })
      ).subscribe();
    })).subscribe();
  }

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private dialog: MatDialog,
    private itemService: ItemService,
    private listService: ListService,
    private tagService: TagService,
    public appComponent: AppComponent
  ) { }

  ngOnInit() {

    // Get Params From The Route
    this.route.queryParams.subscribe((params: Params) => {
      this.text = params['text'] ? params['text'] : '';
      this.search(this.text);
    });

    this.itemService.onUpdate.subscribe($event => {
      let items = this.masonry.items;
      const updatedItems = $event.items;
      switch ($event.action) {
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
