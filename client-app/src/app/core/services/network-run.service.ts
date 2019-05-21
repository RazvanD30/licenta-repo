import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Network} from '../../shared/models/network/Network';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {Observable} from 'rxjs';
import {NetworkTrainLog} from '../../shared/models/network/NetworkTrainLog';

@Injectable({
  providedIn: 'root'
})
export class NetworkRunService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Network[]> {
    return this.http.get<Network[]>(APP_SETTINGS.URLS.NETWORK_RUN.GET_ALL);
  }

  getById(id: number): Observable<Network> {
    return this.http.get<Network>(APP_SETTINGS.URLS.NETWORK_RUN.GET_BY_ID + id);
  }

  update(network: Network) {
    return this.http.put(APP_SETTINGS.URLS.NETWORK_RUN.UPDATE, network);
  }

  delete(id: number) {
    return this.http.delete(APP_SETTINGS.URLS.NETWORK_RUN.DELETE_BY_ID + id);
  }

  getAllLogs(id: number): Observable<NetworkTrainLog[]> {
    return this.http.get<NetworkTrainLog[]>(APP_SETTINGS.URLS.NETWORK_LOGS.GET_ALL_SORTED_BY_NET_ID + id);
  }
}
