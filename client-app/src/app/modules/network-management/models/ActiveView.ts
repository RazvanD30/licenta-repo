import {NetworkDto} from '../../../shared/models/network/runtime/NetworkDto';
import {SelectedTableType} from './SelectedTableType';

export class ActiveView {
  network: NetworkDto;
  tableType: SelectedTableType;
  additionalData;
}
