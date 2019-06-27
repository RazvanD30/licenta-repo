import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTabChangeEvent, MatTableDataSource} from '@angular/material';
import {NetworkTrainLogDto} from '../../../../shared/models/network/log/NetworkTrainLogDto';
import {NetworkDto} from '../../../../shared/models/network/runtime/NetworkDto';

@Component({
  selector: 'app-logs-table',
  templateUrl: './logs-table.component.html',
  styleUrls: ['./logs-table.component.scss']
})
export class LogsTableComponent implements OnInit {

  @Input() logs: NetworkTrainLogDto[];
  @Input() network: NetworkDto;
  displayedColumns: string[];
  logDataSource: MatTableDataSource<NetworkTrainLogDto>;
  filters;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor() {
  }

  static compare(cell: any, expected: any, operation: string) {
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

  ngOnInit() {
    this.filters = {
      createDateTime: '',
      accuracy: '',
      precision: '',
      recall: '',
      f1Score: '',
      iterations: '',
      comparison: {
        createDateTime: '',
        accuracy: '',
        precision: '',
        recall: '',
        f1Score: '',
        iterations: ''
      },
    };

    this.logDataSource = new MatTableDataSource(this.logs);
    this.logDataSource.paginator = this.paginator;
    this.logDataSource.sortingDataAccessor = (log: NetworkTrainLogDto, property: string) => {
      switch (property) {
        default:
          return log[property];
      }
    };
    this.logDataSource.filterPredicate = (log: NetworkTrainLogDto, filters: string) => this.filterPredicate(log);
    this.logDataSource.sort = this.sort;
    this.resetFilters();
    this.displayedColumns = ['createDateTime', 'accuracy', 'precision', 'recall', 'f1Score', 'iterations'];
  }

  filterPredicate(log: NetworkTrainLogDto): boolean {
    let ok = true;
    if (this.filters.createDateTime !== '') {
      const d1 = new Date(log.createDateTime);
      const d2 = new Date(this.filters.createDateTime);
      ok = ok === true && LogsTableComponent.compare(d1, d2, this.filters.comparison.createDateTime);
    }
    if (this.filters.accuracy !== '') {
      ok = ok === true && LogsTableComponent.compare(log.accuracy, this.filters.accuracy, this.filters.comparison.accuracy);
    }
    if (this.filters.precision !== '') {
      ok = ok === true && LogsTableComponent.compare(log.precision, this.filters.precision, this.filters.comparison.precision);
    }
    if (this.filters.recall !== '') {
      ok = ok === true && LogsTableComponent.compare(log.recall, this.filters.recall, this.filters.comparison.recall);
    }
    if (this.filters.f1Score !== '') {
      ok = ok === true && LogsTableComponent.compare(log.f1Score, this.filters.f1Score, this.filters.comparison.f1Score);
    }
    if (this.filters.iterations !== '') {
      ok = ok === true && LogsTableComponent
        .compare(log.networkIterationLogs.length, this.filters.iterations, this.filters.comparison.iterations);
    }
    return ok;
  }


  applyFilter(filterValue: string, filterColumn: string) {

    if (filterValue === null || filterColumn === null) {
      this.logDataSource.filter = 'a';
    } else {
      this.filters[filterColumn] = filterValue;
      this.logDataSource.filter = 'a' + filterValue.trim().toLowerCase();
    }

    if (this.logDataSource.paginator) {
      this.logDataSource.paginator.firstPage();
    }
  }

  resetFilters() {
    this.filters = {
      createDateTime: '',
      accuracy: '',
      precision: '',
      recall: '',
      f1Score: '',
      iterations: '',
      comparison: {
        createDateTime: '',
        accuracy: '',
        precision: '',
        recall: '',
        f1Score: '',
        iterations: ''
      },
    };
    this.applyFilter('', null);
  }

  matTabSelectionChange($event: MatTabChangeEvent) {
    if ($event.index === 0) {
      this.resetFilters();
    }
  }

}
