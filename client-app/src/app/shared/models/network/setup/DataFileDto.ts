import {FileType} from '../shared/FileType';

export interface DataFileDto {
  id: number;
  networkId: number;
  classPath: string;
  type: FileType;
  nLabels: number;
}
