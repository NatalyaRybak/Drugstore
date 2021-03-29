import { Component, OnInit } from '@angular/core';
import {GoodFull} from '../models/good-full.model';
import {GoodsService} from '../services/goods.service';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {LocalStorageService} from '../services/local-storage.service';
import {Good} from '../models/good.model';

@Component({
  selector: 'app-good-item',
  templateUrl: './good-item.component.html',
  styleUrls: ['./good-item.component.scss']
})
export class GoodItemComponent implements OnInit {

  good: GoodFull;
  value = 1;
  private goodId: string;
  goodAbout = '';
  isLoading: boolean;


  constructor(public goodsService: GoodsService, private localStorageService: LocalStorageService, public route: ActivatedRoute) { }

  ngOnInit() {
    this.route.paramMap.subscribe((paramMap: ParamMap) => {
      if (paramMap.has('goodId')) {
        this.goodId = paramMap.get('goodId');
        this.isLoading = true;
        this.goodsService.getGood(+this.goodId)
          .subscribe(goodData => {
            this.isLoading = false;
            this.good = goodData;
          });
      }
    });

  }

  onButtonClick(str){
    this.goodAbout = str;
  }


  onCartClick(good) {
    this.localStorageService.addNGoodToStorage(good, this.value);
  }

  onMinusClick(){
    if(this.value >0 )
      this.value--;
  }

  onPlusClick(){
   this.value++;
  }


}
