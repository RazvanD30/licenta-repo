import {Status} from './Status';

export class Link {
  private readonly _id: number;
  private _weight: number;
  private _status: Status;
  private readonly _sourceId: number;
  private readonly _destinationId: number;


  constructor(id: number, weight: number, status: Status, sourceId: number, destinationId: number) {
    this._id = id;
    this._weight = weight;
    this._status = status;
    this._sourceId = sourceId;
    this._destinationId = destinationId;
  }

  get weight(): number {
    return this._weight;
  }

  set weight(value: number) {
    this._weight = value;
  }

  get status(): Status {
    return this._status;
  }

  set status(value: Status) {
    this._status = value;
  }

  get id(): number {
    return this._id;
  }

  get sourceId(): number {
    return this._sourceId;
  }

  get destinationId(): number {
    return this._destinationId;
  }
}
