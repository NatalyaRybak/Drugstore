import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {ActiveComponent} from '../models/active-component.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ActiveComponentsService {
  private apiUrl = environment.apiUrl || 'http://localhost:8080/api/v1';

  private activeComponentsUrl = `${this.apiUrl}/active-components`;
  constructor(private http: HttpClient, private router: Router) { }


  getActiveComponents() {
    return this.http.get<ActiveComponent[]>(this.activeComponentsUrl);
  }


}
