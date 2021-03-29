import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";
import {first} from "rxjs/operators";
import {ErrorStateMatcher} from "@angular/material";

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const invalidCtrl = !!(control && control.invalid && control.parent.dirty);
    const invalidParent = !!(control && control.parent && control.parent.invalid && control.parent.dirty);

    return (invalidCtrl || invalidParent);
  }
}

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  form: FormGroup;
  loading = false;
  error = null;
  returnUrl: string;

  matcher: ErrorStateMatcher;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private authenticationService: AuthenticationService,
              private fb: FormBuilder)
  {
    this.matcher = new MyErrorStateMatcher();
  }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'];
    if (this.authenticationService.currentUserValue) {
      console.log("already authed");
      this.router.navigate([this.returnUrl ? this.returnUrl : this.authenticationService.getUserMainPageUrl()]);
    }
    this.form = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', Validators.compose([Validators.email, Validators.required])],
      password: ['', Validators.required],
      confirmPass: ['']
    }, {validator: this.checkPasswords});
  }

  signup() {
    if(this.form.valid) {
      this.loading = true;
      this.error = null;

      this.authenticationService.register(
        this.form.get('name').value,
        this.form.get('surname').value,
        this.form.get('email').value,
        this.form.get('password').value
      )
        .pipe(first())
        .subscribe(
          data => {
            this.loading = false;
            this.router.navigate([this.returnUrl ? this.returnUrl : this.authenticationService.getUserMainPageUrl()]);
          },
          error => {
            console.log(error);
            this.loading = false;
          });
    }
  }

  checkPasswords(group: FormGroup) {
    let pass = group.get('password').value;
    let confirmPass = group.get('confirmPass').value;

    return pass === confirmPass ? null : { notSame: true }
  }

}
