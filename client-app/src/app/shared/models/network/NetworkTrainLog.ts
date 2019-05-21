import {NetworkIterationLog} from './NetworkIterationLog';

export interface NetworkTrainLog {
  createDateTime: string;
  accuracy: number;
  precision: number;
  recall: number;
  f1Score: number;
  networkIterationLogs: NetworkIterationLog[];
}
