import {Status} from './Status';
import {NodeGui} from './NodeGui';

export class LinkGui {

  sourceId: number;
  destinationId: number;
  private readonly _id: number;

  constructor(id: number, weight: number, status: Status, source: NodeGui, destination: NodeGui) {
    this._id = id;
    this._weight = weight;
    this._status = status;
    this._source = source;
    this._destination = destination;
  }

  private _weight: number;

  get weight(): number {
    return this._weight;
  }

  set weight(value: number) {
    this._weight = value;
  }

  private _status: Status;

  get status(): Status {
    return this._status;
  }

  set status(value: Status) {
    this._status = value;
  }

  private _source: NodeGui;

  get source(): NodeGui {
    return this._source;
  }

  set source(value: NodeGui) {
    this._source = value;
  }

  private _destination: NodeGui;

  get destination(): NodeGui {
    return this._destination;
  }

  set destination(value: NodeGui) {
    this._destination = value;
  }

  private _targetNodeId: number;

  get targetNodeId(): number {
    return this._targetNodeId;
  }

  get id(): number {
    return this._id;
  }

  public draw(context: CanvasRenderingContext2D): void {

    if (this._destination == null || this._source == null) {
      return;
    }

    this.updateStatus();
    context.globalCompositeOperation = 'source-over';
    context.beginPath();
    let color: string;
    switch (this.status) {
      case Status.UNDER_WATCH:
        color = 'rgba(0, 230, 64, 1)';
        context.lineWidth = (this.weight * 2 + 2);
        break;
      case Status.WAIT:
        context.lineWidth = (this.weight * 2 + 2);
        color = 'rgba(255,0,0,1)';
        break;
      case Status.IGNORED:
        context.lineWidth = Math.pow(Math.round(Math.abs(this.weight) * 200) / 100, 2) + 0.5;
        color = 'rgba(43, 116, 124,' + (this.weight / 3) + ')';
        break;
      case Status.INPUT:
        context.lineWidth = (this.weight * 2 + 2);
        color = 'rgb(153, 255, 51)';
        break;
      default:
        context.lineWidth = (this.weight * 2 + 1);
        color = 'rgba(43, 116, 124,' + (this.weight / 3) + ')';
        break;
    }
    context.moveTo(this.source.pos.x, this.source.pos.y);
    context.strokeStyle = color;
    context.lineTo(this.destination.pos.x, this.destination.pos.y);
    context.stroke();
    context.closePath();
  }

  private updateStatus() {
    if (this._source.status === Status.UNDER_WATCH && this._destination.status === Status.UNDER_WATCH) {
      this._status = Status.UNDER_WATCH;
    } else if (this._source.status === Status.BREAKPOINT) {
      this._status = Status.WAIT;
    } else if (this.destination.status === Status.INPUT) {
      this._status = Status.INPUT;
    } else {
      this._status = Status.IGNORED;
    }
  }
}
