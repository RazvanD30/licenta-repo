import {Component, OnInit, Renderer2} from '@angular/core';
import {NetworkDto} from '../../../../shared/models/network/runtime/NetworkDto';
import {faTimes} from '@fortawesome/free-solid-svg-icons';
import {ActiveView} from '../../models/ActiveView';
import {NetworkInitService} from '../../../../shared/services/network-init.service';
import {MatDialog} from '@angular/material';
import {SelectedTableType} from '../../models/SelectedTableType';
import {NetworkConfigureService} from '../../../../shared/services/network-configure.service';
import {NetworkLogService} from '../../../../shared/services/network-log.service';
import {BranchService} from '../../../../shared/services/branch.service';

@Component({
  selector: 'app-global-table',
  templateUrl: './global-table.component.html',
  styleUrls: ['./global-table.component.scss']
})
export class GlobalTableComponent implements OnInit {


  networks: NetworkDto[];
  faTimes = faTimes;

  activeViews: ActiveView[] = [];
  doneLoading = false;
  selectedView: ActiveView;

  constructor(private networkConfigureService: NetworkConfigureService,
              private networkLogService: NetworkLogService,
              private networkInitService: NetworkInitService,
              private branchService: BranchService) {
  }

  ngOnInit() {
    this.loadNetworks();
    this.branchService.branchChanged.subscribe(change => {
      this.doneLoading = false;
      this.loadNetworks();
    });
  }

  loadNetworks() {
    this.networkConfigureService.getAllForUser(localStorage.getItem('username')).subscribe(networks => {
      this.networks = networks;
      console.log(networks);
      this.selectedView = {
        uniqueNameParam: 'None',
        dataParam: this.networks,
        tableType: SelectedTableType.NETWORK_TABLE,
      };
      this.activeViews = [this.selectedView];
      this.doneLoading = true;
    });
  }

  loadWithLogs(selection: ActiveView) {
    this.networkLogService.getAllForNetworkId(selection.dataParam.id).subscribe(logs => {
      selection.dataParam.logs = logs;
      this.activeViews.push(selection);
      this.setCurrentSelection(selection);
    });
  }


  addView(newView: ActiveView) {
    const found = this.activeViews.find(n => {
      return n.uniqueNameParam === newView.uniqueNameParam && n.tableType === newView.tableType;
    });

    if (found == null) {
      if (newView.tableType === SelectedTableType.LOG_TABLE) {
        this.loadWithLogs(newView);
      } else {
        this.activeViews.push(newView);
        this.setCurrentSelection(newView);
      }
    } else {
      this.setCurrentSelection(found);
    }
  }


  removeSelection(selection: ActiveView) {

    this.activeViews = this.activeViews.filter(n => {
      return n.uniqueNameParam !== selection.uniqueNameParam || n.tableType !== selection.tableType;
    });
    this.selectedView = this.activeViews[0];
  }

  setCurrentSelection(selection: ActiveView) {
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

  isNodeTable(selected: ActiveView) {
    return selected != null && selected.tableType === SelectedTableType.NODE_TABLE;
  }
}
