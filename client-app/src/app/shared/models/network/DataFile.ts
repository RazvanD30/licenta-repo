import {FileType} from './shared/FileType';


export interface DataFile {
  id: number;
  networkId: number;
  classPath: string;
  type: FileType;
  nLabels: number;
}
