import {Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {BranchDto} from '../../../../shared/models/branch/BranchDto';
import {MatPaginator, MatSort, MatTabChangeEvent, MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-branch-table',
  templateUrl: './branch-table.component.html',
  styleUrls: ['./branch-table.component.scss']
})
export class BranchTableComponent implements OnInit, OnChanges {

  @Input() branches: BranchDto[];

  displayedColumns: string[];
  branchDataSource: MatTableDataSource<BranchDto>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  filters;

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.branchDataSource != null) {
      this.branchDataSource.data = this.branches;
    }
  }

  ngOnInit() {
    this.branchDataSource = new MatTableDataSource(this.branches);
    this.branchDataSource.paginator = this.paginator;
    this.branchDataSource.sortingDataAccessor = (branch: BranchDto, property: string) => {
      switch (property) {
        default:
          return branch[property];
      }
    };
    this.branchDataSource.filterPredicate = (branch: BranchDto, filters: string) => this.filterPredicate(branch);
    this.branchDataSource.sort = this.sort;
    this.resetFilters();
    this.displayedColumns = ['id', 'name', 'type', 'ownerName', 'created', 'updated', 'actions'];

  }

  resetFilters() {
    this.filters = {
      id: '',
      name: '',
      type: '',
      ownerName: '',
    };
    this.applyFilter('', null);
  }

  applyFilter(filterValue: string, filterColumn: string) {

    if (filterValue === null || filterColumn === null) {
      this.branchDataSource.filter = 'a';
    } else {
      this.filters[filterColumn] = filterValue;
      this.branchDataSource.filter = 'a' + filterValue.trim().toLowerCase();
    }

    if (this.branchDataSource.paginator) {
      this.branchDataSource.paginator.firstPage();
    }
  }

  filterPredicate(branch: BranchDto): boolean {
    let ok = true;
    if (this.filters.id !== '') {
      ok = ok === true && branch.id + '' === this.filters.id;
    }
    if (this.filters.name !== '') {
      ok = ok === true && branch.name.toLocaleLowerCase().includes(this.filters.name.toLocaleLowerCase());
    }
    if (this.filters.type !== '') {
      ok = ok === true && branch.type + '' === this.filters.type;
    }
    if (this.filters.ownerName !== '') {
      ok = ok === true && branch.owner.username.toLocaleLowerCase().includes(this.filters.ownerName.toLocaleLowerCase());
    }
    return ok;
  }

  matTabSelectionChange($event: MatTabChangeEvent) {
    if ($event.index === 0) {
      this.resetFilters();
    }
  }

}
