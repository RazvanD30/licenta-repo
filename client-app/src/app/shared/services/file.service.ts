import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DataFileDto} from '../models/file/DataFileDto';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {RunConfigDto} from '../models/network/traintest/RunConfigDto';
import {FileLinkDto} from '../models/file/FileLinkDto';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<DataFileDto[]> {
    return this.http.get<DataFileDto[]>(APP_SETTINGS.URLS.FILE_MANAGEMENT.GET_ALL);
  }

  getAllForNetworkId(networkId: number): Observable<RunConfigDto[]> {
    return this.http.get<RunConfigDto[]>(APP_SETTINGS.URLS.FILE_MANAGEMENT.GET_ALL_FOR_NETWORK_ID + networkId);
  }

  getByName(fileName: string): Observable<DataFileDto> {
    return this.http.get<DataFileDto>(APP_SETTINGS.URLS.FILE_MANAGEMENT.GET_BY_NAME + fileName);
  }

  getLinksByName(fileName: string): Observable<FileLinkDto[]> {
    return this.http.get<FileLinkDto[]>(encodeURI(APP_SETTINGS.URLS.FILE_MANAGEMENT.GET_LINKS_BY_NAME + fileName));
  }

  add(fileUploadForm: any): Observable<void> {
    return this.http.post<void>(APP_SETTINGS.URLS.FILE_MANAGEMENT.POST_ADD_FILE, fileUploadForm);
  }

  remove(fileName: string): Observable<void> {
    return this.http.delete<void>(APP_SETTINGS.URLS.FILE_MANAGEMENT.DELETE_FILE_BY_NAME + fileName);
  }

  download(fileName: string): Observable<any> {
    return this.http.get<any>(APP_SETTINGS.URLS.FILE_MANAGEMENT.GET_DOWNLOAD_FILE_BY_NAME + fileName);
  }

  setTrainLinks(fileName: string, networkNames: string[]): Observable<FileLinkDto[]> {
    return this.http.post<FileLinkDto[]>(APP_SETTINGS.URLS.FILE_MANAGEMENT.POST_SET_TRAIN_LINKS + fileName, networkNames);
  }

  setTestLinks(fileName: string, networkNames: string[]): Observable<FileLinkDto[]> {
    return this.http.post<FileLinkDto[]>(APP_SETTINGS.URLS.FILE_MANAGEMENT.POST_SET_TEST_LINKS + fileName, networkNames);
  }

}
