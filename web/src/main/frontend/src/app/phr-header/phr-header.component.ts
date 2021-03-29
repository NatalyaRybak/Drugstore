import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../services/authentication.service";
import {Router} from "@angular/router";


@Component({
  selector: 'app-phr-header',
  templateUrl: './phr-header.component.html',
  styleUrls: ['./phr-header.component.scss']
})
export class PhrHeaderComponent implements OnInit {
  searchForm: any;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit() {
  }


  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/signin']);
  }
}
