import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  public get(key: string, defaultValue?: string): string {
    const value = localStorage.getItem(key);
    if (value == null && defaultValue != null) {
      this.set(key, defaultValue);
      return defaultValue;
    } else {
      return value;
    }
  }

  public set(key: string, value: string): void {
    if (value != null) {
      localStorage.setItem(key, value);
    } else {
      localStorage.removeItem(key);
    }
  }

  constructor() { }
}
