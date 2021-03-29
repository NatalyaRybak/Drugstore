import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
  selector: 'app-info-dialog',
  templateUrl: './info-dialog.component.html',
  styleUrls: ['./info-dialog.component.scss']
})
export class InfoDialogComponent implements OnInit {

  title = '';
  message = '';
  title_color = '';

  constructor(
    public thisDialogRef: MatDialogRef<InfoDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data)
  {
    thisDialogRef.disableClose = true;
  }

  ngOnInit() {
    this.title = this.data.title;
    this.message = this.data.message;
    this.title_color = this.data.title_color;
  }

  onClose() {
    this.thisDialogRef.close({
      status: "OK"
    });
  }
}
