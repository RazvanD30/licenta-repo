import {VirtualLinkDto} from './VirtualLinkDto';
import {StatusInternal} from '../gui/Status';

export interface VirtualNodeDto {
  id: number;
  bias: number;
  value: number;
  status: StatusInternal;
  outputLinks: VirtualLinkDto[];
  inputLinks: VirtualLinkDto[];
}
