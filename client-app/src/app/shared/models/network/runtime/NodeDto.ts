import {LinkDto} from './LinkDto';

export interface NodeDto {
  id: number;
  bias: number;
  outputLinks: LinkDto[];
}

