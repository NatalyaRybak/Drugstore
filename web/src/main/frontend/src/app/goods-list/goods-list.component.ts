import {Component, OnDestroy, OnInit} from '@angular/core';
import {Good} from "../models/good.model";
import {GoodsService} from "../services/goods.service";
import {PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-goods-list',
  templateUrl: './goods-list.component.html',
  styleUrls: ['./goods-list.component.scss']
})
export class GoodsListComponent implements OnInit , OnDestroy{

  goods: Good[] = [];
  maxPages: number;
  goodsPerPage = 10;
  currentPage = 0;
  pageSizeOptions = [10,20,30,40,50];
  constructor(public goodsService:GoodsService) { }

  ngOnInit() {
    this.goodsService.getGoods(this.currentPage, this.goodsPerPage).subscribe(goodsData=>{
      this.maxPages = goodsData.numPages;
      this.goods = goodsData.goods;
    })
  }

  onChangePage(pageData: PageEvent){
    this.currentPage = pageData.pageIndex + 1;
    this.goodsPerPage = pageData.pageSize;
//    this.isLoading = true;
    this.goodsService.getGoods(this.currentPage, this.goodsPerPage)
      .subscribe(goodsData =>{
//        this.isLoading=false;
        this.maxPages = goodsData.numPages;
        this.goods = goodsData.goods;
      });
    // this.meetupsService.getMeetupsFiltered(this.filter,this.meetupsPerPage, this.currentPage).subscribe(meetupsData =>{
    //   this.isLoading=false;
    //   this.meetups = meetupsData.meetups;
    //   this.totalMeetups = meetupsData.meetupCount;
    // });

  }


  ngOnDestroy(): void {
  }

}
