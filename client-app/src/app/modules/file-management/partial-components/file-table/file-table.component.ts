import {ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {APP_SETTINGS} from '../../../../configs/app-settings.config';
import {faDownload, faLink} from '@fortawesome/free-solid-svg-icons';
import {NetworkConfigureService} from '../../../../shared/services/network-configure.service';
import {FileService} from '../../../../shared/services/file.service';
import {ExtendedDataFileDto} from '../../../../shared/models/file/ExtendedDataFileDto';
import {FileType} from '../../../../shared/models/network/shared/FileType';

@Component({
  selector: 'app-file-table',
  templateUrl: './file-table.component.html',
  styleUrls: ['./file-table.component.scss']
})
export class FileTableComponent implements OnInit, OnChanges {

  public faDownload = faDownload;
  public faLink = faLink;
  @Input() extendedFiles: ExtendedDataFileDto[];

  displayedColumns: string[];
  fileDataSource: MatTableDataSource<ExtendedDataFileDto>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  filters;
  networkNames: string[];
  sendingData = false;
  dataSent = new EventEmitter();

  constructor(private networkService: NetworkConfigureService,
              private fileService: FileService,
              private cd: ChangeDetectorRef) {
  }

  ngOnChanges(changes): void {
    if (this.fileDataSource) {
      this.fileDataSource.data = this.extendedFiles;
      this.loadLinks();
      this.resetFilters();
    }
  }


  loadLinks() {
    if (this.extendedFiles != null) {
      this.extendedFiles.forEach(extendedFile => {
        this.fileService.getLinksByName(extendedFile.dataFile.name)
          .subscribe(linkDtos => {
            extendedFile.testLinkWith = [];
            extendedFile.trainLinkWith = [];
            linkDtos.forEach(linkDto => {
              if (linkDto.fileType === FileType.TEST) {
                extendedFile.testLinkWith.push(linkDto.networkName);
              } else if (linkDto.fileType === FileType.TRAIN) {
                extendedFile.trainLinkWith.push(linkDto.networkName);
              }
            });
          });
      });
    }
  }

  ngOnInit() {
    this.networkService.getNetworkNamesForUser(localStorage.getItem('username'))
      .subscribe(names => {
        this.networkNames = names;
        this.loadLinks();
      });

    this.fileDataSource = new MatTableDataSource(this.extendedFiles);
    this.fileDataSource.paginator = this.paginator;
    this.fileDataSource.sortingDataAccessor = (file: ExtendedDataFileDto, property: string) => {
      switch (property) {
        default:
          return file[property];
      }
    };
    this.fileDataSource.filterPredicate = (file: ExtendedDataFileDto, filters: string) => this.filterPredicate(file);
    this.fileDataSource.sort = this.sort;
    this.resetFilters();
    this.displayedColumns = ['id', 'name', 'nLabels', 'networkTrain', 'networkTest', 'actions'];

  }

  resetFilters() {
    this.filters = {
      id: '',
      name: '',
      nLabels: ''
    };
    this.applyFilter('', null);
  }

  applyFilter(filterValue: string, filterColumn: string) {

    if (filterValue === null || filterColumn === null) {
      this.fileDataSource.filter = 'a';
    } else {
      this.filters[filterColumn] = filterValue;
      this.fileDataSource.filter = 'a' + filterValue.trim().toLowerCase();
    }

    if (this.fileDataSource.paginator) {
      this.fileDataSource.paginator.firstPage();
    }
  }

  filterPredicate(extendedFile: ExtendedDataFileDto): boolean {
    let ok = true;
    if (this.filters.id !== '') {
      ok = ok === true && extendedFile.dataFile.id + '' === this.filters.id;
    }
    if (this.filters.name !== '') {
      ok = ok === true && extendedFile.dataFile.name.toLocaleLowerCase().includes(this.filters.name.toLocaleLowerCase());
    }
    if (this.filters.nLabels !== '') {
      ok = ok === true && extendedFile.dataFile.nLabels + '' === this.filters.nLabels;
    }
    return ok;
  }

  getDownloadLink(fileName: string): string {
    return APP_SETTINGS.URLS.FILE_MANAGEMENT.GET_DOWNLOAD_FILE_BY_NAME + fileName;
  }

  setTrainLinks(extendedFile: ExtendedDataFileDto, networkNames: string[]) {
    if (this.sendingData === false) {
      this.sendingData = true;
      this.cd.markForCheck();
      this.fileService.setTrainLinks(extendedFile.dataFile.name, networkNames)
        .subscribe(linkDtos => {
          extendedFile.trainLinkWith = [];
          linkDtos.forEach(linkDto => {
            extendedFile.trainLinkWith.push(linkDto.networkName);
          });
          this.sendingData = false;
        });
    }
  }

  setTestLinks(extendedFile: ExtendedDataFileDto, networkNames: string[]) {

    if (this.sendingData === false) {
      this.sendingData = true;
      this.cd.markForCheck();
      const loadSubscriber = this.fileService.setTestLinks(extendedFile.dataFile.name, networkNames)
        .subscribe(linkDtos => {
          loadSubscriber.unsubscribe();
          extendedFile.testLinkWith = [];
          linkDtos.forEach(linkDto => {
            extendedFile.testLinkWith.push(linkDto.networkName);
          });
          this.sendingData = false;
        });
    }
  }
}
