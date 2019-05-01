import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {Network} from '../../shared/models/network/Network';

@Injectable({
  providedIn: 'root'
})
export class NetworkDebugService {

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<Network[]>(APP_SETTINGS.URLS.NETWORK_DEBUG.GET_ALL);
  }

  getById(id: number) {
    return this.http.get(APP_SETTINGS.URLS.NETWORK_DEBUG.GET_BY_ID + id);
  }

  update(network: Network) {
    return this.http.put(APP_SETTINGS.URLS.NETWORK_DEBUG.UPDATE + network.id, network);
  }

  delete(id: number) {
    return this.http.delete(APP_SETTINGS.URLS.NETWORK_DEBUG.DELETE + id);
  }
}
