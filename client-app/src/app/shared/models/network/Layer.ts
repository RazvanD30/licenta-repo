import {Node} from './Node';
import {Activation} from './shared/Activation';
import {LayerType} from './shared/LayerType';

export interface Layer {
  id: number;
  nInputs: number;
  nNodes: number;
  nOutputs: number;
  type: LayerType;
  activation: Activation;
  nodes: Node[];
}
