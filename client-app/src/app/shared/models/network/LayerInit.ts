import {LayerType} from './LayerType';
import {Activation} from './Activation';

export interface LayerInit {
  id: number;
  nInputs: number;
  nNodes: number;
  nOutputs: number;
  type: LayerType;
  activation: Activation;
}
