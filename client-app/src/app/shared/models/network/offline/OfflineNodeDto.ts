import {OfflineNodeStatus} from './OfflineNodeStatus';
import {OfflineLinkDto} from './OfflineLinkDto';

export interface OfflineNodeDto {
  id: number;
  bias: number;
  value: number;
  status: OfflineNodeStatus;
  links: OfflineLinkDto[];
}
