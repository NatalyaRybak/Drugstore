import { Component,Inject, OnInit } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-bonuses',
  templateUrl: './bonuses.component.html',
  styleUrls: ['./bonuses.component.scss']
})
export class BonusesComponent implements OnInit {

  public userBonuses;

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authenticationService: AuthenticationService,
    public thisDialogRef: MatDialogRef<BonusesComponent>,
    @Inject(MAT_DIALOG_DATA) public data) {
      
  }

  ngOnInit() {
    this.userBonuses = this.authenticationService.currentUserValue.bonuses;
    this.form = this.fb.group({
      bonuses: [0, [Validators.max(this.userBonuses), Validators.min(0)]]
    });
  }

  
  makeOrder() {
    if(this.form.valid) {
      this.thisDialogRef.close({
        status: "Ok",
        data: {
          bonuses: this.form.get('bonuses').value
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
