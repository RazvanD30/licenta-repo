import {Status} from '../gui/Status';
import {LayerDto} from './LayerDto';
import {LinkGui} from '../gui/LinkGui';
import {LinkDto} from './LinkDto';

export interface NodeDto {
  id: number;
  bias: number;
  outputLinks: LinkDto[];
}

