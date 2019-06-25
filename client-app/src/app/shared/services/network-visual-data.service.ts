import {EventEmitter, Injectable} from '@angular/core';
import {LayerGui} from '../models/network/gui/LayerGui';
import {HttpClient} from '@angular/common/http';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {Observable} from 'rxjs';
import {VirtualNetworkDto} from '../models/network/virtual/VirtualNetworkDto';
import {VirtualLayerDto} from '../models/network/virtual/VirtualLayerDto';
import {NodeGui} from '../models/network/gui/NodeGui';
import {Status, StatusInternal} from '../models/network/gui/Status';
import {LinkGui} from '../models/network/gui/LinkGui';
import {Pos} from '../models/network/gui/Pos';

@Injectable({
  providedIn: 'root'
})
export class NetworkVisualDataService {
  public layerAdded = new EventEmitter<LayerGui>();
  public doneLoading = new EventEmitter();
  public pos: number;
  public layers: LayerGui[];

  constructor(private http: HttpClient) {
    this.pos = 0;
    this.layers = [];
  }

  private _virtualNetwork: VirtualNetworkDto;


  redoLinks(previousLayer: LayerGui, currentLayer: LayerGui) {
    for (const [key1, prevNode] of previousLayer.nodes) {
      for (const [key2, outputLink] of prevNode.outputLinks) {
        outputLink.destination = currentLayer.nodes.get(outputLink.destinationId);
      }
    }
    for (const [key1, currNode] of currentLayer.nodes) {
      for (const [key2, inputLink] of currNode.inputLinks) {
        inputLink.source = previousLayer.nodes.get(inputLink.sourceId);
      }
    }
  }


  getNextLayer() {
    const status = new Status(StatusInternal.IGNORE);
    if (this._virtualNetwork == null) {
      return;
    }
    this.getLayerAtPos(this._virtualNetwork.id, this.pos).subscribe(layerDto => {
      const layerGui = new LayerGui(layerDto.id);
      layerDto.nodes.forEach(nodeDto => {
        const nodeGui = new NodeGui(nodeDto.id, nodeDto.bias, status.fromInternal(nodeDto.status), layerGui);
        layerGui.addNode(nodeGui);

        nodeDto.outputLinks.forEach(link => {
          const linkGui = new LinkGui(link.id, link.weight, Status.IGNORED, nodeGui, null);
          nodeGui.addOutputLink(linkGui);
          linkGui.destinationId = link.destinationNodeId;
        });
        nodeDto.inputLinks.forEach(link => {
          const linkGui = new LinkGui(link.id, link.weight, Status.IGNORED, null, nodeGui);
          nodeGui.addInputLink(linkGui);
          linkGui.sourceId = link.sourceNodeId;
        });
      });

      this.layers.push(layerGui);
      this.pos++;
      if (this.pos > 1) {
        this.layers[this.pos - 2].next = this.layers[this.pos - 1];
        this.layers[this.pos - 1].previous = this.layers[this.pos - 2];
        this.redoLinks(this.layers[this.pos - 2], this.layers[this.pos - 1]);
      }
      this.layerAdded.emit(layerGui);
      if (this.pos < this._virtualNetwork.nLayers) {
        this.getNextLayer();
      } else {
        this.doneLoading.emit();
      }
    });
  }

  public findNodeForPos(pos: Pos): NodeGui {
    const layerOnX = this.findLayerForPosX(pos.x);
    if (layerOnX !== null) {
      for (const [key, node] of layerOnX.nodes) {
        if (this.isDistanceAcceptable(this.getDistanceForPoints(pos, node.pos))) {
          return node;
        }
      }
    }
    return null;
  }

  public getLayerForNodeId(id: number): LayerGui {

    let result: LayerGui = null;
    for (const layer of this.layers) {
      if (layer.findNode(id) !== null) {
        result = layer;
        break;
      }
    }
    return result;
  }

  create(dto: VirtualNetworkDto): Observable<VirtualNetworkDto> {
    return this.http.post<VirtualNetworkDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_VIRTUAL_SIMULATION.POST_CREATE, dto);
  }

  getAllForNetworkId(networkId: number): Observable<VirtualNetworkDto[]> {
    return this.http.get<VirtualNetworkDto[]>
    (APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_VIRTUAL_SIMULATION.GET_ALL_FOR_NETWORK_ID + networkId);
  }

  getAllForNetworkName(name: string): Observable<VirtualNetworkDto[]> {
    return this.http.get<VirtualNetworkDto[]>
    (APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_VIRTUAL_SIMULATION.GET_ALL_FOR_NETWORK_NAME + name);
  }

  getLayerAtPos(virtualNetworkId: number, pos: number): Observable<VirtualLayerDto> {
    return this.http.post<VirtualLayerDto>
    (APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_VIRTUAL_SIMULATION.GET_LAYER_BY_VIRTUAL_ID_AT_POS + virtualNetworkId, pos);
  }

  private findLayerForPosX(posX: number): LayerGui {
    for (const layer of this.layers) {
      const distance = this.getDistanceForAxis(layer.xPos, posX);
      if (this.isDistanceAcceptable(distance)) {
        return layer;
      }
    }
    return null;
  }

  private isDistanceAcceptable(distance: number) {
    return distance < NodeGui.RADIUS * 1.2 + 1;
  }

  private getDistanceForAxis(x1: number, x2: number) {
    return Math.abs(x1 - x2);
  }

  private getDistanceForPoints(pos1: Pos, pos2: Pos): number {
    return Math.sqrt(Math.pow(pos2.x - pos1.x, 2) + Math.pow(pos2.y - pos1.y, 2));
  }

  get virtualNetwork(): VirtualNetworkDto {
    return this._virtualNetwork;
  }

  set virtualNetwork(value: VirtualNetworkDto) {
    this.pos = 0;
    this.layers = [];
    this._virtualNetwork = value;
  }
}
