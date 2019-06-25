import {FileType} from '../network/shared/FileType';

export interface FileLinkDto {
  networkName: string;
  fileName: string;
  fileType: FileType;
}
