import {Status} from './Status';
import {Layer} from './Layer';
import {LinkGui} from './gui/LinkGui';
import {Link} from './Link';

export class NeuralNode {

  private readonly _id: number;
  private _status: Status;
  private readonly _outputLinks: Link[];

  constructor(id: number, status: Status) {
    this._id = id;
    this._status = status;
    this._outputLinks = [];
  }

  get id(): number {
    return this._id;
  }

  get status(): Status {
    return this._status;
  }

  set status(value: Status) {
    this._status = value;
  }

  get outputLinks(): Link[] {
    return this._outputLinks;
  }
}

