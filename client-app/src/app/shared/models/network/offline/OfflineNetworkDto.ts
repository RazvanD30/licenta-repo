import {OfflineLayerDto} from './OfflineLayerDto';

export interface OfflineNetworkDto {
  id: number;
  name: string;
  layers: OfflineLayerDto[];
}
