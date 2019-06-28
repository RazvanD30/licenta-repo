import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {NetworkInitDto} from '../models/network/init/NetworkInitDto';
import {TIMEOUT} from '../config/timeout-config';

@Injectable({
  providedIn: 'root'
})
export class NetworkInitService {

  constructor(private http: HttpClient) {
  }

  create(networkInit: NetworkInitDto) {
    return this.http.post(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.POST_CREATE, networkInit).timeout(TIMEOUT);
  }

  getAll(): Observable<NetworkInitDto[]> {
    return this.http.get<NetworkInitDto[]>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.GET_ALL).timeout(TIMEOUT);
  }

  getById(id: number): Observable<NetworkInitDto> {
    return this.http.get<NetworkInitDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.GET_BY_ID + id).timeout(TIMEOUT);
  }

  getByName(name: string): Observable<NetworkInitDto> {
    return this.http.get<NetworkInitDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.GET_BY_NAME + name).timeout(TIMEOUT);
  }

  getAllNames(): Observable<string[]> {
    return this.http.get<string[]>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.GET_ALL_NAMES).timeout(TIMEOUT);
  }

  delete(id: number) {
    return this.http.delete(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_INIT.DELETE_BY_ID + id).timeout(TIMEOUT);
  }
}
