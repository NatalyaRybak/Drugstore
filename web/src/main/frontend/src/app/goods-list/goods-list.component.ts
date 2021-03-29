import {Component, OnDestroy, OnInit} from '@angular/core';
import {Good} from '../models/good.model';
import {GoodsService} from '../services/goods.service';
import {PageEvent} from '@angular/material/paginator';
import {LocalStorageService} from '../services/local-storage.service';
import {FiltersService} from '../services/filters.service';
import {Category} from '../models/category.model';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';

@Component({
  selector: 'app-goods-list',
  templateUrl: './goods-list.component.html',
  styleUrls: ['./goods-list.component.scss']
})
export class GoodsListComponent implements OnInit , OnDestroy {

  goods: Good[] = [];
  maxPages: number;
  goodsPerPage = 10;
  currentPage = 0;
  pageSizeOptions = [10, 20, 30, 40, 50];
  states: string[] = [
    'Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware',
    'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky',
    'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi',
    'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico',
    'New York', 'North Carolina', 'North Dakota', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania',
    'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont',
    'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
  ];
  manufacturersList: string[];
  manufacturerModel: string;
  countriesList: string[];
  countryModel: string;
  categoriesList: Category[];
  categoryModel: number;
  minPrice: number;
  minprice: number;
  maxPrice: number;
  maxprice: number;
  searchModel: string;
  componentId: number;
  isLoading: boolean;

  constructor(public goodsService: GoodsService,
              private localStorageService: LocalStorageService,
              public filtersService: FiltersService,
              public route: ActivatedRoute, private _router: Router,
              private localStorage: LocalStorageService) { }

  ngOnInit() {

    this.route.paramMap.subscribe((paramMap: ParamMap) => {
      if (paramMap.has('componentId')) {
        this.componentId = +paramMap.get('componentId');
        this.getFilteredGoods(this.currentPage, this.goodsPerPage, null,5,
          233, null,null, null, this.componentId);
      }else{
        if (this.goodsService.selectedCategory != null) {
          this.categoryModel = this.goodsService.selectedCategory.id;
          this.getFilteredGoods(this.currentPage, this.goodsPerPage, null,5,
            233, null,null, this.categoryModel, null);

          // this.goodsService.getGoodsFiltered(this.currentPage, this.goodsPerPage, null,5,
          //   233, null,null, this.categoryModel, null ).subscribe(goodsData => {
          //   this.maxPages = goodsData.numPages;
          //   this.goods = goodsData.goods;
          // });
        }else{
          this.isLoading = true;
          this.goodsService.getGoods(this.currentPage, this.goodsPerPage).subscribe(goodsData => {
            this.isLoading = false;
            this.maxPages = goodsData.numPages;
            this.goods = goodsData.goods;
          });
        }
      }
      this.getAllFilterData();

    });



  }

  getAllFilterData(){
    this.filtersService.getCountries().subscribe(countriesData => {
      this.countriesList = countriesData;
    });
    this.filtersService.getCategories().subscribe(categoriesData => {
      this.categoriesList = categoriesData;
    });
    this.filtersService.getManufacturers().subscribe(manufacturersData => {
      this.manufacturersList = manufacturersData;
    });
    this.filtersService.getMinPrice().subscribe(minPrice => {
      this.minPrice = minPrice;
    });
    this.filtersService.getMaxPrice().subscribe(maxPrice => {
      this.maxPrice = maxPrice;
    });
  }


  onChangePage(pageData: PageEvent) {
    this.currentPage = pageData.pageIndex;
    this.goodsPerPage = pageData.pageSize;
    this.isLoading = true;
    if(this.searchModel==null && this.categoryModel==null
      && this.manufacturerModel==null && this.countryModel==null
      && this.minprice!=this.minPrice && this.maxprice!=this.maxPrice){
      this.goodsService.getGoods(this.currentPage, this.goodsPerPage)
        .subscribe(goodsData => {
          this.isLoading=false;
          this.maxPages = goodsData.numPages;
          this.goods = goodsData.goods;
        });
    }else{
      this.onFilter();
    }



  }
  getFilteredGoods(page, perPage, search, min, max, manufacturer, country, category, component ){
    this.isLoading = true;
    this.goodsService.getGoodsFiltered(page, perPage, search, min, max, manufacturer, country, category, component ).subscribe(goodsData => {
      this.isLoading=false;
      this.maxPages = goodsData.numPages;
      this.goods = goodsData.goods;
    });


  }


  onFilter() {

    if (this.minprice == null) { this.minprice = this.minPrice; }
    if (this.maxprice == null) { this.maxprice = this.maxPrice; }
    this.getFilteredGoods(this.currentPage, this.goodsPerPage, this.searchModel,
      this.minprice, this.maxprice, this.manufacturerModel, this.countryModel, this.categoryModel, this.componentId);

  }


  onCartClick(good) {
    this.localStorageService.addNGoodToStorage(good,1);

  }


  ngOnDestroy(): void {
  }

  onReset() {

    this.categoryModel = null;
    this.goodsService.selectedCategory = null;
    this.manufacturerModel = null;
    this.countryModel = null;
    this.minprice = this.minPrice;
    this.maxprice = this.maxPrice;

    if(this.componentId == null) {
      this.isLoading = true;
      this.goodsService.getGoods(this.currentPage, this.goodsPerPage).subscribe(goodsData => {
        this.isLoading = false;
        this.maxPages = goodsData.numPages;
        this.goods = goodsData.goods;
      });
    }
    else{
      this.getFilteredGoods(this.currentPage, this.goodsPerPage, null,5,
        233, null,null, null, this.componentId);
    }



  }

  applySearch(event: KeyboardEvent) {
    this.searchModel = (event.target as HTMLInputElement).value;
    this.onFilter();

  }
}
