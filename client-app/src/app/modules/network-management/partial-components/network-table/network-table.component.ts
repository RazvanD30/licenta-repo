import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatMenuTrigger, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {MyErrorStateMatcher} from '../../../user-management/partial-components/users-table/users-table.component';
import {Network} from '../../../../shared/models/network/Network';
import {NetworkRunService} from '../../../../core/services/network-run.service';
import {faList} from '@fortawesome/free-solid-svg-icons';
import {ActiveView} from '../../models/ActiveView';
import {SelectedTableType} from '../../models/SelectedTableType';


@Component({
  selector: 'app-network-table',
  templateUrl: './network-table.component.html',
  styleUrls: ['./network-table.component.scss']
})
export class NetworkTableComponent implements OnInit {

  @Input() networks: Network[];
  displayedColumns: string[];
  editable: boolean;
  networkDataSource: MatTableDataSource<Network>;
  filters;
  gradeErrorMatcher;
  fc;
  faList = faList;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @Output() onLayersView = new EventEmitter();
  contextMenuPosition = {x: '0px', y: '0px'};

  @ViewChild(MatMenuTrigger) contextMenu: MatMenuTrigger;

  constructor(private networkRunService: NetworkRunService) {
  }

  ngOnInit() {
    this.filters = {
      name: '',
      seed: '',
      learningRate: '',
      batchSize: '',
      nEpochs: '',
      nInputs: '',
      nOutputs: '',
      nLayers: '',
      comparison: {
        seed: '',
        learningRate: '',
        batchSize: '',
        nEpochs: '',
        nInputs: '',
        nOutputs: '',
        nLayers: ''
      }
    };

    this.networkDataSource = new MatTableDataSource(this.networks);
    this.networkDataSource.paginator = this.paginator;
    this.networkDataSource.sortingDataAccessor = (network: Network, property: string) => {
      switch (property) {
        case 'nLayers':
          return network.layers.length;
        default:
          return network[property];
      }
    };
    this.networkDataSource.filterPredicate = (network: Network, filters: string) => this.filterPredicate(network);
    this.networkDataSource.sort = this.sort;

    this.resetFilters();
    this.displayedColumns = ['id', 'name', 'seed', 'learningRate', 'batchSize', 'nEpochs', 'nInputs', 'nOutputs', 'nLayers', 'actions'];
    this.editable = true;
    this.gradeErrorMatcher = new MyErrorStateMatcher();
    this.fc = [];
    this.resetFormControl();

  }

  onContextMenuActionLayerView(item: Network) {
    const newView: ActiveView = {
      network: item,
      tableType: SelectedTableType.LAYER_TABLE,
      additionalData: null
    };
    this.onLayersView.emit(newView);
  }

  onContextMenuActionLogView(item: Network) {
    const newView: ActiveView = {
      network: item,
      tableType: SelectedTableType.LOG_TABLE,
      additionalData: null
    };
    this.onLayersView.emit(newView);
  }

  compare(cell: number, expected: number, operation: string) {
    switch (operation) {
      case 'lt':
        return cell < expected;
      case 'gt':
        return cell > expected;
      case 'eq':
        return cell == expected;
      case 'nq':
        return cell != expected;
    }
  }

  filterPredicate(network: Network): boolean {
    let ok = true;
    if (this.filters.name !== '') {
      ok = ok === true && network.name.toLowerCase().includes(this.filters.name.toLowerCase());
    }
    if (this.filters.seed !== '') {
      ok = ok === true && this.compare(network.seed, this.filters.seed, this.filters.comparison.seed);
    }
    if (this.filters.learningRate !== '') {
      ok = ok === true && this.compare(network.learningRate, this.filters.learningRate, this.filters.comparison.learningRate);
    }
    if (this.filters.batchSize !== '') {
      ok = ok === true && this.compare(network.batchSize, this.filters.batchSize, this.filters.comparison.batchSize);
    }
    if (this.filters.nEpochs !== '') {
      ok = ok === true && this.compare(network.nEpochs, this.filters.nEpochs, this.filters.comparison.nEpochs);
    }
    if (this.filters.nInputs !== '') {
      ok = ok === true && this.compare(network.nInputs, this.filters.nInputs, this.filters.comparison.nInputs);
    }
    if (this.filters.nOutputs !== '') {
      ok = ok === true && this.compare(network.nOutputs, this.filters.nOutputs, this.filters.comparison.nOutputs);
    }
    if (this.filters.nLayers !== '') {
      ok = ok === true && this.compare(network.layers.length, this.filters.nLayers, this.filters.comparison.nLayers);
    }
    return ok;
  }


  getRowIndexFor(network: Network) {
    return this.networks.findIndex(n => n.id === network.id);
  }

  resetFormControl() {
    // this.fc = this.gradeFormControls;
    // this.gradeFormControls.forEach(f => this.fc.push(new FormControl(f.value)));
  }

  applyFilter(filterValue: string, filterColumn: string) {

    if (filterValue === null || filterColumn === null) {
      this.networkDataSource.filter = 'a';
    } else {
      this.filters[filterColumn] = filterValue;
      this.networkDataSource.filter = 'a' + filterValue.trim().toLowerCase();
    }

    if (this.networkDataSource.paginator) {
      this.networkDataSource.paginator.firstPage();
    }
  }

  resetFilters() {
    this.filters = {
      name: '',
      seed: '',
      learningRate: '',
      batchSize: '',
      nEpochs: '',
      nInputs: '',
      nOutputs: '',
      nLayers: '',
      comparison: {
        seed: '',
        learningRate: '',
        batchSize: '',
        nEpochs: '',
        nInputs: '',
        nOutputs: '',
        nLayers: ''
      }
    };
    this.applyFilter('', null);
  }

  getAveragePresence(): number {
    return 5;
  }

  getAverageGrade(): number {
    return 10;

  }

  save() {

  }


}
