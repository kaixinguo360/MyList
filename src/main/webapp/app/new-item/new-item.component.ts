import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';

import { NgxMasonryComponent, NgxMasonryOptions } from 'ngx-masonry';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';

import { environment } from '../../environments/environment';
import { ProxyService } from '../service/proxy.service';
import { List, ListService } from '../service/list.service';
import { Image, Item, ItemService } from '../service/item.service';

interface SelectableImage extends Image {
  selected?: boolean;
}

@Component({
  selector: 'app-new-item',
  templateUrl: './new-item.component.html',
  styleUrls: ['./new-item.component.css']
})
export class NewItemComponent implements OnInit {

  @ViewChild('masonry')
  masonry: NgxMasonryComponent;
  isMobile = false;
  column = 1;
  masonryWidth: number;
  columnWidth: number;
  masonryOptions: NgxMasonryOptions = {
    transitionDuration: '0.2s',
    gutter: 0,
    resize: false,
    initLayout: true
  };

  src: SafeUrl;
  isLoading = true;
  lists: List[];

  item: Item = {
    title: '',
    info: '',
    img: '',
    url: '',
    images: []
  };
  images: SelectableImage[] = [];
  
  isOpen = false;

  selectAll() {
    if (this.images.find(i => !i.selected)) {
      this.images.forEach(item => item.selected = true);
    } else {
      this.images.forEach(item => item.selected = false);
    }
  }

  saveToList(list: List) {
    const selectedImages = this.images.filter(i => i.selected);
    if (selectedImages.length) {
      this.item.list = list.id !== 0 ? { id: list.id } : null;
      this.item.img = selectedImages[0].url;
      if (selectedImages.length > 1) {
        selectedImages.forEach(image => this.item.images.push(image));
      }
      this.itemService.add(this.item).pipe(
        tap(() => {
          this.images = this.images.filter(i => !i.selected);
          if (list.id !== 0) {
            this.router.navigate([ '/list', list.id ], { replaceUrl: true });
          } else {
            this.router.navigate([ '/list/default' ], { replaceUrl: true });
          }
        }),
        catchError(err => {
          alert('保存失败!');
          this.images.forEach(item => item.selected = false);
          return of(err);
        })
      ).subscribe();
    }
  }

  updateImages() {
    const tmpItemStr = localStorage.getItem('tmpItem');
    if (tmpItemStr) {
      const item = JSON.parse(tmpItemStr);
      this.item.title = item.title;
      this.images = item.images;
      localStorage.removeItem('tmpItem');
      this.isLoading = false;
    }
  }

  updateLayout() {
    this.masonry.layout();
  }

  @HostListener('window:resize')
  private resize() {
    if (this.isMobile) {
      this.column = 1;
      this.columnWidth = window.innerWidth;
      this.masonryWidth = window.innerWidth;
    } else {
      this.column = Math.round(window.innerWidth / this.columnWidth) - 1;
      this.columnWidth = environment.defaultColumnWidth;
      this.masonryWidth = (this.column === 0) ? this.columnWidth : this.column * this.columnWidth;
    }
    this.updateLayout();
  }

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private domSanitizer: DomSanitizer,
    private breakpointObserver: BreakpointObserver,
    private proxyService: ProxyService,
    private listService: ListService,
    private itemService: ItemService
  ) { }

  ngOnInit() {

    // Get URL From The Route
    this.route.queryParams.subscribe((params: Params) => {
      this.item.url = params['url'];
      if (this.item.url) {
        const url = this.proxyService.proxyPage(this.item.url);
        this.src = this.domSanitizer.bypassSecurityTrustResourceUrl(url);
      }
    });

    // Add Event Listener For Storage
    window.addEventListener('storage', () => {
      this.updateImages();
    });

    // UI
    this.breakpointObserver.observe(Breakpoints.XSmall).pipe(
      tap(({ matches }) => {
        this.isMobile = matches;
        this.resize();
      })
    ).subscribe();

    this.resize();

    // Get All Lists
    if (!this.lists) {
      this.listService.getAll().pipe(
        tap(lists => {
          lists.unshift({ id: 0, title: '默认列表' });
          this.lists = lists;
        }),
        catchError(err => {
          alert('获取列表信息时出错!');
          return of(err);
        })
      ).subscribe();
    }
  }

}
