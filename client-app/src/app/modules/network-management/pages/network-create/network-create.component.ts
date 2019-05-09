import {Component, OnInit, ViewChild} from '@angular/core';
import {NetworkRunService} from '../../../../core/services/network-run.service';
import {MatPaginator, MatSort} from '@angular/material';
import {NetworkInit} from '../../../../shared/models/network/NetworkInit';
import {merge} from 'rxjs';
import {map, startWith, switchMap} from 'rxjs/operators';
import {NetworkInitService} from '../../../../core/services/network-init.service';

@Component({
  selector: 'app-network-create',
  templateUrl: './network-create.component.html',
  styleUrls: ['./network-create.component.scss']
})
export class NetworkCreateComponent implements OnInit {

  displayedColumns: string[] = ['id','name','seed','learningRate','batchSize','nEpochs','nInputs','nOutputs','layers'];
  data: NetworkInit[] = [];

  isLoadingResults = true;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private networkService: NetworkRunService,
              private networkInitService: NetworkInitService) {
  }

  ngOnInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    this.networkInitService.getAll().subscribe(networkInits => {
      this.isLoadingResults = false;
      console.log(this.data);
      this.data = networkInits;
    });

  }

}


