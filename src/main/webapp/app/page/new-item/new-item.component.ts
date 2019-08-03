import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { BreakpointObserver } from '@angular/cdk/layout';
import { MatDialog } from '@angular/material';

import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';

import { StorageService } from '../../service/storage.service';
import { ProxyService } from '../../service/proxy.service';
import { ItemCardMasonryComponent } from '../../com/item-card-masonry/item-card-masonry.component';
import { Item, ItemService } from '../../service/item.service';
import { ListSelectorComponent } from '../../com/list-selector/list-selector.component';

@Component({
  selector: 'app-new-item',
  templateUrl: './new-item.component.html',
  styleUrls: ['./new-item.component.css']
})
export class NewItemComponent implements OnInit {

  @ViewChild('masonry')
  masonry: ItemCardMasonryComponent;

  src: SafeUrl;
  isLoading = true;

  item: Item = {
    title: '',
    info: '',
    img: '',
    url: '',
    images: []
  };

  isOpen = false;

  saveToList() {
    const selectedImages = this.masonry.getSelectedItems();
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
              if (list.id !== 0) {
                this.router.navigate([ '/list', list.id ], { replaceUrl: true });
              } else {
                this.router.navigate([ '/list/default' ], { replaceUrl: true });
              }
            }),
            catchError(err => {
              alert('保存失败!');
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
      item.images.forEach(i => {
        i.img = i.url;
        i.url = null;
      });
      this.masonry.changeItems(item.images);
      localStorage.removeItem('tmpItem');
      this.isLoading = false;
    }
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
    this.masonry.enableSelectMode(true);

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
  }

}
