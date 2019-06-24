import {Injectable} from '@angular/core';
import {LayerGui} from '../models/network/gui/LayerGui';
import {NetworkDto} from '../models/network/runtime/NetworkDto';
import {LayerDto} from '../models/network/runtime/LayerDto';
import {NodeGui} from '../models/network/gui/NodeGui';
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
        .map(targetNode => new NodeGui(targetNode.id,targetNode.status,currentLayerGui))
        .forEach(nodeGui => {
          currentLayerGui.addNode(nodeGui);
        });
      this._layers.set(currentLayerGui.id, currentLayerGui);
      currentLayer = currentLayer.next;
    }

    // initialize the connections
    currentLayer = network.firstLayer;
    while (currentLayer !== null) {
      currentLayer.nodes.forEach(targetNode => {
        targetNode.outputLinks.forEach(link => {
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




  get layers(): Map<number, LayerGui> {
    return this._layers;
  }

  get networkConnections(): LinkGui[] {
    return this._networkConnections;
  }
}
