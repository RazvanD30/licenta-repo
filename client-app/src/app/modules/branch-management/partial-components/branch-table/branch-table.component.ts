import {Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {BranchDto} from '../../../../shared/models/branch/BranchDto';
import {MatPaginator, MatSnackBar, MatSort, MatTabChangeEvent, MatTableDataSource} from '@angular/material';
import {PublicUserDto} from '../../../../shared/models/authentication/PublicUserDto';
import {BranchTypeParser} from '../../../../shared/models/branch/BranchType';
import {BranchService} from '../../../../shared/services/branch.service';

@Component({
  selector: 'app-branch-table',
  templateUrl: './branch-table.component.html',
  styleUrls: ['./branch-table.component.scss']
})
export class BranchTableComponent implements OnInit, OnChanges {

  @Input() branches: BranchDto[];
  @Input() users: PublicUserDto[];
  @Input() loggedUsername: string;
  displayedColumns: string[];
  branchDataSource: MatTableDataSource<BranchDto>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  filters;
  types: string[] = BranchTypeParser.names();
  updating: boolean;

  constructor(private branchService: BranchService,
              private snackBar: MatSnackBar) {
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
    this.displayedColumns = ['id', 'name', 'type', 'ownerName', 'created', 'updated', 'contributors', 'actions'];

  }

  resetFilters() {
    this.filters = {
      id: '',
      name: '',
      type: '',
      ownerName: '',
      contributor: ''
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
    if (this.filters.contributor !== '') {
      ok = ok === true && branch.contributors.find(c =>
        c.username.toLocaleLowerCase().includes(this.filters.contributor)) != null;
    }
    return ok;
  }

  compareUsers(u1: PublicUserDto, u2: PublicUserDto) {
    return u1.username === u2.username;
  }

  matTabSelectionChange($event: MatTabChangeEvent) {
    if ($event.index === 0) {
      this.resetFilters();
    }
  }

  getPossibleContributors(branch: BranchDto) {
    return this.users.filter(u => u.username !== branch.owner.username);
  }

  updateBranch(branch: BranchDto) {
    this.updating = true;
    this.branchService.update(branch).subscribe(() => {
      this.updating = false;
      this.branchService.branchChanged.emit();
      this.branchService.branchAdded.emit();
      this.snackBar.open('Branch updated successfully', 'Dismiss', {duration: 3000});
    }, () => {
      this.updating = false;
      this.branchService.branchChanged.emit();
    });
  }

  isContributor(branch: BranchDto) {
    return branch.contributors.find(c => c.username === this.loggedUsername) != null;
  }

  pull(branch: BranchDto, sourceBranch: BranchDto) {
    this.updating = true;
    this.branchService.pull(branch.id, sourceBranch.id).subscribe(() => {
      this.updating = false;
      this.branchService.branchChanged.emit();
      this.branchService.branchAdded.emit();
      this.snackBar.open('Branch updated successfully', 'Dismiss', {duration: 3000});
    }, () => {
      this.updating = false;
      this.branchService.branchChanged.emit();
    });
  }
}
