import {OfflineNodeDto} from './OfflineNodeDto';
import {LayerType} from '../shared/LayerType';

export interface OfflineLayerDto {
  id: number;
  type: LayerType;
  nodes: OfflineNodeDto[];
}
