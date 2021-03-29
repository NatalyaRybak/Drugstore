import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { environment } from 'src/environments/environment';
import {Category} from "../models/category.model";

@Injectable({
  providedIn: 'root'
})
export class FiltersService {
  // private countriesUrl = "http://localhost:8080/api/v1/countries";
  // private manufacturersUrl = "http://localhost:8080/api/v1/manufacturers";
  // private minPriceUrl = "http://localhost:8080/api/v1/price-min";
  // private maxPriceUrl = "http://localhost:8080/api/v1/price-max";
  private apiUrl = environment.apiUrl || 'http://localhost:8080/api/v1';

  private countriesUrl = `${this.apiUrl}/countries`;
  private categoriesUrl = `${this.apiUrl}/categories`;
  private manufacturersUrl = `${this.apiUrl}/manufacturers`;
  private minPriceUrl = `${this.apiUrl}/price-min`;
  private maxPriceUrl = `${this.apiUrl}/price-max`;

  constructor(private http: HttpClient) { }

  getCountries() {
    return this.http.get<string[]>(this.countriesUrl);
  }
  getCategories() {
    return this.http.get<Category[]>(this.categoriesUrl);
  }

  getManufacturers() {
    return this.http.get<string[]>(this.manufacturersUrl);
  }
  getMinPrice() {
      return this.http.get<number>(this.minPriceUrl);
    }
  getMaxPrice() {
      return this.http.get<number>(this.maxPriceUrl);
    }


}
