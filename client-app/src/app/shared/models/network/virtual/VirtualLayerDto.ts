import {VirtualNodeDto} from './VirtualNodeDto';
import {LayerType} from '../shared/LayerType';
import {Activation} from '../shared/Activation';

export interface VirtualLayerDto {
  id: number;
  type: LayerType;
  activation: Activation;
  nodes: VirtualNodeDto[];
}
