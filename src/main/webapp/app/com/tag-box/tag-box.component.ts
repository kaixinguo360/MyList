import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material';

import {Tag} from '../../service/tag.service';

@Component({
  selector: 'app-tag-box',
  templateUrl: './tag-box.component.html',
  styleUrls: ['./tag-box.component.css']
})
export class TagBoxComponent implements OnInit {

  @Input() tags: Tag[];

  search(tag: Tag) {
    this.router.navigate(['/item/search'], { queryParams: { text: tag.title } });
    this.dialog.closeAll();
  }

  constructor(
    public router: Router,
    private dialog: MatDialog,
  ) { }

  ngOnInit() {
  }

}
