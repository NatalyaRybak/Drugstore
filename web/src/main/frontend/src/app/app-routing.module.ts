import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {GoodsListComponent} from "./goods-list/goods-list.component";
import {GoodItemComponent} from "./good-item/good-item.component";


const routes: Routes = [
  {path: 'goods', component: GoodsListComponent},
  {path: 'goods/:goodId', component: GoodItemComponent}
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
