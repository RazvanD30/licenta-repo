import {NeuralNodeGui} from './NeuralNodeGui';
import {Status} from '../Status';

export class LayerGui {
  private readonly _id: number;
  private readonly _nodes: Map<number,NeuralNodeGui>;
  private _previous: LayerGui;
  private _next: LayerGui;
  private _xPos: number;

  constructor(id: number) {
    this._id = id;
    this._nodes = new Map<number,NeuralNodeGui>();
    this._previous = null;
    this._next = null;
  }


  drawNodes(context: CanvasRenderingContext2D){
    this.nodes.forEach(node => node.draw(context));
  }

  drawText(context: CanvasRenderingContext2D){
    this.nodes.forEach(node => node.drawText(context));
  }

  drawLines(context: CanvasRenderingContext2D){
    this.nodes.forEach(node => {
      if(node.status === Status.IGNORED) {
        if(node.layer.next !== null) {
          node.outputLinks.forEach(link => link.draw(context));
        }
        if(node.layer.previous !== null) {
          node.inputLinks.forEach(link => link.draw(context));
        }
      }
    });
    this.nodes.forEach(node => {
      if(node.status !== Status.IGNORED) {
        if(node.layer.next !== null) {
          node.outputLinks.forEach(link => link.draw(context));
        }
        if(node.layer.previous !== null) {
          node.inputLinks.forEach(link => link.draw(context));
        }
      }
    });
  }

  drawLayer(context: CanvasRenderingContext2D){
    this.drawLines(context);
    this.drawNodes(context);
    this.drawText(context);
  }

  get previous(): LayerGui {
    return this._previous;
  }

  set previous(value: LayerGui) {
    this._previous = value;
  }

  get next(): LayerGui {
    return this._next;
  }

  set next(value: LayerGui) {
    this._next = value;
  }

  get id(): number {
    return this._id;
  }

  get nodes(): Map<number, NeuralNodeGui> {
    return this._nodes;
  }

  addNode(node: NeuralNodeGui): void {
    this._nodes.set(node.id, node);
  }

  findNode(id: number): NeuralNodeGui {
    return this._nodes.get(id);
  }

  removeNode(id: number): boolean {
    return this._nodes.delete(id);
  }

  get xPos(): number {
    return this._xPos;
  }

  set xPos(value: number) {
    this._xPos = value;
  }
}
