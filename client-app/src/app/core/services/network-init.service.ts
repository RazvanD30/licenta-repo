import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {NetworkInit} from '../../shared/models/network/NetworkInit';

@Injectable({
  providedIn: 'root'
})
export class NetworkInitService {

  constructor(private http: HttpClient) { }

  create(networkInit: NetworkInit) {
    return this.http.post(APP_SETTINGS.URLS.NETWORK_INIT.CREATE,networkInit);
  }

  getAll(): Observable<NetworkInit[]> {
    return this.http.get<NetworkInit[]>(APP_SETTINGS.URLS.NETWORK_INIT.GET_ALL);
  }

  getById(id: number): Observable<NetworkInit> {
    return this.http.get<NetworkInit>(APP_SETTINGS.URLS.NETWORK_INIT.GET_BY_ID + id);
  }

  delete(id: number) {
    return this.http.delete(APP_SETTINGS.URLS.NETWORK_INIT.DELETE_BY_ID + id);
  }
}
