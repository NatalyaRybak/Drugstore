import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../services/authentication.service";

@Component({
  selector: 'app-make-order-dialog',
  templateUrl: './make-order-dialog.component.html',
  styleUrls: ['./make-order-dialog.component.scss']
})
export class MakeOrderDialogComponent implements OnInit {

  public userBonuses;
  public orderId;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authenticationService: AuthenticationService,
    public thisDialogRef: MatDialogRef<MakeOrderDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data
  ) { }

  ngOnInit() {
    this.orderId = this.data.orderId;
    this.userBonuses = this.authenticationService.currentUserValue.bonuses;
    this.form = this.fb.group({
      bonuses: [0, [Validators.max(this.userBonuses), Validators.min(0)]],
      comment: ['']
    });
  }

  makeOrder() {
    if(this.form.valid) {
      this.thisDialogRef.close({
        status: "Ok",
        data: {
          bonuses: this.form.get('bonuses').value,
          comment: this.form.get('comment').value
        }
      });
    }
  }

  cancel() {
    this.thisDialogRef.close({
      status: "Cancel"
    });
  }

}
