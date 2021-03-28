import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {GoodsListComponent} from "./goods-list/goods-list.component";
import {
  MatButtonModule,
  MatCardModule,
  MatInputModule,
  MatMenuModule,
  MatToolbarModule,
  MatPaginatorModule
} from "@angular/material";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {HttpClientModule} from "@angular/common/http";
import { HeaderComponent } from './header/header.component';
import {MatIconModule} from "@angular/material/icon";
import {MatChipsModule} from "@angular/material/chips";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatDividerModule} from "@angular/material/divider";
import { GoodItemComponent } from './good-item/good-item.component';
import { GoodCardComponent } from './good-card/good-card.component';
import {FlexLayoutModule} from "@angular/flex-layout";
import { FiltersPanelComponent } from './filters-panel/filters-panel.component';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatSelectModule} from "@angular/material/select";
// import {StyleUtils,StylesheetMap,MediaMarshaller,ɵMatchMedia,BreakPointRegistry,PrintHook,LayoutStyleBuilder,FlexStyleBuilder,ShowHideStyleBuilder,FlexOrderStyleBuilder} from "@angular/flex-layout";
@NgModule({
  declarations: [
    AppComponent,
    GoodsListComponent,
    HeaderComponent,
    GoodItemComponent,
    GoodCardComponent,
    FiltersPanelComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FlexLayoutModule,
    MatPaginatorModule,
    MatCardModule,
    MatButtonModule,
    MatMenuModule,
    MatInputModule,
    MatToolbarModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatChipsModule,
    MatGridListModule,
    MatDividerModule,
    MatSidenavModule,
    MatSelectModule
  ],
  providers: [
    // StyleUtils,StylesheetMap,MediaMarshaller,ɵMatchMedia,BreakPointRegistry,PrintHook,LayoutStyleBuilder,FlexStyleBuilder,ShowHideStyleBuilder,FlexOrderStyleBuilder
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
