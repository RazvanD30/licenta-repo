import { Component, OnInit } from '@angular/core';
import {FileService} from '../../../../shared/services/file.service';
import {DataFileDto} from '../../../../shared/models/file/DataFileDto';
import {APP_SETTINGS} from '../../../../configs/app-settings.config';

@Component({
  selector: 'app-file-dashboard',
  templateUrl: './file-dashboard.component.html',
  styleUrls: ['./file-dashboard.component.scss']
})
export class FileDashboardComponent implements OnInit {

  files: DataFileDto[];
  currentFile: DataFileDto;


  getDownloadLink(fileName: string): string {
    return APP_SETTINGS.URLS.FILE_MANAGEMENT.GET_DOWNLOAD_FILE_BY_NAME + fileName;
  }

  constructor(private fileService: FileService) { }

  ngOnInit() {
  }

  loadAll() {
    this.fileService.getAll().subscribe(resp => this.files = resp);
  }

  remove() {
  }
}
