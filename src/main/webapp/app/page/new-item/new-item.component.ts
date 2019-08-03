import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { MatDialog } from '@angular/material';

import { NgxMasonryComponent, NgxMasonryOptions } from 'ngx-masonry';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';

import { environment } from '../../../environments/environment';
import { StorageService } from '../../service/storage.service';
import { ProxyService } from '../../service/proxy.service';
import { Image, Item, ItemService } from '../../service/item.service';
import { ListSelectorComponent } from '../../com/list-selector/list-selector.component';

interface SelectableImage extends Image, Item {
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

  saveToList() {
    const selectedImages = this.images.filter(i => i.selected);
    if (selectedImages.length) {
      ListSelectorComponent.getList(this.dialog).subscribe(list => {
        if (list) {
          this.item.list = list.id !== 0 ? { id: list.id } : null;
          this.item.img = selectedImages[0].img;
          if (selectedImages.length > 1) {
            selectedImages.forEach(image => {
              image.url = image.img;
              this.item.images.push(image);
            });
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
      });
    }
  }

  updateImages() {
    const tmpItemStr = localStorage.getItem('tmpItem');
    if (tmpItemStr) {
      const item = JSON.parse(tmpItemStr);
      this.item.title = item.title;
      this.images = item.images;
      this.images.forEach(i => {
        i.img = i.url;
        i.url = null;
      });
      localStorage.removeItem('tmpItem');
      this.isLoading = false;
    }
  }

  updateLayout() {
    this.masonry.layout();
  }

  onImageLoad($event, index: number) {
    if ($event !== null) {
      const size = $event.naturalWidth < $event.naturalHeight ? $event.naturalWidth : $event.naturalHeight;
      const minImageSize = Number(this.storageService.get('minImageSize', environment.minImageSize + ''));
      if (size < minImageSize) {
        this.images.splice(index, 1);
      }
    } else {
      this.images[index].title = '图像加载失败';
    }
    this.updateLayout();
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
    private router: Router,
    private dialog: MatDialog,
    private domSanitizer: DomSanitizer,
    private breakpointObserver: BreakpointObserver,
    private storageService: StorageService,
    private proxyService: ProxyService,
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
  }

}
