import {Category} from "./category.model";
import {ActiveComponent} from "./active-component.model";

export interface GoodFull {
  id: number;
  name: string;
  manufacturer: string;
  country: string;
  price: number;
  numAvailable: number;
  numInPack: string;
  dose: string;
  form: string;
  description: string;
  shelfLife: string;
  photo: string;
  category: Category;
  activeComponents: ActiveComponent[];
  prescriptionNeeded: boolean;
}
