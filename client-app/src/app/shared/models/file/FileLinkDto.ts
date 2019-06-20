import {FileType} from '../network/shared/FileType';

export interface FileLinkDto {
  networkId: number;
  fileName: string;
  fileType: FileType;
}
