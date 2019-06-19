import {Activation} from './shared/Activation';
import {LayerType} from './shared/LayerType';


export interface LayerInit {
  id: number;
  nInputs: number;
  nNodes: number;
  nOutputs: number;
  type: LayerType;
  activation: Activation;
}
