import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiService, Message } from './api.service';

export interface Item {
  id?: number,
  createdTime?: number,
  updatedTime?: number,
  
  title?: string,
  info?: string,
  url?: string,
  img?: string,

  list?: object,
  tags?: object[],

  texts?: object[],
  images?: object[],
  musics?: object[],
  videos?: object[],
  links?: object[]
}

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  get(id: number): Observable<Item> {
    return this.apiService.get<Item>(`item/${id}`);
  }
  
  getAll(search?: string): Observable<Item[]> {
    const params = search ? { search: search } : null;
    return this.apiService.get<Item[]>('item', params);
  }
  
  add(item: Item): Observable<Item> {
    return this.apiService.post<Item>('item', item);
  }
  
  update(item: Item): Observable<Item> {
    return this.apiService.put<Item>('item', item);
  }
  
  delete(id: number): Observable<Message> {
    return this.apiService.delete<Message>('item', { id: id });
  }
  
  constructor(
    private apiService: ApiService
  ) { }
}
