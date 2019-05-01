import {NeuralNode} from './NeuralNode';

export class Layer {

  private readonly _id: number;
  private readonly _nodes: NeuralNode[];
  private _next: Layer;

  constructor(id: number) {
    this._id = id;
    this._nodes = [];
    this._next = null;
  }

  get next(): Layer {
    return this._next;
  }

  set next(value: Layer) {
    this._next = value;
  }

  get id(): number {
    return this._id;
  }

  get nodes(): NeuralNode[] {
    return this._nodes;
  }
}
