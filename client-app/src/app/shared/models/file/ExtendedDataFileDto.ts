import {DataFileDto} from './DataFileDto';

export interface ExtendedDataFileDto {
  dataFile: DataFileDto;
  trainLinkWith: string[];
  testLinkWith: string[];
}
