import {Component, Inject, OnInit, Renderer2, ViewChild} from '@angular/core';
import {NetworkRunService} from '../../../../core/services/network-run.service';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef, MatPaginator, MatSort, MatTabChangeEvent} from '@angular/material';
import {NetworkInit} from '../../../../shared/models/network/NetworkInit';
import {merge} from 'rxjs';
import {map, startWith, switchMap} from 'rxjs/operators';
import {NetworkInitService} from '../../../../core/services/network-init.service';
import {Network} from '../../../../shared/models/network/Network';
import {FormControl} from '@angular/forms';
import {Layer} from '../../../../shared/models/network/Layer';

@Component({
  selector: 'app-network-create',
  templateUrl: './network-create.component.html',
  styleUrls: ['./network-create.component.scss']
})
export class NetworkCreateComponent implements OnInit {


  networks: Network[];
  selectedTab: number;
  selectedNetworkForLayerWise: Network;
  selection = {
    networkConfig: true,
    networkRun: false,
    layerConfig: false
  };


  constructor(private networkRunService: NetworkRunService,
              private networkInitService: NetworkInitService,
              public dialog: MatDialog,
              private renderer: Renderer2) {
  }

  ngOnInit() {
    this.networkRunService.getAll().subscribe(networks =>
      this.networks = networks
    );
  }

  getLayersForNetwork(network: Network): Layer[] {
    return this.networks.find(n => n.id === network.id).layers;
  }

  setSelection(tabChangeEvent: MatTabChangeEvent) {
    this.selection = {
      networkConfig: false,
      networkRun: false,
      layerConfig: false
    };
    switch(tabChangeEvent.index){
      case 0:
        this.selection.networkConfig = true;
        break;
      case 1:
        this.selection.networkRun = true;
        break;
      case 2:
        this.selection.layerConfig = true;
        break;
    }
  }

  setSelectedNetworkForLayerWise(network: Network) {
    this.selection = {
      networkConfig: false,
      networkRun: false,
      layerConfig: true
    };
    this.selectedNetworkForLayerWise = network;
    this.selectedTab = 2;
  }

}


