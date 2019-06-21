import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {LayerDto} from '../../../../shared/models/network/runtime/LayerDto';
import {ActiveView} from '../../models/ActiveView';
import {SelectedTableType} from '../../models/SelectedTableType';
import {NetworkDto} from '../../../../shared/models/network/runtime/NetworkDto';

@Component({
  selector: 'app-layer-table',
  templateUrl: './layer-table.component.html',
  styleUrls: ['./layer-table.component.scss']
})
export class LayerTableComponent implements OnInit, OnChanges {

  @Input() network: NetworkDto;
  displayedColumns: string[];
  layerDataSource: MatTableDataSource<LayerDto>;
  filters;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @Output() nodeView = new EventEmitter();

  constructor() {
  }

  ngOnChanges(changes): void {
    if (this.layerDataSource) {
      this.layerDataSource.data = this.network.layers;
    }
  }

  ngOnInit() {
    this.filters = {
      nInputs: '',
      nNodes: '',
      nOutputs: '',
      type: '',
      activation: '',
      comparison: {
        nInputs: '',
        nNodes: '',
        nOutputs: ''
      }
    };

    this.layerDataSource = new MatTableDataSource(this.network.layers);
    this.layerDataSource.paginator = this.paginator;
    this.layerDataSource.sortingDataAccessor = (layer: LayerDto, property: string) => {
      switch (property) {
        default:
          return layer[property];
      }
    };
    this.layerDataSource.filterPredicate = (layer: LayerDto, filters: string) => this.filterPredicate(layer);
    this.layerDataSource.sort = this.sort;

    this.resetFilters();
    this.displayedColumns = ['id', 'nInputs', 'nNodes', 'nOutputs', 'type', 'activation', 'actions'];

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

  filterPredicate(layer: LayerDto): boolean {
    let ok = true;
    if (this.filters.nInputs !== '') {
      ok = ok === true && this.compare(layer.nInputs, this.filters.nInputs, this.filters.comparison.nInputs);
    }
    if (this.filters.nOutputs !== '') {
      ok = ok === true && this.compare(layer.nOutputs, this.filters.nOutputs, this.filters.comparison.nOutputs);
    }
    if (this.filters.nNodes !== '') {
      ok = ok === true && this.compare(layer.nNodes, this.filters.nNodes, this.filters.comparison.nNodes);
    }
    if (this.filters.type !== '') {
      ok = ok === true && layer.type.toString().toLowerCase().includes(this.filters.type.toLowerCase());
    }
    if (this.filters.activation !== '') {
      ok = ok === true && layer.activation != null && layer.activation.toString().toLowerCase()
        .includes(this.filters.activation.toLowerCase());
    }
    return ok;
  }

  applyFilter(filterValue: string, filterColumn: string) {

    if (filterValue === null || filterColumn === null) {
      this.layerDataSource.filter = 'a';
    } else {
      this.filters[filterColumn] = filterValue;
      this.layerDataSource.filter = 'a' + filterValue.trim().toLowerCase();
    }

    if (this.layerDataSource.paginator) {
      this.layerDataSource.paginator.firstPage();
    }
  }

  resetFilters() {
    this.filters = {
      nInputs: '',
      nNodes: '',
      nOutputs: '',
      type: '',
      activation: '',
      comparison: {
        nInputs: '',
        nNodes: '',
        nOutputs: ''
      }
    };
    this.applyFilter('', null);
  }

  save() {

  }

  onContextMenuActionNodeView(item: LayerDto) {
    const newView: ActiveView = {
      uniqueNameParam: item.id + '',
      dataParam: item,
      tableType: SelectedTableType.NODE_TABLE
    };
    this.nodeView.emit(newView);
  }


}
