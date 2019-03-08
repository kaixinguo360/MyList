import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import {map} from 'rxjs/operators';

import { OrderService } from './order.service';
import { ApiService, Message } from './api.service';
import { Item } from './item.service';

export interface Tag {
  id?: number;
  createdTime?: number;
  updatedTime?: number;

  title?: string;
  info?: string;
}

@Injectable({
  providedIn: 'root'
})
export class TagService {

  get(id: number): Observable<Tag> {
    return this.apiService.get<Tag>(`tag/${id}`);
  }

  getItems(id: number): Observable<Item[]> {
    return this.apiService.get<Item[]>(`tag/${id}/item`).pipe(
      map(items => {
        this.orderService.sort(items);
        return items;
      })
    );
  }

  getAll(search?: string): Observable<Tag[]> {
    const params = search ? { search: search } : null;
    return this.apiService.get<Tag[]>('tag', params).pipe(
      map(tags => {
        this.orderService.sort(tags);
        return tags;
      })
    );
  }

  add(tag: Tag): Observable<Tag> {
    return this.apiService.post<Tag>('tag', tag);
  }

  update(tag: Tag): Observable<Tag> {
    return this.apiService.put<Tag>('tag', tag);
  }

  delete(id: number): Observable<Message> {
    return this.apiService.delete<Message>('tag', { id: id });
  }

  constructor(
    private apiService: ApiService,
    private orderService: OrderService
  ) { }
}
