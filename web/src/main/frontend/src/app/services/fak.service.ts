import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {KitDto} from '../models/fak.dto.model';

@Injectable({
  providedIn: 'root'
})
export class FakService {

  private apiUrl = environment.apiUrl || 'http://localhost:8080/api/v1';

  private faksUrl = `${this.apiUrl}/kits`;

  constructor(private http: HttpClient, private router: Router) { }

  getKits(currentPage: number, kitsPerPage: number ) {
    const queryParams = `?page=${currentPage}&page-size=${kitsPerPage}`;
    return this.http
      .get<{numPages: number, kits: KitDto[] }>(
        this.faksUrl + queryParams
      );
  }

  getKit(id: number) {
    return this.http.get<KitDto>(this.faksUrl + '/' + id);
  }


}
