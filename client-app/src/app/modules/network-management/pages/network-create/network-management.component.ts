import {Component, OnInit, Renderer2} from '@angular/core';
import {NetworkRunService} from '../../../../core/services/network-run.service';
import {MatDialog} from '@angular/material';
import {NetworkInitService} from '../../../../core/services/network-init.service';
import {NetworkDto} from '../../../../shared/models/network/runtime/NetworkDto';
import {faTimes} from '@fortawesome/free-solid-svg-icons';
import {SelectedTableType} from '../../models/SelectedTableType';
import {ActiveView} from '../../models/ActiveView';


@Component({
  selector: 'app-network-create',
  templateUrl: './network-management.component.html',
  styleUrls: ['./network-management.component.scss']
})
export class NetworkManagementComponent implements OnInit {


  networks: NetworkDto[];
  faTimes = faTimes;

  activeViews: ActiveView[] = [];

  selectedView: ActiveView;

  constructor(private networkRunService: NetworkRunService,
              private networkInitService: NetworkInitService,
              public dialog: MatDialog,
              private renderer: Renderer2) {
  }

  ngOnInit() {
    this.networkRunService.getAll().subscribe(networks => {
      this.networks = networks;
      this.selectedView = {
        network: null,
        tableType: SelectedTableType.NETWORK_TABLE,
        additionalData: null
      };
      this.activeViews = [this.selectedView];
    });
  }

  loadWithLogs(selection: ActiveView) {
    this.networkRunService.getAllLogs(selection.network.id).subscribe(logs => {
      selection.additionalData = logs;
      this.activeViews.push(selection);
      this.setCurrentSelection(selection);
    });
  }

  getCurrentSelectionIndex() {
    return this.activeViews.find(n => {
      return (n.network == null || n.network.id === this.selectedView.network.id) && n.tableType === this.selectedView.tableType;
    });
  }

  isCurrentSelection(selection: ActiveView) {
    return (this.selectedView.network == null || selection.network == null || this.selectedView.network.id == selection.network.id)
      && this.selectedView.tableType === selection.tableType;
  }

  addView(newView: ActiveView) {
    const found = this.activeViews.find(n => {
      return (n.network == null || newView.network == null || n.network.id === newView.network.id) && n.tableType === newView.tableType;
    });

    if (found == null) {
      console.log('loading');
      if (newView.tableType === SelectedTableType.LOG_TABLE) {
        console.log('loading with logs');
        this.loadWithLogs(newView);
      } else {
        this.activeViews.push(newView);
        this.setCurrentSelection(newView);
      }
    } else {
      this.setCurrentSelection(found);
    }
  }


  addNetworkToSelections(network: NetworkDto, selectedTableType: SelectedTableType) {

    const sel = this.activeViews.find(n => {
      return (n.network == null || n.network.id === network.id) && n.tableType === selectedTableType;
    });
    if (sel == null) {
      const selection: ActiveView = {
        network: network,
        tableType: selectedTableType,
        additionalData: null
      };
      this.activeViews.push(selection);
      this.setCurrentSelection(selection);
    } else {
      this.setCurrentSelection(sel);
    }
  }


  removeSelection(selection: ActiveView) {

    this.activeViews = this.activeViews.filter(n => {
      return (n.network == null || n.network.id !== selection.network.id) || n.tableType !== selection.tableType;
    });
    this.selectedView = this.activeViews[0];
  }


  addNetworkToLayerWise(network: NetworkDto) {
    this.addNetworkToSelections(network, SelectedTableType.LAYER_TABLE);
  }

  setCurrentSelection(selection: ActiveView) {
    console.log(this.activeViews);
    this.selectedView = selection;
  }

  isNetworkTable(selected: ActiveView): boolean {
    return selected != null && selected.tableType === SelectedTableType.NETWORK_TABLE;
  }

  isLayerTable(selected: ActiveView): boolean {
    return selected != null && selected.tableType === SelectedTableType.LAYER_TABLE;
  }

  isLogTable(selected: ActiveView): boolean {
    return selected != null && selected.tableType === SelectedTableType.LOG_TABLE;
  }

}


