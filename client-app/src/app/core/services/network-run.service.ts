import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NetworkDto} from '../../shared/models/network/runtime/NetworkDto';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {Observable} from 'rxjs';
import {NetworkTrainLog} from '../../shared/models/network/log/NetworkTrainLog';

@Injectable({
  providedIn: 'root'
})
export class NetworkRunService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<NetworkDto[]> {
    return this.http.get<NetworkDto[]>(APP_SETTINGS.URLS.NETWORK_RUN.GET_ALL);
  }

  getById(id: number): Observable<NetworkDto> {
    return this.http.get<NetworkDto>(APP_SETTINGS.URLS.NETWORK_RUN.GET_BY_ID + id);
  }

  update(network: NetworkDto) {
    return this.http.put(APP_SETTINGS.URLS.NETWORK_RUN.UPDATE, network);
  }

  delete(id: number) {
    return this.http.delete(APP_SETTINGS.URLS.NETWORK_RUN.DELETE_BY_ID + id);
  }

  getAllLogs(id: number): Observable<NetworkTrainLog[]> {
    return this.http.get<NetworkTrainLog[]>(APP_SETTINGS.URLS.NETWORK_LOGS.GET_ALL_SORTED_BY_NET_ID + id);
  }
}
