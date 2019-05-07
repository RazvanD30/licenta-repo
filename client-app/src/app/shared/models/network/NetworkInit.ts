import {LayerInit} from './LayerInit';

export interface NetworkInit {
  id: number;
  name: string;
  seed: number;
  learningRate: number;
  batchSize: number;
  nEpochs: number;
  nInputs: number;
  nOutputs: number;
  layers: LayerInit[];
}
