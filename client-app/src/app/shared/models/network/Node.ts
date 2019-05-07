import {Status} from './Status';
import {Layer} from './Layer';
import {LinkGui} from './gui/LinkGui';
import {Link} from './Link';

export interface Node {
  id: number;
  bias: number;
  links: Link[];
}

