import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { Router } from "@angular/router";
import {Good} from "../models/good.model";
import {GoodFull} from "../models/good-full.model";
import {Category} from "../models/category.model";

@Injectable({
  providedIn: 'root'
})
export class GoodsService {
  private goodsUrl = "http://localhost:8080/api/v1/goods";
  private categoriesUrl = "http://localhost:8080/api/v1/categories";

  constructor(private http: HttpClient, private router: Router) { }

  getGoods(currentPage: number, goodsPerPage : number ) {
    const queryParams = `?page=${currentPage}&page-size=${goodsPerPage}`;
    return this.http
      .get<{numPages : number, goods: Good[] }>(
        this.goodsUrl+queryParams
      );
  }

  getGood(id:number){
    return this.http.get<GoodFull>(this.goodsUrl+"/"+ id);
  }

  getCategories() {
    return this.http.get<Category[]>(this.categoriesUrl);
  }
}
