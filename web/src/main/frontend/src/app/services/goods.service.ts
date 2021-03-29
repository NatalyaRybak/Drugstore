import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Router } from "@angular/router";
import {Good} from '../models/good.model';
import {GoodFull} from '../models/good-full.model';
import {Category} from '../models/category.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GoodsService {
  // private goodsUrl = "http://localhost:8080/api/v1/goods";
  // private categoriesUrl = "http://localhost:8080/api/v1/categories";

  private apiUrl = environment.apiUrl || 'http://localhost:8080/api/v1';

  private goodsUrl = `${this.apiUrl}/goods`;
  private categoriesUrl = `${this.apiUrl}/categories`;
  public  search: string;
  public selectedCategory: Category;

  constructor(private http: HttpClient, private router: Router) { }

  getGoods(currentPage: number, goodsPerPage: number ) {
    const queryParams = `?page=${currentPage}&page-size=${goodsPerPage}`;
    return this.http
      .get<{numPages: number, goods: Good[] }>(
        this.goodsUrl + queryParams
      );
  }
    getGoodsFiltered( currentPage: number, goodsPerPage: number, search: string,
                      priceMin: number, priceMax: number, manufacturer: string,
                      country: string, categoryId: number, activeId: number ) {
    let queryParams = `?page=${currentPage}&page-size=${goodsPerPage}`;
    if (search != null) { queryParams += `&search=${search}`; }
    queryParams += `&price-min=${priceMin}&price-max=${priceMax}`;
    if (manufacturer != null) { queryParams += `&manufacturer=${manufacturer}`; }
    if (country != null) { queryParams += `&country=${country}`; }
    if (categoryId != null) { queryParams += `&category-id=${categoryId}`; }
    if (activeId != null) { queryParams += `&active-component-id=${activeId}`; }

    return this.http
      .get<{numPages: number, goods: Good[] }>(
        this.goodsUrl + queryParams
      );
  }


  getGood(id: number) {
    return this.http.get<GoodFull>(this.goodsUrl + '/' + id);
  }

  getCategories() {
    return this.http.get<Category[]>(this.categoriesUrl);
  }
}
