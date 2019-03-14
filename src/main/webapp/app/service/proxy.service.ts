import { Injectable } from '@angular/core';

import { environment } from '../../environments/environment';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class ProxyService {

  private root = environment.proxyRoot;

  public static base64url(input) {
    return btoa(encodeURI(input))
      .replace(/-/g, '+')
      .replace(/_/g, '/');
  }

  public proxy(url: string): string {
    const proxyMode = this.storageService.get('proxyMode', 'http');
    const proxy = this.root + 'static/' + ProxyService.base64url(url);
    switch (proxyMode) {
      case 'all':
        return proxy;
      case 'http':
        return (url.substr(0, 7) === 'http://') ? proxy : url;
      case 'none':
        return url;
      default:
        return url;
    }
  }

  constructor(
    private storageService: StorageService
  ) { }
}
