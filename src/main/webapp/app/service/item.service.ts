import { Injectable } from '@angular/core';

import {Observable, Subject} from 'rxjs';
import {map, tap} from 'rxjs/operators';

import { OrderService } from './order.service';
import { ApiService, Message } from './api.service';
import { List } from './list.service';

export interface Item {
  id?: number;
  createdTime?: number;
  updatedTime?: number;

  title?: string;
  info?: string;
  url?: string;
  img?: string;

  list?: List;
  tags?: object[];

  texts?: object[];
  images?: object[];
  musics?: object[];
  videos?: object[];
  links?: object[];
}

export interface UpdateEvent {
  type: string;
  item: Item;
}

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  public onUpdate: Subject<UpdateEvent> = new Subject<UpdateEvent>();

  get(id: number): Observable<Item> {
    return this.apiService.get<Item>(`item/${id}`);
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
      tap(i => this.onUpdate.next({ type: 'add', item: i }))
    );
  }

  update(item: Item): Observable<Item> {
    return this.apiService.put<Item>('item', item).pipe(
      tap(i => this.onUpdate.next({ type: 'update', item: i }))
    );
  }

  delete(id: number): Observable<Message> {
    return this.apiService.delete<Message>('item', { id: id }).pipe(
      tap(() => this.onUpdate.next({ type: 'delete', item: { id: id } }))
    );
  }

  constructor(
    private apiService: ApiService,
    private orderService: OrderService
  ) { }
}
