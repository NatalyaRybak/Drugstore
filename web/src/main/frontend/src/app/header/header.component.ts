import { Component, OnInit } from '@angular/core';

import { LocalStorageService } from '../services/local-storage.service';

import {MatDialog} from '@angular/material';
import {SigninComponent} from '../signin/signin.component';
import {SignupComponent} from '../signup/signup.component';
import {AuthenticationService} from '../services/authentication.service';
import {Router} from '@angular/router';
import {GoodsService} from '../services/goods.service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {


  searchModel: string;

  constructor(public dialog: MatDialog,
              private router: Router,
              private authenticationService: AuthenticationService,
              private localStorage: LocalStorageService,
              private goodsService: GoodsService
              ) { }

  cartLenght = this.localStorage.totalLength;

  ngOnInit() {
    this.localStorage.onClick.subscribe(len => this.cartLenght = len);
  }

  isAuthed() {
    return this.authenticationService.isAuthed();
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/signin']);
  }

  openSignInDialog(): void {
    const dialogRef = this.dialog.open(SigninComponent, {
      width: '300px'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('Dialog Closed');
    });
  }

  openSignUpDialog(): void {
    const dialogRef = this.dialog.open(SignupComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('Dialog Closed');
    });
  }

  //
  // applySearch(event: KeyboardEvent) {
  //   this.goodsService.searchInput = this.searchModel;
  //
  //
  // }
}
