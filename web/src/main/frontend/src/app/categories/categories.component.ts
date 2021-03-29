import { Component, OnInit } from '@angular/core';
import {GoodsService} from '../services/goods.service';
import {Category} from '../models/category.model';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.scss']
})
export class CategoriesComponent implements OnInit {

  categories: Category[];
  category : Category;
  isLoading: boolean;

  constructor( public goodsService: GoodsService) { }

  ngOnInit() {
    this.isLoading = true;
    this.goodsService.getCategories().subscribe(categoriesData => {
      this.isLoading = false;
      this.categories = categoriesData;
    });

  }

  onCategorySelected( c : Category) {
    console.log(c);
    this.goodsService.selectedCategory = c;

  }
}
