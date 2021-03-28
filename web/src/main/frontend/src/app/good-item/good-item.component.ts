import { Component, OnInit } from '@angular/core';
import {GoodFull} from "../models/good-full.model";
import {GoodsService} from "../services/goods.service";
import {ActivatedRoute, ParamMap} from "@angular/router";

@Component({
  selector: 'app-good-item',
  templateUrl: './good-item.component.html',
  styleUrls: ['./good-item.component.scss']
})
export class GoodItemComponent implements OnInit {

  good: GoodFull;
  private goodId:string;

  constructor(public goodsService:GoodsService, public route: ActivatedRoute) { }

  ngOnInit() {
    this.route.paramMap.subscribe((paramMap: ParamMap) => {
      if (paramMap.has('goodId')) {
        this.goodId = paramMap.get('goodId');
        this.goodsService.getGood(+this.goodId)
          .subscribe(goodData => {
            this.good = goodData;
          });
      }
    });
  }
}
