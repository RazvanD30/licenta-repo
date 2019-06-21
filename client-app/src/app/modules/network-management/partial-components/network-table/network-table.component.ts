import {ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {MatMenuTrigger, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {NetworkDto} from '../../../../shared/models/network/runtime/NetworkDto';
import {faList} from '@fortawesome/free-solid-svg-icons';
import {ActiveView} from '../../models/ActiveView';
import {SelectedTableType} from '../../models/SelectedTableType';
import {NetworkConfigureService} from '../../../../shared/services/network-configure.service';


@Component({
  selector: 'app-network-table',
  templateUrl: './network-table.component.html',
  styleUrls: ['./network-table.component.scss']
})
export class NetworkTableComponent implements OnInit, OnChanges {

  @Input() networks: NetworkDto[];
  displayedColumns: string[];
  editable: boolean;
  networkDataSource: MatTableDataSource<NetworkDto>;
  filters;
  gradeErrorMatcher;
  fc;
  faList = faList;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @Output() onLayersView = new EventEmitter();
  contextMenuPosition = {x: '0px', y: '0px'};

  @ViewChild(MatMenuTrigger) contextMenu: MatMenuTrigger;

  constructor() {
  }


  ngOnChanges(changes): void {
    if (this.networkDataSource) {
      this.networkDataSource.data = this.networks;
    }
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
    this.networkDataSource.sortingDataAccessor = (network: NetworkDto, property: string) => {
      switch (property) {
        case 'nLayers':
          return network.layers.length;
        default:
          return network[property];
      }
    };
    this.networkDataSource.filterPredicate = (network: NetworkDto, filters: string) => this.filterPredicate(network);
    this.networkDataSource.sort = this.sort;

    this.resetFilters();
    this.displayedColumns = ['id', 'name', 'seed', 'learningRate', 'batchSize', 'nEpochs', 'nInputs', 'nOutputs', 'nLayers', 'actions'];
    this.editable = true;
    this.fc = [];
    this.resetFormControl();

  }

  onContextMenuActionLayerView(item: NetworkDto) {
    const newView: ActiveView = {
      uniqueNameParam: item.name + '',
      dataParam: item,
      tableType: SelectedTableType.LAYER_TABLE
    };
    this.onLayersView.emit(newView);
  }

  onContextMenuActionLogView(item: NetworkDto) {
    const newView: ActiveView = {
      uniqueNameParam: item.name + '',
      dataParam: item,
      tableType: SelectedTableType.LOG_TABLE
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
        return cell === expected;
      case 'nq':
        return cell !== expected;
    }
  }

  filterPredicate(network: NetworkDto): boolean {
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


  getRowIndexFor(network: NetworkDto) {
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
