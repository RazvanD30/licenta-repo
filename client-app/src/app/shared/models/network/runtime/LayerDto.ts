import {NodeDto} from './NodeDto';
import {LayerType} from '../shared/LayerType';
import {Activation} from '../shared/Activation';

export interface LayerDto {
  id: number;
  nInputs: number;
  nNodes: number;
  nOutputs: number;
  type: LayerType;
  activation: Activation;
  nodes: NodeDto[];
}
