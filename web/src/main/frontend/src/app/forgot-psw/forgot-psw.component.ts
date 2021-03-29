import {Component, Inject, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-forgot-psw',
  templateUrl: './forgot-psw.component.html',
  styleUrls: ['./forgot-psw.component.scss']
})
export class ForgotPswComponent implements OnInit {

  form: FormGroup;

  constructor(public thisDialogRef: MatDialogRef<ForgotPswComponent>, private fb: FormBuilder) { }

  ngOnInit() {
    this.form = this.fb.group({
      email: ['', Validators.required]
    });
  }

  onCancel() {
    this.thisDialogRef.close({
      status: 'Cancel'
    });
  }

  onSubmit() {
    if(this.form.valid) {
      this.thisDialogRef.close({
        status: 'Submit',
        email: this.form.get('email').value
      });
    }
  }
}
