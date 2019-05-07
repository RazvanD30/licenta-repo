import {Node} from './Node';
import {LayerType} from './LayerType';
import {Activation} from './Activation';

export interface Layer {
  id: number;
  nInputs: number;
  nNodes: number;
  nOutputs: number;
  type: LayerType;
  activation: Activation;
  nodes: Node[];
}
