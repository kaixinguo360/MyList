import { Injectable } from '@angular/core';

import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';

import { OrderService } from './order.service';
import { ApiService, Message } from './api.service';
import { Item, ItemService } from './item.service';

export interface List {
  id?: number;
  createdTime?: number;
  updatedTime?: number;

  title?: string;
  info?: string;
  img?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ListService {

  get(id: number): Observable<List> {
    if (id === 0) {
      return of({
        id: 0,
        createdTime: 0,
        updatedTime: 0,
        title: '默认列表'
      });
    } else {
      return this.apiService.get<List>(`list/${id}`);
    }
  }

  getItems(id: number): Observable<Item[]> {
    if (id === 0) {
      return this.itemService.getAll('list:none');
    } else {
      return this.apiService.get<Item[]>(`list/${id}/item`).pipe(
        map(items => {
          this.orderService.sort(items);
          return items;
        })
      );
    }
  }

  getAll(search?: string): Observable<List[]> {
    const params = search ? { search: search } : null;
    return this.apiService.get<List[]>('list', params).pipe(
      map(list => {
        this.orderService.sort(list);
        return list;
      })
    );
  }

  add(list: List): Observable<List> {
    return this.apiService.post<List>('list', list);
  }

  addItems(id: number, items: Item[]): Observable<Message> {
    const ids: number[] = items.map(item => item.id);
    items.forEach(item => item.list = { id: id});
    this.itemService.onUpdate.next({ action: 'update', items: items });
    return this.apiService.post<Message>(`list/${id}/item`, ids);
  }

  update(list: List): Observable<List> {
    return this.apiService.put<List>('list', list);
  }

  delete(id: number): Observable<Message> {
    return this.apiService.delete<Message>('list', { id: id });
  }

  constructor(
    private apiService: ApiService,
    private itemService: ItemService,
    private orderService: OrderService
  ) { }
}
