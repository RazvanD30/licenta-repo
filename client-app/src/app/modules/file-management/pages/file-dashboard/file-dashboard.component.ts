import {Component, OnInit} from '@angular/core';
import {FileService} from '../../../../shared/services/file.service';
import {DataFileDto} from '../../../../shared/models/file/DataFileDto';
import {ExtendedDataFileDto} from '../../../../shared/models/file/ExtendedDataFileDto';

@Component({
  selector: 'app-file-dashboard',
  templateUrl: './file-dashboard.component.html',
  styleUrls: ['./file-dashboard.component.scss']
})
export class FileDashboardComponent implements OnInit {

  extendedFiles: ExtendedDataFileDto[];
  currentFile: DataFileDto;

  constructor(private fileService: FileService) {
  }

  ngOnInit() {
    this.loadAll();
  }

  loadAll() {
    this.fileService.getAll().subscribe(resp => {
      this.extendedFiles = resp.map(file => {
        return {dataFile: file, trainLinkWith: null, testLinkWith: null};
      });
    });
  }

  remove() {
  }
}
