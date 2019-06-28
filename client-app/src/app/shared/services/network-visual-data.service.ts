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
import {LayerType} from '../models/network/shared/LayerType';
import {Activation} from '../models/network/shared/Activation';
import {TIMEOUT} from '../config/timeout-config';

@Injectable({
  providedIn: 'root'
})
export class NetworkVisualDataService {
  public layerAdded = new EventEmitter<LayerGui>();
  public gotInputs = new EventEmitter<number>();
  public doneLoading = new EventEmitter();
  public pos: number;
  public layers: LayerGui[];

  constructor(private http: HttpClient) {
    this.pos = 0;
    this.layers = [];
  }

  private _virtualNetwork: VirtualNetworkDto;

  get virtualNetwork(): VirtualNetworkDto {
    return this._virtualNetwork;
  }

  set virtualNetwork(value: VirtualNetworkDto) {
    this.pos = 0;
    this.layers = [];
    this._virtualNetwork = value;
  }

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
      if (layerDto.type === LayerType.INPUT) {
        this.gotInputs.emit(layerDto.nodes.length);
      }
      const layerGui = new LayerGui(layerDto.id, layerDto.activation, layerDto.type);
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
        this.redoLinks(this.layers[this.pos - 2], this.layers[this.pos - 1]);
        this.layers[this.pos - 2].next = this.layers[this.pos - 1];
        this.layers[this.pos - 1].previous = this.layers[this.pos - 2];
      }
      this.layerAdded.emit(layerGui);
      if (this.pos >= this._virtualNetwork.nLayers) {
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
    return this.http.post<VirtualNetworkDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_VIRTUAL_SIMULATION.POST_CREATE, dto)
      .timeout(TIMEOUT);
  }

  getAllForNetworkId(networkId: number): Observable<VirtualNetworkDto[]> {
    return this.http.get<VirtualNetworkDto[]>
    (APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_VIRTUAL_SIMULATION.GET_ALL_FOR_NETWORK_ID + networkId).timeout(TIMEOUT);
  }

  getAllForNetworkName(name: string): Observable<VirtualNetworkDto[]> {
    return this.http.get<VirtualNetworkDto[]>
    (APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_VIRTUAL_SIMULATION.GET_ALL_FOR_NETWORK_NAME + name).timeout(TIMEOUT);
  }

  getLayerAtPos(virtualNetworkId: number, pos: number): Observable<VirtualLayerDto> {
    return this.http.post<VirtualLayerDto>
    (APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_VIRTUAL_SIMULATION.GET_LAYER_BY_VIRTUAL_ID_AT_POS + virtualNetworkId, pos)
      .timeout(TIMEOUT);
  }

  applySoftmax(layer: LayerGui) {
    layer.nodes.forEach(node => node.value = this.getX(node));
    let max = 0.0;
    let sum = 0.0;
    layer.nodes.forEach(node => {
      if (node.value >= max) {
        max = node.value;
      }
    });
    layer.nodes.forEach(node => {
      node.value = Math.exp(node.value - max);
      sum += node.value;
    });
    if (sum === 0) {
      layer.nodes.forEach(node => {
        node.value = 100 / layer.nodes.size;
      });
    } else {
      layer.nodes.forEach(node => {
        node.value /= sum;
      });
    }
  }

  applyActivation(targetLayer: LayerGui) {
    if (targetLayer.activation === Activation.SOFTMAX) {
      this.applySoftmax(targetLayer);
    } else {
      targetLayer.nodes.forEach(targetNode => {
        let value = this.applyActivationForNode(targetNode);
        if (Math.abs(value) > Number.MAX_VALUE) {
          value = Number.MAX_VALUE;
        }
        targetNode.value = value;
      });
    }
  }

  getX(targetNode: NodeGui) {
    let x = targetNode.bias;
    targetNode.inputLinks.forEach(link => {
      x += link.source.value * link.weight;
    });
    return x;
  }

  applyActivationForNode(targetNode: NodeGui) {
    const x = this.getX(targetNode);
    let DEFAULT_ALPHA;
    let OUTOFF;
    switch (targetNode.layer.activation) {
      case Activation.CUBE:
        return Math.pow(x, 3);
      case Activation.ELU:
        DEFAULT_ALPHA = 1.0;
        return x < 0 ? DEFAULT_ALPHA * (Math.exp(x) - 1.0) : x;
      case Activation.HARDSIGMOID:
        return Math.min(1, Math.max(0, 0.2 * x + 0.5));
      case Activation.HARDTANH:
        return x < 1 ? 1 : (x < -1 ? -1 : x);
      case Activation.IDENTITY:
        return x;
      case Activation.LEAKYRELU:
        DEFAULT_ALPHA = 0.01;
        return Math.max(0, x) + DEFAULT_ALPHA * Math.min(0, x);
      case Activation.RATIONALTANH:
        return 1.7189 * Math.tanh(2 * x / 3);
      case Activation.RECTIFIEDTANH:
        return Math.max(0, Math.tanh(x));
      case Activation.RELU:
        return Math.max(0, x);
      case Activation.RELU6:
        OUTOFF = 0.0;
        return Math.min(Math.max(x, 0), 6);
      case Activation.SWISH:
        return x / (1 + Math.exp(-x));
      case Activation.SIGMOID:
        return 1 / (1 + Math.exp(-x));
      case Activation.SOFTPLUS:
        return Math.log(1 + Math.exp(x));
      case Activation.TANH:
        const expPlusX = Math.exp(x);
        const expMinusX = Math.exp(-x);
        return (expPlusX - expMinusX) / (expPlusX + expMinusX);
      case Activation.THRESHOLDEDRELU:
        DEFAULT_ALPHA = 1.0;
        return x > DEFAULT_ALPHA ? x : 0;
    }
  }

  private findLayerForPosX(posX: number): LayerGui {
    for (const layer of this.layers) {
      const distance = this.getDistanceForAxis(layer.pos.x, posX);
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
}
