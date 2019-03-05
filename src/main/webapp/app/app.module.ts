import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { MaterialModules } from './material-modules.module';
import { LoginComponent } from './login/login.component';
import { ListComponent } from './list/list.component';
import { ListDetailComponent } from './list-detail/list-detail.component';
import { ListEditComponent } from './list-edit/list-edit.component';
import { ItemDialogComponent } from './item-dialog/item-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ListComponent,
    ListDetailComponent,
    ListEditComponent,
    ItemDialogComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MaterialModules
  ],
  providers: [],
  bootstrap: [ AppComponent ],
  entryComponents: [
    ItemDialogComponent
  ]
})
export class AppModule { }
