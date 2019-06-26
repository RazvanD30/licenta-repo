import {LayerDto} from './LayerDto';

export interface NetworkDtoWithTestTrain {
  id: number;
  name: string;
  seed: number;
  learningRate: number;
  batchSize: number;
  nEpochs: number;
  nInputs: number;
  nOutputs: number;
  layers: LayerDto[];
  trainFileName: string;
  testFileName: string;
  trainFileNames: string[];
  testFileNames: string[];
}
