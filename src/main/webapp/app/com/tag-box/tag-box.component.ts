import { Component, Input, OnInit } from '@angular/core';

import { Tag } from '../../service/tag.service';

@Component({
  selector: 'app-tag-box',
  templateUrl: './tag-box.component.html',
  styleUrls: ['./tag-box.component.css']
})
export class TagBoxComponent implements OnInit {

  @Input() tags: Tag[];

  constructor() { }

  ngOnInit() {
  }

}
