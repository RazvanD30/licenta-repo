import {Injectable} from '@angular/core';
import {LayerGui} from '../models/network/gui/LayerGui';
import {NetworkDto} from '../models/network/runtime/NetworkDto';
import {LayerDto} from '../models/network/runtime/LayerDto';
import {NeuralNodeGui} from '../models/network/gui/NeuralNodeGui';
import {LinkGui} from '../models/network/gui/LinkGui';
import {Pos} from '../models/network/gui/Pos';
import {HttpClient} from '@angular/common/http';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {NetworkConnection} from '../models/network/NetworkConnection';

@Injectable({
  providedIn: 'root'
})
export class NetworkDebugService {

  private readonly _layers: Map<number, LayerGui>;
  private readonly _networkConnections: LinkGui[];

  constructor(private http: HttpClient) {
    this._layers = new Map<number, LayerGui>();
    this._networkConnections = [];
  }

  getAll() {
    return this.http.get<NetworkDto[]>(APP_SETTINGS.URLS.NETWORK_DEBUG.GET_ALL);
  }

  getConnections(){
    return this.http.get<NetworkConnection[]>(APP_SETTINGS.URLS.NETWORK_DEBUG.GET_CONNECTIONS);
  }


  public loadNetwork(network: NetworkDto) {
    /*
    let currentLayer: LayerDto = network.firstLayer;


    let currentLayerGui: LayerGui = null;
    // intialize the layers and the nNodes on them (no connections yet)
    while (currentLayer !== null) {
      let previousLayerGui = currentLayerGui;
      currentLayerGui = new LayerGui(currentLayer.id);
      currentLayerGui.previous = previousLayerGui;
      if(previousLayerGui !== null){
        previousLayerGui.next = currentLayerGui;
      }
      currentLayer.nodes
        .map(offlineNode => new NeuralNodeGui(offlineNode.id,offlineNode.status,currentLayerGui))
        .forEach(nodeGui => {
          currentLayerGui.addNode(nodeGui);
        });
      this._layers.set(currentLayerGui.id, currentLayerGui);
      currentLayer = currentLayer.next;
    }

    // initialize the connections
    currentLayer = network.firstLayer;
    while (currentLayer !== null) {
      currentLayer.nodes.forEach(offlineNode => {
        offlineNode.outputLinks.forEach(link => {
          let layerGui = this._layers.get(currentLayer.id);
          let sourceNode = layerGui.findNode(link.sourceId);
          let destinationNode = layerGui.next.findNode(link.destinationId);
          let linkGui = new LinkGui(link.id, link.weight, link.status, sourceNode, destinationNode);
          sourceNode.addOutputLink(linkGui);
          destinationNode.addInputLink(linkGui);
        });
      });
      currentLayer = currentLayer.next;
    }
    */
    return null;
  }



  private findLayerForPosX(posX: number): LayerGui {
    for (let [key, layer] of this._layers) {
      const distance = this.getDistanceForAxis(layer.xPos, posX);
      if (this.isDistanceAcceptable(distance)) {
        return layer;
      }
    }
    return null;
  }

  public findNodeForPos(pos: Pos): NeuralNodeGui {
    const layerOnX = this.findLayerForPosX(pos.x);
    if(layerOnX !== null) {
      for (let [key, node] of layerOnX.nodes) {
        if (this.isDistanceAcceptable(this.getDistanceForPoints(pos, node.pos))) {
          return node;
        }
      }
    }
    return null;
  }

  private isDistanceAcceptable(distance: number) {
    return distance < NeuralNodeGui.RADIUS * 1.2 + 1;
  }

  private getDistanceForAxis(x1: number, x2: number) {
    return Math.abs(x1 - x2);
  }

  private getDistanceForPoints(pos1: Pos, pos2: Pos): number {
    return Math.sqrt(Math.pow(pos2.x - pos1.x, 2) + Math.pow(pos2.y - pos1.y, 2));
  }

  public getLayerForNodeId(id: number): LayerGui {

    let result: LayerGui = null;
    for (let [key, layer] of this._layers) {
      if (layer.findNode(id) !== null) {
        result = layer;
        break;
      }
    }
    return result;
  }


  get layers(): Map<number, LayerGui> {
    return this._layers;
  }

  get networkConnections(): LinkGui[] {
    return this._networkConnections;
  }
}
