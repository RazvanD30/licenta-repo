import {LayerType} from '../shared/LayerType';
import {Activation} from '../shared/Activation';

export interface LayerInitDto {
  id: number;
  nInputs: number;
  nNodes: number;
  nOutputs: number;
  type: LayerType;
  activation: Activation;
}
