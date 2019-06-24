import {VirtualNodeDto} from './VirtualNodeDto';
import {LayerType} from '../shared/LayerType';

export interface VirtualLayerDto {
  id: number;
  type: LayerType;
  nodes: VirtualNodeDto[];
}
