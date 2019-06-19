import {Component, OnInit} from '@angular/core';
import {NetworkInitService} from '../../../../shared/services/network-init.service';
import {NetworkInitDto} from '../../../../shared/models/network/setup/NetworkInitDto';
import {Activation, ActivationParser} from '../../../../shared/models/network/shared/Activation';
import {LayerType, LayerTypeParser} from '../../../../shared/models/network/shared/LayerType';

@Component({
  selector: 'app-network-create',
  templateUrl: './network-create.component.html',
  styleUrls: ['./network-create.component.scss']
})
export class NetworkCreateComponent implements OnInit {

  networkInit: NetworkInitDto;
  layerCount = 0;
  deleteId: number;
  networkInits: NetworkInitDto[] = [];
  activationTypes =  ActivationParser.names();
  layerTypes = LayerTypeParser.names();

  constructor(private networkInitService: NetworkInitService) {
  }

  reset() {
    this.networkInit = {
      id: null,
      name: null,
      seed: null,
      learningRate: null,
      batchSize: null,
      nEpochs: null,
      nInputs: null,
      nOutputs: null,
      branchId: null,
      layers: []
    };
    this.layerCount = 0;
    this.deleteId = null;
  }

  ngOnInit() {
    this.reset();
  }

  loadAll() {
    this.networkInitService.getAll().subscribe(resp => this.networkInits = resp);
  }

  create() {
    this.networkInitService.create(this.networkInit).subscribe(() => this.reset());
  }

  remove() {
    this.networkInitService.delete(this.deleteId).subscribe(() => this.reset());
  }

  updateLayerCount() {
    while (this.layerCount != null && this.networkInit.layers.length < this.layerCount) {
      this.networkInit.layers.push({
        id: null,
        nInputs: null,
        nNodes: null,
        nOutputs: null,
        type: null,
        activation: null
      });
    }
    while (this.layerCount != null && this.networkInit.layers.length > this.layerCount) {
      this.networkInit.layers.pop();
    }
  }

}
