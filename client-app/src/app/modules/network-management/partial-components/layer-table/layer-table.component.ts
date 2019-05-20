import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {MyErrorStateMatcher} from '../../../user-management/partial-components/users-table/users-table.component';
import {Layer} from '../../../../shared/models/network/Layer';

@Component({
  selector: 'app-layer-table',
  templateUrl: './layer-table.component.html',
  styleUrls: ['./layer-table.component.scss']
})
export class LayerTableComponent implements OnInit {

  @Input() layers: Layer[];
  displayedColumns: string[];
  editable: boolean;
  layerDataSource: MatTableDataSource<Layer>;
  filters;
  gradeErrorMatcher;
  fc;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor() {
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

    this.layerDataSource = new MatTableDataSource(this.layers);
    this.layerDataSource.paginator = this.paginator;
    this.layerDataSource.sortingDataAccessor = (layer: Layer, property: string) => {
      switch (property) {
        default:
          return layer[property];
      }
    };
    this.layerDataSource.filterPredicate = (layer: Layer, filters: string) => this.filterPredicate(layer);
    this.layerDataSource.sort = this.sort;

    this.resetFilters();
    this.displayedColumns = ['id', 'nInputs', 'nNodes', 'nOutputs', 'type', 'activation'];
    this.editable = true;
    this.gradeErrorMatcher = new MyErrorStateMatcher();
    this.fc = [];
    this.resetFormControl();

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

  filterPredicate(layer: Layer): boolean {
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
    if(this.filters.type !== '') {
      ok = ok === true && layer.type == this.filters.type;
    }
    if(this.filters.activation !== ''){
      ok = ok === true && layer.activation == this.filters.activation;
    }
    return ok;
  }


  getRowIndexFor(layer: Layer) {
    return this.layers.findIndex(n => n.id === layer.id);
  }

  resetFormControl() {
    // this.fc = this.gradeFormControls;
    // this.gradeFormControls.forEach(f => this.fc.push(new FormControl(f.value)));
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

  getAveragePresence(): number {
    return 5;
  }

  getAverageGrade(): number {
    return 10;

  }

  save() {

  }


}
