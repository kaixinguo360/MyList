import { Injectable } from '@angular/core';

import { Observable, Subject } from 'rxjs';
import { map, tap } from 'rxjs/operators';

import { Order, OrderService } from './order.service';
import { ApiService, Message } from './api.service';
import { List } from './list.service';
import { Tag } from './tag.service';

export interface Image {
  createdTime?: number;
  updatedTime?: number;

  url?: string;
  info?: string;
}

export interface Item {
  id?: number;
  createdTime?: number;
  updatedTime?: number;

  title?: string;
  info?: string;
  url?: string;
  img?: string;

  list?: List;
  tags?: Tag[];

  texts?: object[];
  images?: Image[];
  musics?: object[];
  videos?: object[];
  links?: object[];
}

export interface UpdateEvent {
  action: 'add'|'update'|'delete';
  items?: Item[];
}

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  public onUpdate: Subject<UpdateEvent> = new Subject<UpdateEvent>();

  get(id: number): Observable<Item> {
    return this.apiService.get<Item>(`item/${id}`).pipe(
      map(item => {
        if (item.images) {
          this.orderService.sort(item.images, Order.CREATE_DESC);
        }
        return item;
      })
    );
  }

  getAll(search?: string): Observable<Item[]> {
    const params = search ? { search: search } : null;
    return this.apiService.get<Item[]>('item', params).pipe(
      map(items => {
        this.orderService.sort(items);
        return items;
      })
    );
  }

  add(item: Item): Observable<Item> {
    return this.apiService.post<Item>('item', item).pipe(
      tap(i => this.onUpdate.next({ action: 'add', items: [i] }))
    );
  }

  update(item: Item): Observable<Item> {
    return this.apiService.put<Item>('item', item).pipe(
      tap(i => this.onUpdate.next({ action: 'update', items: [i] }))
    );
  }

  delete(id: number): Observable<Message> {
    return this.apiService.delete<Message>('item', [{ id: id }]).pipe(
      tap(() => this.onUpdate.next({ action: 'delete', items: [{ id: id }] }))
    );
  }

  deleteAll(items: Item[]): Observable<Message> {
    return this.apiService.delete<Message>('item', items).pipe(
      tap(() => this.onUpdate.next({ action: 'delete', items: items }))
    );
  }

  constructor(
    private apiService: ApiService,
    private orderService: OrderService
  ) { }
}
