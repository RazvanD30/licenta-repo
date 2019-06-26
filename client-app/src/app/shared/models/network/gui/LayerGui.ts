import {NodeGui} from './NodeGui';
import {Pos} from './Pos';
import {Activation} from '../shared/Activation';

export class LayerGui {
  private readonly _id: number;
  private readonly _nodes: Map<number, NodeGui>;
  pos: Pos;
  activation: Activation;

  constructor(id: number, activation: Activation) {
    this._id = id;
    this.activation = activation;
    this._nodes = new Map<number, NodeGui>();
    this._previous = null;
    this._next = null;
  }

  private _previous: LayerGui;

  get previous(): LayerGui {
    return this._previous;
  }

  set previous(value: LayerGui) {
    this._previous = value;
  }

  private _next: LayerGui;

  get next(): LayerGui {
    return this._next;
  }

  set next(value: LayerGui) {
    this._next = value;
  }

  private _xPos: number;

  get xPos(): number {
    return this._xPos;
  }

  set xPos(value: number) {
    this._xPos = value;
  }

  get id(): number {
    return this._id;
  }

  get nodes(): Map<number, NodeGui> {
    return this._nodes;
  }

  drawNodes(context: CanvasRenderingContext2D) {
    this.nodes.forEach(node => node.draw(context));
  }

  drawText(context: CanvasRenderingContext2D) {
    this.nodes.forEach(node => node.drawText(context));
  }

  drawOutputLinks(context: CanvasRenderingContext2D) {
    for (const [key, node] of this.nodes) {
      for (const [linkKey, link] of node.outputLinks) {
        link.draw(context);
      }
    }
  }

  drawInputLinks(context: CanvasRenderingContext2D) {
    for (const [key, node] of this.nodes) {
      for (const [linkKey, link] of node.inputLinks) {
        link.draw(context);
      }
    }
  }

  drawLines(context: CanvasRenderingContext2D) {
    this.drawOutputLinks(context);
    this.drawInputLinks(context);
  }

  drawLayer(context: CanvasRenderingContext2D) {
    this.drawLines(context);
    this.drawNodes(context);
    this.drawText(context);
  }

  drawHeader(context: CanvasRenderingContext2D) {
    context.globalCompositeOperation = 'source-over';
    context.beginPath();
    context.font = 'bold 20px Futura';
    context.fillStyle = 'rgba(46, 49, 49, 1)';
    context.fillText('Layer', this.pos.x, this.pos.y);
    context.closePath();

    context.globalCompositeOperation = 'source-over';
    context.beginPath();
    context.font = 'bold 20px Futura';
    context.fillStyle = 'rgba(46, 49, 49, 1)';
    context.fillText( '' + this.id, this.pos.x, this.pos.y + 25);
    context.closePath();
  }

  addNode(node: NodeGui): void {
    this._nodes.set(node.id, node);
  }

  findNode(id: number): NodeGui {
    return this._nodes.get(id);
  }

  removeNode(id: number): boolean {
    return this._nodes.delete(id);
  }
}
