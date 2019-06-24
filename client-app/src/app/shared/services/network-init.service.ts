import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {NetworkInitDto} from '../models/network/init/NetworkInitDto';

@Injectable({
  providedIn: 'root'
})
export class NetworkInitService {

  constructor(private http: HttpClient) {
  }

  create(networkInit: NetworkInitDto) {
    return this.http.post(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.POST_CREATE, networkInit);
  }

  getAll(): Observable<NetworkInitDto[]> {
    return this.http.get<NetworkInitDto[]>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.GET_ALL);
  }

  getById(id: number): Observable<NetworkInitDto> {
    return this.http.get<NetworkInitDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.GET_BY_ID + id);
  }

  getByName(name: string): Observable<NetworkInitDto> {
    return this.http.get<NetworkInitDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.GET_BY_NAME + name);
  }

  getAllNames(): Observable<string[]> {
    return this.http.get<string[]>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.GET_ALL_NAMES);
  }

  delete(id: number) {
    return this.http.delete(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.DELETE_BY_ID + id);
  }
}
