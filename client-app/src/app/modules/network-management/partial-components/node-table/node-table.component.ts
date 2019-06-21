import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {NodeDto} from '../../../../shared/models/network/runtime/NodeDto';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {NetworkDto} from '../../../../shared/models/network/runtime/NetworkDto';
import {ActiveView} from '../../models/ActiveView';
import {SelectedTableType} from '../../models/SelectedTableType';
import {LayerDto} from "../../../../shared/models/network/runtime/LayerDto";

@Component({
  selector: 'app-node-table',
  templateUrl: './node-table.component.html',
  styleUrls: ['./node-table.component.scss']
})
export class NodeTableComponent implements OnInit, OnChanges {

  @Input() layer: LayerDto;
  displayedColumns: string[];
  nodeDataSource: MatTableDataSource<NodeDto>;
  filters;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @Output() linkView = new EventEmitter();

  constructor() {
  }

  ngOnChanges(changes): void {
    if (this.nodeDataSource) {
      this.nodeDataSource.data = this.layer.nodes;
    }
  }

  ngOnInit() {
    this.nodeDataSource = new MatTableDataSource(this.layer.nodes);
    this.nodeDataSource.paginator = this.paginator;
    this.nodeDataSource.sortingDataAccessor = (node: NodeDto, property: string) => {
      switch (property) {
        default:
          return node[property];
      }
    };
    this.nodeDataSource.filterPredicate = (node: NodeDto, filters: string) => this.filterPredicate(node);
    this.nodeDataSource.sort = this.sort;
    this.resetFilters();
    this.displayedColumns = ['id', 'bias', 'nOutputLinks', 'actions'];
  }

  filterPredicate(node: NodeDto): boolean {
    let ok = true;
    if (this.filters.bias !== '') {
      ok = ok === true && this.compare(node.bias, this.filters.bias, this.filters.comparison.bias);
    }
    if (this.filters.nOutputLinks !== '') {
      ok = ok === true && this.compare(node.links.length, this.filters.nOutputLinks, this.filters.comparison.nOutputLinks);
    }
    return ok;
  }

  resetFilters() {
    this.filters = {
      bias: '',
      nOutputLinks: '',
      comparison: {
        bias: '',
        nOutputLinks: ''
      }
    };
    this.applyFilter('', null);
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

  applyFilter(filterValue: string, filterColumn: string) {

    if (filterValue === null || filterColumn === null) {
      this.nodeDataSource.filter = 'a';
    } else {
      this.filters[filterColumn] = filterValue;
      this.nodeDataSource.filter = 'a' + filterValue.trim().toLowerCase();
    }

    if (this.nodeDataSource.paginator) {
      this.nodeDataSource.paginator.firstPage();
    }
  }

  save() {

  }

  onContextMenuActionLinkView(item: NodeDto) {
    const newView: ActiveView = {
      uniqueNameParam: item.id + '',
      dataParam: item,
      tableType: SelectedTableType.LINK_TABLE
    };
    this.linkView.emit(newView);
  }
}
