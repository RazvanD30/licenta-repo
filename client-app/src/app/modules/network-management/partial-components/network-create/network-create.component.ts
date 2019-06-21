import {Component, OnInit} from '@angular/core';
import {NetworkInitService} from '../../../../shared/services/network-init.service';
import {NetworkInitDto} from '../../../../shared/models/network/init/NetworkInitDto';
import {Activation, ActivationParser} from '../../../../shared/models/network/shared/Activation';
import {LayerType, LayerTypeParser} from '../../../../shared/models/network/shared/LayerType';
import {BranchService} from '../../../../shared/services/branch.service';
import {BranchDto} from '../../../../shared/models/branch/BranchDto';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

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
  availableBranches: BranchDto[] = [];
  networkInitFormGroup: FormGroup;


  constructor(private networkInitService: NetworkInitService,
              private branchService: BranchService,
              private formBuilder: FormBuilder) {
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
    this.networkInitFormGroup = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(4)]],
      seed: ['', [Validators.required, Validators.min(0)]],
      learningRate: ['', [Validators.required, Validators.min(0.000001)]],
      batchSize: ['', [Validators.required, Validators.min(1)]],
      nEpochs: ['', [Validators.required, Validators.min(1)]],
      branchId: ['', Validators.required]
    });

    this.reset();
    this.branchService.getAllForUser(localStorage.getItem('username'))
      .subscribe(resp => this.availableBranches = resp);
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

  a() {
    console.log(this.networkInitFormGroup.controls.name.hasError('minlength'));
    console.log(this.networkInitFormGroup.errors);
  }
}
