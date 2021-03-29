import {Component, Inject, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material";
import {ForgotPswComponent} from "../forgot-psw/forgot-psw.component";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";
import {first} from "rxjs/operators";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {InfoDialogComponent} from "../info-dialog/info-dialog.component";

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  form: FormGroup;
  loading = false;
  error = null;
  loginInvalid = false;
  returnUrl: string;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private authenticationService: AuthenticationService,
              public dialog: MatDialog,
              private fb: FormBuilder)
  {
    // thisDialogRef.disableClose = true;
  }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'];
    if (this.authenticationService.currentUserValue) {
      console.log("already authed");
      this.router.navigate([this.returnUrl ? this.returnUrl : this.authenticationService.getUserMainPageUrl()]);
    }
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ForgotPswComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result && result.status === 'Submit'){
        this.loading = true;
        this.authenticationService.resetPassword(result.email).subscribe(() => {
          this.loading = false;
          const dialogRef = this.dialog.open(InfoDialogComponent, {
            width: '300px',
            data: {
              title: 'Success',
              title_color: 'green',
              message: 'Check your email for the reset password link'
            }
          });
          dialogRef.afterClosed().subscribe(() => {});
        });
      }
    });
  }

  login() {
    if(this.form.valid) {
      this.loading = true;
      this.error = null;

      this.authenticationService.login(
        this.form.get('username').value,
        this.form.get('password').value
      )
        .pipe(first())
        .subscribe(
          data => {
            this.loading = false;
            this.router.navigate([this.returnUrl ? this.returnUrl : this.authenticationService.getUserMainPageUrl()]);
          },
          error => {
            this.loginInvalid = true;
            console.log(error);
            this.loading = false;

            const dialogRef = this.dialog.open(InfoDialogComponent, {
              width: '300px',
              data: {
                title: 'Error',
                title_color: 'red',
                message: 'Login failed'
              }
            });
            dialogRef.afterClosed().subscribe(() => {});
          });
    }
  }

}
