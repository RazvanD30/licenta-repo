import {Pos} from './Pos';
import {Status} from './Status';
import {LinkGui} from './LinkGui';
import {LayerGui} from './LayerGui';

export class NodeGui {

  private _id: number;
  private _bias: number;
  private _status: Status;
  private readonly _inputLinks: Map<number, LinkGui>;
  private readonly _outputLinks: Map<number, LinkGui>;
  private _pos: Pos;
  public static readonly RADIUS = 10;
  private _layer: LayerGui;

  public constructor(id: number, bias: number, status: Status, layer: LayerGui) {
    this._id = id;
    this._bias = bias;
    this._status = status;
    this._layer = layer;
    this._inputLinks = new Map<number, LinkGui>();
    this._outputLinks = new Map<number, LinkGui>();
  }

  public draw(context: CanvasRenderingContext2D): void {
    let innerColor;
    switch (this._status) {
      case Status.UNDER_WATCH:
        innerColor = 'rgb(0, 230, 64)';
        break;
      case Status.WAIT:
        innerColor = 'rgb(228, 132, 72)';
        break;
      case Status.IGNORED:
        innerColor = 'rgb(58, 182, 196)';
        break;
      case Status.BREAKPOINT:
        innerColor = 'rgb(230, 75, 75)';
        break;
      case Status.INPUT:
        innerColor = 'rgb(153, 255, 51)';
        break;
      default:
        innerColor = 'rgb(58, 182, 196)';
        break;
    }
    context.globalCompositeOperation = 'source-over';
    context.beginPath();
    context.arc(this.pos.x, this.pos.y, NodeGui.RADIUS, 0, 2 * Math.PI, false);
    context.fillStyle = 'rgba(46, 49, 49, 1)';
    context.fill();
    context.closePath();

    context.beginPath();
    context.arc(this.pos.x, this.pos.y, NodeGui.RADIUS * 0.4, 0, 2 * Math.PI, false);
    context.fillStyle = innerColor;
    context.fill();
    context.closePath();
  }

  public drawText(context: CanvasRenderingContext2D): void {
    context.globalCompositeOperation = 'source-over';
    context.beginPath();
    context.font = '20px Futura';
    context.fillStyle = 'rgba(46, 49, 49, 1)';
    context.fillText('' + this.id, this.pos.x + NodeGui.RADIUS * 1.1, this.pos.y);
    context.closePath();
  }

  public drawLinks(context: CanvasRenderingContext2D): void {
    this.outputLinks.forEach(link => {
      link.draw(context);
    });
    this.inputLinks.forEach(link => {
      link.draw(context);
    });
  }

  get pos(): Pos {
    return this._pos;
  }

  set pos(value: Pos) {
    this._pos = value;
  }

  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  get bias(): number {
    return this._bias;
  }

  set bias(value: number) {
    this._bias = value;
  }

  get status(): Status {
    return this._status;
  }

  set status(value: Status) {
    this._status = value;
  }

  get inputLinks(): Map<number, LinkGui> {
    return this._inputLinks;
  }

  get outputLinks(): Map<number, LinkGui> {
    return this._outputLinks;
  }

  addOutputLink(linkGui: LinkGui): void {
    this.outputLinks.set(linkGui.id, linkGui);
  }

  addInputLink(linkGui: LinkGui): void {
    this.inputLinks.set(linkGui.id, linkGui);
  }

  removeInputLink(linkGui: LinkGui): boolean {
    return this.inputLinks.delete(linkGui.id);
  }

  removeOutputLink(linkGui: LinkGui): boolean {
    return this.outputLinks.delete(linkGui.id);
  }

  findInputLink(id: number) {
    return this.inputLinks.get(id);
  }

  findOutputLink(id: number) {
    return this.outputLinks.get(id);
  }


  get layer(): LayerGui {
    return this._layer;
  }

  set layer(value: LayerGui) {
    this._layer = value;
  }
}
