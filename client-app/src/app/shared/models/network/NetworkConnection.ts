import {LinkDto} from './runtime/LinkDto';

export class NetworkConnection {

  private readonly sourceNetworkId: number;
  private readonly destinationNetworkId: number;
  private readonly _sourceLayerId: number;
  private readonly _destinationLayerId: number;
  private readonly _links: LinkDto[];


  constructor(sourceNetworkId: number, destinationNetworkId: number, sourceLayerId: number, destinationLayerId: number) {
    this.sourceNetworkId = sourceNetworkId;
    this.destinationNetworkId = destinationNetworkId;
    this._sourceLayerId = sourceLayerId;
    this._destinationLayerId = destinationLayerId;
    this._links = [];
  }

  get sourceLayerId(): number {
    return this._sourceLayerId;
  }

  get destinationLayerId(): number {
    return this._destinationLayerId;
  }

  get links(): LinkDto[] {
    return this._links;
  }
}
