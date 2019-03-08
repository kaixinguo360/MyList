import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { OrderService } from './order.service';
import { ApiService, Message } from './api.service';
import { Item } from './item.service';

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
    return this.apiService.get<List>(`list/${id}`);
  }

  getItems(id: number): Observable<Item[]> {
    return this.apiService.get<Item[]>(`list/${id}/item`).pipe(
      map(items => {
        this.orderService.sort(items);
        return items;
      })
    );
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

  update(list: List): Observable<List> {
    return this.apiService.put<List>('list', list);
  }

  delete(id: number): Observable<Message> {
    return this.apiService.delete<Message>('list', { id: id });
  }

  constructor(
    private apiService: ApiService,
    private orderService: OrderService
  ) { }
}
