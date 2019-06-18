import {LayerDto} from './LayerDto';

export interface NetworkDto {
  id: number;
  name: string;
  seed: number;
  learningRate: number;
  batchSize: number;
  nEpochs: number;
  nInputs: number;
  nOutputs: number;
  layers: LayerDto[];
}



