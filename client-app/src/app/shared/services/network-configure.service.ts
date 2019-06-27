import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {Observable} from 'rxjs';
import {NetworkDto} from '../models/network/runtime/NetworkDto';
import {FileLinkDto} from '../models/file/FileLinkDto';
import {RunConfigDto} from '../models/network/traintest/RunConfigDto';
import {NetworkEvalDto} from '../models/network/traintest/NetworkEvalDto';

@Injectable({
  providedIn: 'root'
})
export class NetworkConfigureService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<NetworkDto[]> {
    return this.http.get<NetworkDto[]>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.GET_ALL);
  }

  getAllForUser(username: string): Observable<NetworkDto[]> {
    return this.http.get<NetworkDto[]>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.GET_ALL_FOR_USER + username);
  }

  getById(id: number): Observable<NetworkDto> {
    return this.http.get<NetworkDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.GET_BY_ID + id);
  }

  getByName(name: string): Observable<NetworkDto> {
    return this.http.get<NetworkDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.GET_BY_NAME + name);
  }

  getNetworkNamesForUser(username: string): Observable<string[]> {
    return this.http.get<string[]>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.GET_NAMES_FOR_USER + username);
  }

  delete(id: number): Observable<NetworkDto> {
    return this.http.delete<NetworkDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.DELETE_BY_ID + id);
  }

  trainAndEvaluate(networkId: number, config: RunConfigDto): Observable<NetworkEvalDto> {
    return this.http.post<NetworkEvalDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_TRAIN_TEST.POST_RUN + networkId, config);
  }

  getAllLinks(networkName: string): Observable<FileLinkDto[]> {
    return this.http.get<FileLinkDto[]>
    (APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.GET_ALL_LINKS_FOR_NETWORK_NAME + networkName);
  }
}
