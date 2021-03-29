import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../services/authentication.service";
import {UserInfo} from "../models/userInfo";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  public user: UserInfo;

  constructor(private router: Router, private authenticationService: AuthenticationService) {}

  ngOnInit() {
    this.user = this.authenticationService.currentUserValue;
  }

  isEmployee() {
    return this.authenticationService.isEmployee();
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/signin']);
  }

  checkLogOutTab($event) {
    if($event.index == 2 || $event.tab.textLabel === 'Log Out')
      this.logout();
  }
}
