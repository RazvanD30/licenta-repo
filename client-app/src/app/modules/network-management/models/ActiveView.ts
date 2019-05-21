import {Network} from '../../../shared/models/network/Network';
import {SelectedTableType} from './SelectedTableType';

export class ActiveView {
  network: Network;
  tableType: SelectedTableType;
  additionalData;
}
