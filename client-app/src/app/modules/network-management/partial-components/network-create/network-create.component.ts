import {Component, OnInit} from '@angular/core';
import {NetworkInitService} from '../../../../shared/services/network-init.service';
import {NetworkInitDto} from '../../../../shared/models/network/init/NetworkInitDto';
import {ActivationParser} from '../../../../shared/models/network/shared/Activation';
import {LayerType, LayerTypeParser} from '../../../../shared/models/network/shared/LayerType';
import {BranchService} from '../../../../shared/services/branch.service';
import {BranchDto} from '../../../../shared/models/branch/BranchDto';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-network-create',
  templateUrl: './network-create.component.html',
  styleUrls: ['./network-create.component.scss']
})
export class NetworkCreateComponent implements OnInit {

  layerCount = 0;
  deleteId: number;
  networkInits: NetworkInitDto[] = [];
  activationTypes = ActivationParser.names();
  layerTypes = LayerTypeParser.names();
  hiddenLayerTypes: string[] = [];
  availableBranches: BranchDto[] = [];
  networkInitFormGroup: FormGroup;
  layerFormGroups: FormGroup[] = [];
  step: number;


  constructor(private networkInitService: NetworkInitService,
              private branchService: BranchService,
              private formBuilder: FormBuilder) {
  }

  reset() {

    this.layerCount = 0;
    this.deleteId = null;
  }


  removeLayer(idx: number) {
    this.layerFormGroups.splice(idx, 1);
    this.signalNodeChange(idx);
  }

  ngOnInit() {
    this.hiddenLayerTypes = this.layerTypes.filter(layer => layer !== 'INPUT' && layer !== 'OUTPUT');
    const inputLayer = this.formBuilder.group({
      nInputs: [{value: 0, disabled: true}, [Validators.required]],
      nNodes: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
      nOutputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
      type: [{value: LayerType.INPUT, disabled: true}, [Validators.required]],
      activation: ['', [Validators.required]]
    });

    inputLayer.controls.nNodes.valueChanges.subscribe(val => {
      if (inputLayer.controls.nNodes.valid === true) {
        this.signalNodeChange(0);
      }
    });

    const outputLayer = this.formBuilder.group({
      nInputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
      nNodes: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
      nOutputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
      type: [{value: LayerType.OUTPUT, disabled: true}, [Validators.required]],
      activation: ['', [Validators.required]]
    });

    outputLayer.controls.nNodes.valueChanges.subscribe(val => {
      if (outputLayer.controls.nNodes.valid === true) {
        this.signalNodeChange(0);
      }
    });

    this.layerFormGroups.push(inputLayer, outputLayer);

    this.networkInitFormGroup = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(4)]],
      seed: ['', [Validators.required, Validators.min(0)]],
      learningRate: ['', [Validators.required, Validators.min(0.000001), Validators.max(10)]],
      batchSize: ['', [Validators.required, Validators.min(1)]],
      nEpochs: ['', [Validators.required, Validators.min(1)]],
      branchId: ['', Validators.required],
      nInputs: ['', [Validators.required, Validators.min(1)]],
      nOutputs: ['', [Validators.required, Validators.min(1)]],
      defaultType: ['', []],
      defaultActivation: ['', []],
      defaultNNeurons: [0, []]
    });


    this.networkInitFormGroup.valueChanges.subscribe(val => {
      if (this.networkInitFormGroup.invalid === true) {
        this.setStep(null);
      }

      if (this.networkInitFormGroup.controls.defaultType.value !== '') {
        this.layerFormGroups.forEach(fg => {
          if (fg.controls.type.value === '') {
            fg.controls.type.setValue(this.networkInitFormGroup.controls.defaultType.value);
          }
        });
      }

      if (this.networkInitFormGroup.controls.defaultActivation.value !== '') {
        this.layerFormGroups.forEach(fg => {
          if (fg.controls.activation.value === '') {
            fg.controls.activation.setValue(this.networkInitFormGroup.controls.defaultActivation.value);
          }
        });
      }

      if (this.networkInitFormGroup.controls.defaultNNeurons.value !== 0) {
        this.layerFormGroups.forEach(fg => {
          if (fg.controls.nNodes.value === 0) {
            fg.controls.nNodes.setValue(this.networkInitFormGroup.controls.defaultNNeurons.value);
          }
        });
      }

      if (this.networkInitFormGroup.controls.nInputs.valid === true) {
        this.layerFormGroups[0].controls.nNodes.setValue(val.nInputs);
        this.configureInputNodes(1);
      }
      if (this.networkInitFormGroup.controls.nOutputs.valid === true) {
        this.layerFormGroups[this.layerFormGroups.length - 1].controls.nNodes.setValue(val.nOutputs);
        this.configureOutputNodes(this.layerFormGroups.length - 2);
      }
    });


    this.reset();
    this.branchService.getAllForUser(localStorage.getItem('username'))
      .subscribe(resp => this.availableBranches = resp);
  }

  getLayerTypesForIdx(idx: number) {
    if (this.isBoundaryLayer(idx)) {
      return this.layerTypes;
    }
    return this.hiddenLayerTypes;
  }

  signalNodeChange(idx: number) {
    this.configureInputNodes(idx + 1);
    this.configureOutputNodes(idx - 1);
    this.configureInputNodes(idx);
    this.configureOutputNodes(idx);
  }

  configureInputNodes(idx: number) {
    if (idx < 1 || idx > this.layerFormGroups.length - 1) {
      return;
    }
    const nodesControl = this.layerFormGroups[idx - 1].controls.nNodes;

    if (nodesControl.invalid === false) {
      this.layerFormGroups[idx].controls.nInputs.setValue(nodesControl.value);
    }
  }

  configureOutputNodes(idx: number) {
    if (idx < 0 || idx > this.layerFormGroups.length - 2) {
      return;
    }
    const nodesControl = this.layerFormGroups[idx + 1].controls.nNodes;
    if (nodesControl.invalid === false) {
      this.layerFormGroups[idx].controls.nOutputs.setValue(nodesControl.value);
    }
  }

  isBoundaryLayer(idx: number) {
    return idx === 0 || idx === this.layerFormGroups.length - 1;
  }

  isInputLayer(idx: number) {
    return idx === 0;
  }

  isOutputLayer(idx: number) {
    return idx === this.layerFormGroups.length - 1;
  }

  saveNetwork() {
    this.markFormGroupForValidation(this.networkInitFormGroup);
    const invalidFgs = this.layerFormGroups.reduce((n, fg) => {
      this.markFormGroupForValidation(fg);
      return fg.invalid === true ? 1 : 0;
    }, 0);
    if (this.networkInitFormGroup.invalid === true || invalidFgs > 0) {
      return;
    }

    const networkInit = {
      id: null,
      name: this.networkInitFormGroup.controls['name'].value,
      seed: this.networkInitFormGroup.controls['seed'].value,
      learningRate: this.networkInitFormGroup.controls['learningRate'].value,
      batchSize: this.networkInitFormGroup.controls['batchSize'].value,
      nEpochs: this.networkInitFormGroup.controls['nEpochs'].value,
      nInputs: this.networkInitFormGroup.controls['nInputs'].value,
      nOutputs: this.networkInitFormGroup.controls['nOutputs'].value,
      branchId: this.networkInitFormGroup.controls['branchId'].value,
      layers: []
    };
    this.layerFormGroups.forEach(fg => {
      const layer = {
        nInputs: fg.controls['nInputs'].value,
        nNodes: fg.controls['nNodes'].value,
        nOutputs: fg.controls['nOutputs'].value,
        type: fg.controls['type'].value,
        activation: fg.controls['activation'].value
      };
      networkInit.layers.push(layer);
    });
    this.networkInitService.create(networkInit).subscribe(() => this.reset());
  }


  markFormGroupForValidation(formGroup: FormGroup) {
    console.log(formGroup);
    formGroup.markAsTouched();
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsTouched({onlySelf: true});
      } else if (control instanceof FormGroup) {
        this.markFormGroupForValidation(control);
      }
    });
  }

  loadAll() {
    this.networkInitService.getAll().subscribe(resp => this.networkInits = resp);
  }


  remove() {
    this.networkInitService.delete(this.deleteId).subscribe(() => this.reset());
  }

  signalNodeChangesForLayersWithNodes(nNodes: number) {
    this.layerFormGroups.forEach((fg, index) => {
      console.log('SIGNALING');
      if (fg.controls.nNodes.valid === true && fg.controls.nNodes.value === nNodes) {
        console.log('detected change on ' + index);
        this.signalNodeChange(index);
      }
    });
  }

  addHiddenLayer() {
    const hiddenLayer = this.formBuilder.group({
      nInputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
      nNodes: [0, [Validators.required, Validators.min(1)]],
      nOutputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
      type: ['', [Validators.required]],
      activation: ['', [Validators.required]]
    });

    const idx = this.layerFormGroups.length - 1;
    this.layerFormGroups.splice(idx, 0, hiddenLayer);
    this.configureInputNodes(idx);
    this.configureOutputNodes(idx);
    this.layerFormGroups[idx].controls.nNodes.valueChanges.subscribe(val => {
      this.signalNodeChangesForLayersWithNodes(val);
    });

    if (this.networkInitFormGroup.controls.defaultType.value !== '') {
      hiddenLayer.controls.type.setValue(this.networkInitFormGroup.controls.defaultType.value);
    }

    if (this.networkInitFormGroup.controls.defaultActivation.value !== '') {
      hiddenLayer.controls.activation.setValue(this.networkInitFormGroup.controls.defaultActivation.value);
    }

    if (this.networkInitFormGroup.controls.defaultNNeurons.value !== 0) {
      hiddenLayer.controls.nNodes.setValue(this.networkInitFormGroup.controls.defaultNNeurons.value);
    }

  }

  setStep(step: number) {
    this.step = step;
  }
}
