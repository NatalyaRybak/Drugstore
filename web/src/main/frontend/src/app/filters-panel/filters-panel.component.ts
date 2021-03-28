import { Component, OnInit } from '@angular/core';
import {FiltersService} from "../services/filters.service";

@Component({
  selector: 'app-filters-panel',
  templateUrl: './filters-panel.component.html',
  styleUrls: ['./filters-panel.component.scss']
})
export class FiltersPanelComponent implements OnInit {
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
  countriesList: string[];
  minPrice:number;
  maxPrice:number;

  showFilters = true;
  constructor(public filtersService: FiltersService,) { }

  ngOnInit() {
    this.filtersService.getCountries().subscribe(countriesData=> {
      this.countriesList = countriesData;
    });
    this.filtersService.getManufacturers().subscribe(manufacturersData=> {
      this.manufacturersList = manufacturersData;
    });
    this.filtersService.getMinPrice().subscribe(minPrice=> {
      this.minPrice = minPrice;
    });
    this.filtersService.getMaxPrice().subscribe(maxPrice=> {
      this.maxPrice = maxPrice;
    });



  }

}
