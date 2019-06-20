import {Component, OnInit} from '@angular/core';
import {FileService} from '../../../../shared/services/file.service';
import {HttpEventType, HttpResponse} from '@angular/common/http';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent implements OnInit {

  selectedFiles: FileList;
  currentFileUpload: File;
  labels: number;
  progress: { percentage: number } = {percentage: 0};

  constructor(private fileService: FileService) {
  }

  ngOnInit() {
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {

    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);

    const formData = new FormData();
    formData.append('file', this.currentFileUpload);
    formData.append('nLabels', this.labels + '');

    this.fileService.add(formData).subscribe(event => {

    });

    this.selectedFiles = undefined;
  }

}
