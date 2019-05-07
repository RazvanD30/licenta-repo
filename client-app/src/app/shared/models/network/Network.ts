import {Layer} from './Layer';

export interface Network {
  id: number;
  name: string;
  seed: number;
  learningRate: number;
  batchSize: number;
  nEpochs: number;
  nInputs: number;
  nOutputs: number;
  layers: Layer[];
}


