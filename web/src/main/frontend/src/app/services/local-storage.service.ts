import { Injectable } from '@angular/core';
import {EventEmitter} from '@angular/core';
import { CartGood } from '../models/cart-good.model';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  private readonly storage: Storage;


  itemsArray: CartGood[];

  kitsArray: CartGood[];

  totalLength;
  total  ;
  subtotal ;
  onClick: EventEmitter<number> = new EventEmitter();

  onRemoveClick: EventEmitter<CartGood[]> = new EventEmitter();
  onKitRemoveClick: EventEmitter<CartGood[]> = new EventEmitter();

  onPlusMinusClick: EventEmitter<number> = new EventEmitter();


  constructor() {
    this.storage = window.localStorage;
    if (this.storage.getItem('goods')) {
      this.itemsArray = JSON.parse(localStorage.getItem('goods'));
    }
    else {
      this.itemsArray = [];
    }
    if (this.storage.getItem('kits')) {
      this.kitsArray = JSON.parse(localStorage.getItem('kits'));
    } 
    else {
      this.kitsArray = [];
    }
    this.totalLength = this.itemsArray.length+ this.kitsArray.length;
    this.getTotal();
    
   }

  get length(): number {
    return this.storage.length;
  }



  clear(): void {
    this.storage.clear();
  }

  getItem(key: string): string | null {
    return this.storage.getItem(key);
  }

  key(index: number): string | null {
    return this.storage.key(index);
  }

  removeItem(key: string): void {
    this.storage.removeItem(key);
  }

  setItem(key: string, value: string): void {

    this.storage.setItem(key, value);
  }

  addNGoodToStorage(good,n) {
    if (this.itemsArray.length != 0) {
      let b = false;
      for (let i = 0; i < this.itemsArray.length; i++) {
        if (this.itemsArray[i].object.id === good.id) {
          this.itemsArray[i].numOrdered += n ;
          b = true;
        }
      }
      if (b == false) {
        this.itemsArray.push({object: good, numOrdered: n});
      }

    } else {
      this.itemsArray.push({object: good, numOrdered: n});
    }

    this.setItem('goods', JSON.stringify(this.itemsArray));
    this.getTotal();
    this.totalLength = this.itemsArray.length + this.kitsArray.length;
    this.onClick.emit(this.totalLength);
    
  }
  

  removeNGoodfromStorage(good,n) {
    if (this.itemsArray.length != 0) {
      for (let i = 0; i < this.itemsArray.length; i++) {
        if (this.itemsArray[i].object.id === good.id) {
          if(this.itemsArray[i].numOrdered <= n){
            this.itemsArray.splice(i, 1);
          }
          else{
            this.itemsArray[i].numOrdered -= n ;
          }
          
        }
      }
      this.setItem('goods', JSON.stringify(this.itemsArray));
      this.getTotal();
      this.totalLength = this.itemsArray.length + this.kitsArray.length;
      this.onClick.emit(this.totalLength);
      
      this.onRemoveClick.emit(this.itemsArray);
    }
    
  }
  addNKitsToStorage(kit,n){
      if (this.kitsArray.length != 0) {
      let b = false;
      for (let i = 0; i < this.kitsArray.length; i++) {
        if (this.kitsArray[i].object.id === kit.id) {
          this.kitsArray[i].numOrdered += n ;
          b = true;
        }
      }
      if (b == false) {
        this.kitsArray.push({object: kit, numOrdered: n});
      }
    }
      else {
        this.kitsArray.push({object: kit, numOrdered: n});
      }
      this.setItem('kits', JSON.stringify(this.kitsArray));
      this.getTotal();
      this.totalLength = this.itemsArray.length + this.kitsArray.length;
      this.onClick.emit(this.totalLength);
    
  }
  removeNKitsFromStorage(kit, n){
    if (this.kitsArray.length != 0) {
      for (let i = 0; i < this.kitsArray.length; i++) {
        if (this.kitsArray[i].object.id === kit.id) {
          if(this.kitsArray[i].numOrdered <= n){
            this.kitsArray.splice(i, 1);
          }
          else{
            this.kitsArray[i].numOrdered -= n ;
          }
          
        }
      }
      this.setItem('kits', JSON.stringify(this.kitsArray));
      this.getTotal();
      this.totalLength = this.itemsArray.length + this.kitsArray.length;
      this.onClick.emit(this.totalLength);
      this.onKitRemoveClick.emit(this.kitsArray);
      
    }
  }

  getTotal(){
    this.total = 0;
    for(let i=0; i<this.itemsArray.length; i++){
      this.total += this.itemsArray[i].object.price * this.itemsArray[i].numOrdered;
    }
    for(let i=0; i<this.kitsArray.length; i++){
      this.total += this.kitsArray[i].object.price * this.kitsArray[i].numOrdered;
    }
    this.subtotal = this.total;
    this.onPlusMinusClick.emit(this.total);
  }

  onCheckOut(){
    this.itemsArray = [];
    this.removeItem('goods');
    this.kitsArray = [];
    this.removeItem('kits');
    this.onClick.emit(this.itemsArray.length + this.kitsArray.length);
    this.onRemoveClick.emit(this.itemsArray);
    this.onKitRemoveClick.emit(this.kitsArray);
  }

}
