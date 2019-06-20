import {LayerInitDto} from './LayerInitDto';

export interface NetworkInitDto {
  id: number;
  name: string;
  seed: number;
  learningRate: number;
  batchSize: number;
  nEpochs: number;
  nInputs: number;
  nOutputs: number;
  branchId: number;
  layers: LayerInitDto[];
}
