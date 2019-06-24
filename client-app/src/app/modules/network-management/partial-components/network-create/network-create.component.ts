import {Component, OnInit} from '@angular/core';
import {NetworkInitService} from '../../../../shared/services/network-init.service';
import {NetworkInitDto} from '../../../../shared/models/network/init/NetworkInitDto';
import {ActivationParser} from '../../../../shared/models/network/shared/Activation';
import {LayerType, LayerTypeParser} from '../../../../shared/models/network/shared/LayerType';
import {BranchService} from '../../../../shared/services/branch.service';
import {BranchDto} from '../../../../shared/models/branch/BranchDto';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MatSnackBar} from '@angular/material';
import {LayerInitDto} from "../../../../shared/models/network/init/LayerInitDto";

@Component({
  selector: 'app-network-create',
  templateUrl: './network-create.component.html',
  styleUrls: ['./network-create.component.scss']
})
export class NetworkCreateComponent implements OnInit {

  networkInits: NetworkInitDto[] = [];
  activationTypes = ActivationParser.names();
  layerTypes = LayerTypeParser.names();
  hiddenLayerTypes: string[] = [];
  availableBranches: BranchDto[] = [];
  networkInitFormGroup: FormGroup;
  layerFormGroups: FormGroup[] = [];
  networkInitNames: string[] = [];
  step: number;
  waitingForResponse = false;
  isResettingForms = false;
  warnText = '';
  initializedFrom: string;


  constructor(private networkInitService: NetworkInitService,
              private branchService: BranchService,
              private formBuilder: FormBuilder,
              private snackBar: MatSnackBar) {
  }


  removeLayer(idx: number) {
    this.layerFormGroups.splice(idx, 1);
    this.signalNodeChange(idx);
  }

  ngOnInit() {
    this.hiddenLayerTypes = this.layerTypes.filter(layer => layer !== 'INPUT' && layer !== 'OUTPUT');
    this.resetForms(null);
    this.branchService.getAllForUser(localStorage.getItem('username'))
      .subscribe(resp => this.availableBranches = resp);
    this.networkInitService.getAllNames().subscribe(names => this.networkInitNames = names);
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
      this.warnText = 'Saving is not possible due to invalid fields in the form';
      return;
    }
    this.warnText = '';

    const networkInit = {
      id: null,
      name: this.networkInitFormGroup.controls.name.value,
      seed: this.networkInitFormGroup.controls.seed.value,
      learningRate: this.networkInitFormGroup.controls.learningRate.value,
      batchSize: this.networkInitFormGroup.controls.batchSize.value,
      nEpochs: this.networkInitFormGroup.controls.nEpochs.value,
      nInputs: this.networkInitFormGroup.controls.nInputs.value,
      nOutputs: this.networkInitFormGroup.controls.nOutputs.value,
      branchId: this.networkInitFormGroup.controls.branchId.value,
      layers: []
    };
    this.layerFormGroups.forEach(fg => {
      const layer = {
        nInputs: fg.controls.nInputs.value,
        nNodes: fg.controls.nNodes.value,
        nOutputs: fg.controls.nOutputs.value,
        type: fg.controls.type.value,
        activation: fg.controls.activation.value
      };
      networkInit.layers.push(layer);
    });
    this.disableAllForms();
    this.waitingForResponse = true;

    const subscriber = this.networkInitService.create(networkInit)
      .subscribe(() => {
        this.waitingForResponse = false;
        this.enableAllForms();
        this.snackBar.open('Network saved', 'Dismiss');
        this.networkInitService.getAllNames().subscribe(names => this.networkInitNames = names);
      }, error => {
        this.waitingForResponse = false;
        this.enableAllForms();
        this.snackBar.open('Error encountered while saving the network. Operation failed.', 'Dismiss');
      });
    setTimeout(() => {
      subscriber.unsubscribe();
      this.waitingForResponse = false;
      this.enableAllForms();
      this.snackBar.open('Operation took too long. Please check in the configuration module if the network was saved.', 'Dismiss');
    }, 120000);
  }

  disableAllForms() {
    this.networkInitFormGroup.disable();
    this.layerFormGroups.forEach(fg => fg.disable());
  }

  enableAllForms() {
    this.networkInitFormGroup.enable();
    this.layerFormGroups.forEach(fg => {
      fg.enable();
      fg.controls.nInputs.disable();
      fg.controls.nOutputs.disable();
    });
    this.layerFormGroups[0].controls.type.disable();
    this.layerFormGroups[this.layerFormGroups.length - 1].controls.type.disable();
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


  signalNodeChangesForLayersWithNodes(nNodes: number) {
    this.layerFormGroups.forEach((fg, index) => {
      if (fg.controls.nNodes.valid === true && fg.controls.nNodes.value === nNodes) {
        this.signalNodeChange(index);
      }
    });
  }

  addHiddenLayer(model: LayerInitDto) {
    let hiddenLayer;
    if (model == null) {
      hiddenLayer = this.formBuilder.group({
        nInputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
        nNodes: [0, [Validators.required, Validators.min(1)]],
        nOutputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
        type: ['', [Validators.required]],
        activation: ['', [Validators.required]]
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
    } else {
      hiddenLayer = this.formBuilder.group({
        nInputs: [{value: model.nInputs, disabled: true}, [Validators.required, Validators.min(1)]],
        nNodes: [model.nNodes, [Validators.required, Validators.min(1)]],
        nOutputs: [{value: model.nOutputs, disabled: true}, [Validators.required, Validators.min(1)]],
        type: [model.type, [Validators.required]],
        activation: [model.activation, [Validators.required]]
      });
    }
    const idx = this.layerFormGroups.length - 1;
    this.layerFormGroups.splice(idx, 0, hiddenLayer);
    this.configureInputNodes(idx);
    this.configureOutputNodes(idx);
    this.layerFormGroups[idx].controls.nNodes.valueChanges.subscribe(val => {
      this.signalNodeChangesForLayersWithNodes(val);
    });
  }

  setStep(step: number) {
    this.step = step;
  }

  resetForms(model: NetworkInitDto) {
    this.setStep(null);

    if (model == null) {
      this.initializedFrom = null;
    }

    let inputLayer;
    if (model == null) {
      inputLayer = this.formBuilder.group({
        nInputs: [{value: 0, disabled: true}, [Validators.required]],
        nNodes: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
        nOutputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
        type: [{value: LayerType.INPUT, disabled: true}, [Validators.required]],
        activation: ['', [Validators.required]]
      });
    } else {
      const modelInput = model.layers.find(l => l.type === LayerType.INPUT);
      inputLayer = this.formBuilder.group({
        nInputs: [{value: modelInput.nInputs, disabled: true}, [Validators.required]],
        nNodes: [{value: modelInput.nNodes, disabled: true}, [Validators.required, Validators.min(1)]],
        nOutputs: [{value: modelInput.nOutputs, disabled: true}, [Validators.required, Validators.min(1)]],
        type: [{value: LayerType.INPUT, disabled: true}, [Validators.required]],
        activation: [modelInput.activation, [Validators.required]]
      });
    }
    inputLayer.controls.nNodes.valueChanges.subscribe(val => {
      if (inputLayer.controls.nNodes.valid === true) {
        this.signalNodeChange(0);
      }
    });

    let outputLayer;
    if (model == null) {
      outputLayer = this.formBuilder.group({
        nInputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
        nNodes: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
        nOutputs: [{value: 0, disabled: true}, [Validators.required, Validators.min(1)]],
        type: [{value: LayerType.OUTPUT, disabled: true}, [Validators.required]],
        activation: ['', [Validators.required]]
      });
    } else {
      const modelOutput = model.layers.find(l => l.type === LayerType.OUTPUT);
      outputLayer = this.formBuilder.group({
        nInputs: [{value: modelOutput.nInputs, disabled: true}, [Validators.required]],
        nNodes: [{value: modelOutput.nNodes, disabled: true}, [Validators.required, Validators.min(1)]],
        nOutputs: [{value: modelOutput.nOutputs, disabled: true}, [Validators.required, Validators.min(1)]],
        type: [{value: LayerType.OUTPUT, disabled: true}, [Validators.required]],
        activation: [modelOutput.activation, [Validators.required]]
      });
    }
    outputLayer.controls.nNodes.valueChanges.subscribe(val => {
      if (outputLayer.controls.nNodes.valid === true) {
        this.signalNodeChange(0);
      }
    });

    this.layerFormGroups.splice(0, this.layerFormGroups.length);
    this.layerFormGroups.push(inputLayer, outputLayer);

    if (model == null) {
      this.networkInitFormGroup = this.formBuilder.group({
        loadFromNetworkName: ['', []],
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
    } else {
      this.networkInitFormGroup = this.formBuilder.group({
        loadFromNetworkName: [model.name, []],
        name: ['', [Validators.required, Validators.minLength(4)]],
        seed: [model.seed, [Validators.required, Validators.min(0)]],
        learningRate: [model.learningRate, [Validators.required, Validators.min(0.000001), Validators.max(10)]],
        batchSize: [model.batchSize, [Validators.required, Validators.min(1)]],
        nEpochs: [model.nEpochs, [Validators.required, Validators.min(1)]],
        branchId: [model.branchId, Validators.required],
        nInputs: [model.nInputs, [Validators.required, Validators.min(1)]],
        nOutputs: [model.nOutputs, [Validators.required, Validators.min(1)]],
        defaultType: ['', []],
        defaultActivation: ['', []],
        defaultNNeurons: [0, []]
      });
    }


    this.networkInitFormGroup.valueChanges.subscribe(val => {
      if (this.networkInitFormGroup.invalid === true) {
        this.setStep(null);
      }

      if (this.networkInitFormGroup.controls.loadFromNetworkName.value !== ''
        && this.initializedFrom !== this.networkInitFormGroup.controls.loadFromNetworkName.value) {
        this.initializedFrom = this.networkInitFormGroup.controls.loadFromNetworkName.value;
        this.loadFromConfig(this.initializedFrom);
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
    if (model != null && model.layers.length > 2) {
      for (let i = 1; i < model.layers.length - 1; i++) {
        this.addHiddenLayer(model.layers[i]);
      }
      for (let i = 0; i < model.layers.length; i++) {
        this.configureInputNodes(i);
        this.configureOutputNodes(i);
      }
    }
    this.isResettingForms = false;
  }

  loadFromConfig(name: string) {
    this.networkInitService.getByName(name)
      .subscribe(networkInit => {
        console.log(networkInit);
        this.resetForms(networkInit);
      });
  }
}
