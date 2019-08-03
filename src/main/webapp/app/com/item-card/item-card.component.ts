import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { ProxyService } from '../../service/proxy.service';
import { Item } from '../../service/item.service';

@Component({
  selector: 'app-item-card',
  templateUrl: './item-card.component.html',
  styleUrls: ['./item-card.component.css']
})
export class ItemCardComponent implements OnInit {

  @Input() selected = false;
  @Input() item: Item;
  @Input() maxLength = 100;
  @Output() onLoad = new EventEmitter();

  getDomain(url: string) {
    if (!url) {
      return url;
    }
    const match = url.match(/:\/\/(www[0-9]?\.)?(.[^/:]+)/i);
    if (match != null && match.length > 2 && typeof match[2] === 'string' && match[2].length > 0) {
      return match[2];
    } else {
      return url;
    }
  }

  limit(str: string, length?: number): string {
    length = length ? length : this.maxLength;
    return !str || str.length <= length ? str : str.substr(0 , length) + '…';
  }

  constructor(
    private proxyService: ProxyService
  ) { }

  ngOnInit() {
  }

}
