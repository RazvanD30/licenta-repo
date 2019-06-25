import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FileService} from '../../../../shared/services/file.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent implements OnInit {

  @Output() fileUploaded = new EventEmitter();
  selectedFiles: FileList;
  currentFileUpload: File;
  labels = 1;
  progress: { percentage: number } = {percentage: 0};

  constructor(private fileService: FileService) {
  }

  ngOnInit() {
  }

  selectFile(event) {
    const inputNode: any = document.querySelector('#file');

    if (typeof (FileReader) !== 'undefined') {
      const reader = new FileReader();

      reader.onload = (e: any) => {
        this.selectedFiles = event.target.files;
      };

      reader.readAsArrayBuffer(inputNode.files[0]);
    }
  }

  upload() {

    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);

    const formData = new FormData();
    formData.append('file', this.currentFileUpload);
    formData.append('nLabels', this.labels + '');
    this.selectedFiles = null;
    this.labels = 1;
    this.fileService.add(formData).subscribe(event => {
      this.fileUploaded.emit();
    });

    this.selectedFiles = undefined;
  }

}
