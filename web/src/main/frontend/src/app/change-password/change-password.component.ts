import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";
import {MatDialog, MatSnackBar} from "@angular/material";
import {InfoDialogComponent} from "../info-dialog/info-dialog.component";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  newPassword = '';
  loading = false;

  constructor(
    // private _snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute,
    public dialog: MatDialog,
    private authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      this.authenticationService.checkResetToken(params.get('token')).subscribe((res) => {
        if(!res) {
          const dialogRef = this.dialog.open(InfoDialogComponent, {
            width: '300px',
            data: { title: 'Error',title_color: 'red', message: 'The token is invalid or expired' }
          });

          dialogRef.afterClosed().subscribe(result => {
            if(result.status === 'OK'){
              this.router.navigate(['/signin']);
            }
          });
        }
      })
    });
  }

  onChangePsw() {
    this.route.queryParamMap.subscribe(params => {
      if(this.newPassword) {
        this.loading = true;
        this.authenticationService
          .updatePassword(this.newPassword, params.get('token')).subscribe(() => {
          this.loading = false;
          const dialogRef = this.dialog.open(InfoDialogComponent, {
            width: '300px',
            data: {
              title: 'Success',
              title_color: 'green',
              message: 'Your password was successfully reset'
            }
          });

          dialogRef.afterClosed().subscribe(result => {
            if(result.status === 'OK'){
              this.router.navigate(['/signin']);
            }
          });
          // this.openSnackBar('Your password was updated!', 'Close');
        })
      }
    });
  }

  // openSnackBar(message: string, action: string) {
  //   this._snackBar.open(message, action, {
  //     duration: 2000,
  //   });
  // }

}
