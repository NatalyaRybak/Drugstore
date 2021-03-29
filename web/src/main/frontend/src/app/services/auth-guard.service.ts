import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {AuthenticationService} from "./authentication.service";


@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{

  constructor(private _authService: AuthenticationService, private _router: Router) {
  }

  async canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot):Promise<boolean> {
    console.log("can activated called");
    if (this._authService.currentUserValue) {
      if(next.data.roles){
        let userRole: string = await this._authService.currentUserValue.role.role;
        if(next.data.roles.indexOf(userRole) === -1){
          this._router.navigate(['/signin']);
          return false;
        }
      }
      return true;
    }
    // navigate to login page
    this._router.navigate(['/signin']);
    // you can save redirect url so after authing we can move them back to the page they requested
    return false;
  }
}
