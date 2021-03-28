import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {ActiveComponent} from "../models/active-component.model";

@Injectable({
  providedIn: 'root'
})
export class ActiveComponentsService {
  private activeComponentsUrl = "http://localhost:8080/api/v1/active-components";

  constructor(private http: HttpClient, private router: Router) { }


  getActiveComponents() {
    return this.http.get<ActiveComponent[]>(this.activeComponentsUrl);
  }
}
