import { Component, OnInit } from '@angular/core';
import {KitDto} from '../models/fak.dto.model';
import {FakService} from '../services/fak.service';
import {PageEvent} from '@angular/material/paginator';
import { LocalStorageService } from '../services/local-storage.service';

@Component({
  selector: 'app-fak',
  templateUrl: './fak-list.component.html',
  styleUrls: ['./fak-list.component.scss']
})
export class FakListComponent implements OnInit {
  kits: KitDto[] = [];
  maxPages: number;
  kitsPerPage = 10;
  currentPage = 0;
  isLoading: boolean;

  pageSizeOptions = [10, 20, 30, 40, 50];
  constructor( public kitService: FakService, private localStorage:LocalStorageService) { }

  ngOnInit() {
    this.isLoading = true;
    this.kitService.getKits(this.currentPage, this.kitsPerPage).subscribe(kitssData => {
      this.isLoading=false;
      this.maxPages = kitssData.numPages;
      this.kits = kitssData.kits;
    });
  }

  onChangePage(pageData: PageEvent) {
    this.currentPage = pageData.pageIndex;
    this.kitsPerPage = pageData.pageSize;
   this.isLoading = true;
    this.kitService.getKits(this.currentPage, this.kitsPerPage)
      .subscribe(kitsData => {
       this.isLoading=false;
        this.maxPages = kitsData.numPages;
        this.kits = kitsData.kits;
      });
  }

  onCartClick(kit){
    this.localStorage.addNKitsToStorage(kit,1);
  }


}
