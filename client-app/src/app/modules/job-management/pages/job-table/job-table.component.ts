import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {NetworkDto} from '../../../../shared/models/network/runtime/NetworkDto';
import {MatMenuTrigger, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {faList} from '@fortawesome/free-solid-svg-icons';
import {NetworkConfigureService} from '../../../../shared/services/network-configure.service';
import {NetworkDtoWithTestTrain} from '../../../../shared/models/network/runtime/NetworkDtoWithTestTrain';
import {FileType} from '../../../../shared/models/network/shared/FileType';
import {RunConfigDto} from "../../../../shared/models/network/traintest/RunConfigDto";

@Component({
  selector: 'app-job-table',
  templateUrl: './job-table.component.html',
  styleUrls: ['./job-table.component.scss']
})
export class JobTableComponent implements OnInit {

  networks: NetworkDtoWithTestTrain[];
  displayedColumns: string[];
  networkDataSource: MatTableDataSource<NetworkDtoWithTestTrain>;
  filters;
  faList = faList;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  @ViewChild(MatMenuTrigger) contextMenu: MatMenuTrigger;

  constructor(private networkService: NetworkConfigureService,
              private changeDetectorRefs: ChangeDetectorRef) {
  }


  ngOnInit() {
    this.networks = [];
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
    this.displayedColumns = ['id', 'name', 'seed', 'learningRate', 'batchSize', 'nEpochs', 'nInputs', 'nOutputs', 'nLayers', 'trainFile', 'testFile', 'actions'];
    this.networkService.getAllForUser(localStorage.getItem('username')).subscribe(networks => {
      networks.forEach(network => {
        this.networkService.getAllLinks(network.name).subscribe(fileLinkDtos => {
          const trainFileNames: string[] = [];
          const testFileNames: string[] = [];
          fileLinkDtos.forEach(fileLinkDto => {
            if (fileLinkDto.fileType === FileType.TRAIN) {
              trainFileNames.push(fileLinkDto.fileName);
            } else {
              testFileNames.push(fileLinkDto.fileName);
            }
          });
          this.networks.push({
            id: network.id,
            name: network.name,
            seed: network.seed,
            learningRate: network.learningRate,
            batchSize: network.batchSize,
            nEpochs: network.nEpochs,
            nInputs: network.nInputs,
            nOutputs: network.nOutputs,
            layers: network.layers,
            trainFileName: null,
            testFileName: null,
            trainFileNames,
            testFileNames
          });
          this.networkDataSource.connect().next(this.networks);
          this.changeDetectorRefs.detectChanges();
          console.log(this.networks);
        });
      });
    });
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

  trainAndTest(network: NetworkDtoWithTestTrain) {
    const runConfig: RunConfigDto = {
      networkId: network.id,
      trainFileId: null,
      trainFileName: network.trainFileName,
      testFileId: null,
      testFileName: network.testFileName
    };
    this.networkService.trainAndEvaluate(network.id, runConfig).subscribe(resp => {
      console.log(resp);
    });
  }
}
