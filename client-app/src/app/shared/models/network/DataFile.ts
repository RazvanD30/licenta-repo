import {FileType} from './FileType';

export interface DataFile {
  id: number;
  networkId: number;
  classPath: string;
  type: FileType;
  nLabels: number;
}
