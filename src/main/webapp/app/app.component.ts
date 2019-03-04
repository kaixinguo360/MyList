import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  public title = '';

  back(): void {
    window.stop();
    this.location.back();
  }

  constructor(
    private location: Location
  ) { }

  ngOnInit() { }
}
