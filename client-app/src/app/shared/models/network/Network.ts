import {Layer} from './Layer';

export class Network {

  private readonly _id: number;
  private readonly _name: string;
  private readonly _seed: number;
  private readonly _learningRate: number;
  private readonly _batchSize: number;
  private readonly _epochs: number;
  private readonly _inputs: number;
  private readonly _outputs: number;
  private readonly _firstLayer: Layer;


  constructor(id: number, name: string, seed: number, learningRate: number, batchSize: number, epochs: number, inputs: number, outputs: number, firstLayer: Layer) {
    this._id = id;
    this._name = name;
    this._seed = seed;
    this._learningRate = learningRate;
    this._batchSize = batchSize;
    this._epochs = epochs;
    this._inputs = inputs;
    this._outputs = outputs;
    this._firstLayer = firstLayer;
  }

  get id(): number {
    return this._id;
  }

  get name(): string {
    return this._name;
  }

  get seed(): number {
    return this._seed;
  }

  get learningRate(): number {
    return this._learningRate;
  }

  get batchSize(): number {
    return this._batchSize;
  }

  get epochs(): number {
    return this._epochs;
  }

  get inputs(): number {
    return this._inputs;
  }

  get outputs(): number {
    return this._outputs;
  }

  get firstLayer(): Layer {
    return this._firstLayer;
  }
}



