import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {LinkDto} from '../../../../shared/models/network/runtime/LinkDto';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-link-table',
  templateUrl: './link-table.component.html',
  styleUrls: ['./link-table.component.scss']
})
export class LinkTableComponent implements OnInit {

  @Input() inputLinks: LinkDto[];
  @Input() outputLinks: LinkDto[];
  displayedColumns: string[];
  linkDataSource: MatTableDataSource<LinkDto>;
  filters;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor() {
  }

  ngOnInit() {
  }

}
