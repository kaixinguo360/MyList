import { Injectable } from '@angular/core';

import { StorageService } from './storage.service';

export enum Order {
  UPDATE_ASC = 'UPDATE_ASC',
  UPDATE_DESC = 'UPDATE_DESC',
  CREATE_ASC = 'CREATE_DESC',
  CREATE_DESC = 'CREATE_ASC',
  NAME_ASC = 'NAME_ASC',
  NAME_DESC = 'NAME_DESC',
  RANDOM = 'RANDOM'
}

export interface Sortable {
  createdTime?: number;
  updatedTime?: number;
  title?: string;
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  public sort(array: Sortable[], order?: Order) {
    array.sort(
      (a, b) => {
        switch (order ? order : this.storageService.get('order')) {
          case Order.UPDATE_ASC:
            return b.updatedTime - a.updatedTime;
          case Order.UPDATE_DESC:
            return a.updatedTime - b.updatedTime;
          case Order.CREATE_ASC:
            return b.createdTime - a.createdTime;
          case Order.CREATE_DESC:
            return a.createdTime - b.createdTime;
          case Order.NAME_DESC:
            return a.title < b.title ? 1 : a.title > b.title ? -1 : 0;
          case Order.NAME_ASC:
            return a.title < b.title ? -1 : a.title > b.title ? 1 : 0;
          case Order.RANDOM:
            return Math.random() > 0.5 ? -1 : 1;
        }
      }
    );
  }

  constructor(
    private storageService: StorageService
  ) { }
}
