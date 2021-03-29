import { Component, OnInit } from '@angular/core';
import {FakService} from '../services/fak.service';
import {KitDto} from '../models/fak.dto.model';
import {ActivatedRoute, ParamMap} from '@angular/router';
import { LocalStorageService } from '../services/local-storage.service';
import { FormControl ,  Validator, Validators } from '@angular/forms';

@Component({
  selector: 'app-fak',
  templateUrl: './fak.component.html',
  styleUrls: ['./fak.component.scss']
})
export class FakComponent implements OnInit {

  kit: KitDto;
  kitId: string;
  value: number = 1;
  isLoading: boolean;

  constructor(public kitService: FakService, public route: ActivatedRoute, private localStorage: LocalStorageService) { }

  ngOnInit() {
    this.route.paramMap.subscribe((paramMap: ParamMap) => {
      if (paramMap.has('kitId')) {
        this.kitId = paramMap.get('kitId');
        this.isLoading = true;
        this.kitService.getKit(+this.kitId)
          .subscribe(kitData => {
            this.isLoading = false;
            this.kit = kitData;
          });
      }
    });
  }

  onCartClick(kit){
    if(this.value >0 && this.value <20 ) {
      this.localStorage.addNKitsToStorage(kit,this.value);
    }
    else{
      alert("Enter valid quantity!");
      this.value = 1;
    }
  }
  onMinusClick(){
    if(this.value >0  )
      this.value--;
  }

  onPlusClick(){
   this.value++;
  }
}
