import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ServiceWorkerModule } from '@angular/service-worker';

import { NgxMasonryModule } from 'ngx-masonry';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { environment } from '../environments/environment';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { MaterialModules } from './material-modules.module';
import { ItemCardComponent } from './com/item-card/item-card.component';
import { ItemCardMasonryComponent } from './com/item-card-masonry/item-card-masonry.component';
import { ListSelectorComponent } from './com/list-selector/list-selector.component';
import { TagSelectorComponent } from './com/tag-selector/tag-selector.component';
import { TagBoxComponent } from './com/tag-box/tag-box.component';

import { LoginComponent } from './page/login/login.component';
import { ListComponent } from './page/list/list.component';
import { ListDetailComponent } from './page/list-detail/list-detail.component';
import { ListEditComponent } from './page/list-edit/list-edit.component';
import { ItemDetailComponent, ItemDetailDialogComponent, ItemDetailPopupComponent } from './page/item-detail/item-detail.component';
import { ItemEditComponent, ItemEditDialogComponent } from './page/item-edit/item-edit.component';
import { NewItemComponent } from './page/new-item/new-item.component';

@NgModule({
  declarations: [
    AppComponent,
    ItemCardComponent,
    ItemCardMasonryComponent,
    ListSelectorComponent,
    TagSelectorComponent,
    TagBoxComponent,
    LoginComponent,
    ListComponent,
    ListDetailComponent,
    ListEditComponent,
    ItemDetailDialogComponent,
    ItemDetailComponent,
    ItemDetailPopupComponent,
    ItemEditDialogComponent,
    ItemEditComponent,
    NewItemComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModules,
    NgxMasonryModule,
    InfiniteScrollModule,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production })
  ],
  providers: [],
  bootstrap: [ AppComponent ],
  entryComponents: [
    ListSelectorComponent,
    TagSelectorComponent,
    ItemDetailPopupComponent,
    ItemEditDialogComponent
  ]
})
export class AppModule { }
