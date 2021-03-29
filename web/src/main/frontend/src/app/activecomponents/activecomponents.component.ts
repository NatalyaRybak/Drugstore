import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {ActiveComponent} from '../models/active-component.model';
import {ActiveComponentsService} from '../services/active-components.service';

@Component({
  selector: 'app-activecomponents',
  templateUrl: './activecomponents.component.html',
  styleUrls: ['./activecomponents.component.scss']
})
export class ActiveComponentsComponent implements OnInit {
  // tslint:disable-next-line:max-line-length
  alpha = ['А', 'Б', 'В', 'Г' , 'Д' , 'Е', 'Ж', 'З', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф'];
  components: ActiveComponent[];
  list1: ActiveComponent[];
  list2: ActiveComponent[];
  list3: ActiveComponent[];


  temp: ActiveComponent[];
  letter: string;
  length: number;
  chunk: number;
  hasLetter: boolean;
  isLoading: boolean;
  constructor(public route: ActivatedRoute, public activeService: ActiveComponentsService) { }

  ngOnInit() {

    this.route.paramMap.subscribe((paramMap: ParamMap) => {
      if (paramMap.has('letter')) {
        this.hasLetter = true;
        this.letter = paramMap.get('letter');
        this.isLoading = true;
        this.activeService.getActiveComponents()
          .subscribe(activeData => {
            this.isLoading = false;
            this.temp = activeData;
            const name = 'Aрма';
            this.components = this.temp.filter( ac => ac.name.charAt(0) == this.letter);
            console.log(name.charAt(0) == this.letter);
            this.length = this.components.length;
            if ( this.length < 3 ) {
              this.list1 = [];
              this.list2 = this.components;
              this.list3 = [];
            } else {
              this.chunk = this.length / 3;
              this.list1 = this.components.slice(0, this.chunk);
              this.list2 = this.components.slice(this.chunk, this.chunk * 2);
              this.list3 = this.components.slice(this.chunk * 2, this.length);
            }
          });

      } else {
        this.hasLetter = false;
        this.isLoading = true;
        this.activeService.getActiveComponents()
          .subscribe(activeData => {
            this.isLoading = false;
            this.components = activeData;
            this.length = this.components.length;
            if ( this.length < 3 ) {
              this.list2 = this.components;
            } else {
              this.chunk = this.length / 3;
              this.list1 = this.components.slice(0, this.chunk);
              this.list2 = this.components.slice(this.chunk, this.chunk * 2);
              this.list3 = this.components.slice(this.chunk * 2, this.length);
            }
          });
      }
    });



  }

}
