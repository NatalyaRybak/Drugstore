import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FiltersService{
  private countriesUrl = "http://localhost:8080/api/v1/countries";
  private manufacturersUrl = "http://localhost:8080/api/v1/manufacturers";
  private minPriceUrl = "http://localhost:8080/api/v1/price-min";
  private maxPriceUrl = "http://localhost:8080/api/v1/price-max";

  constructor(private http: HttpClient) { }

  getCountries() {
    return this.http.get<string[]>(this.countriesUrl);
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
