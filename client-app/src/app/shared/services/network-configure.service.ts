import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {Observable} from 'rxjs';
import {NetworkDto} from '../models/network/runtime/NetworkDto';

@Injectable({
  providedIn: 'root'
})
export class NetworkConfigureService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<NetworkDto[]> {
    return this.http.get<NetworkDto[]>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.GET_ALL);
  }

  getById(id: number): Observable<NetworkDto> {
    return this.http.get<NetworkDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.GET_BY_ID + id);
  }

  getByName(name: string): Observable<NetworkDto> {
    return this.http.get<NetworkDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.GET_BY_NAME + name);
  }

  delete(id: number): Observable<NetworkDto> {
    return this.http.delete<NetworkDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_CONFIGURE.DELETE_BY_ID + id);
  }
}
