import {NetworkIterationLogDto} from './NetworkIterationLogDto';

export interface NetworkTrainLogDto {
  createDateTime: string;
  accuracy: number;
  precision: number;
  recall: number;
  f1Score: number;
  networkIterationLogs: NetworkIterationLogDto[];
}
