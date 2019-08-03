import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemCardMasonryComponent } from './item-card-masonry.component';

describe('ItemCardMasonryComponent', () => {
  let component: ItemCardMasonryComponent;
  let fixture: ComponentFixture<ItemCardMasonryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ItemCardMasonryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemCardMasonryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
