import {KitLinesDto} from './fak.lines.dto.model';

export interface KitDto {
  id: number;
  title: string;
  description: string;
  price: number;
  photo: string;
  kitLines: KitLinesDto[];
}
