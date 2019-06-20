import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {HttpClient} from '@angular/common/http';
import {NetworkTrainLogDto} from '../models/network/log/NetworkTrainLogDto';

@Injectable({
  providedIn: 'root'
})
export class NetworkLogService {

  constructor(private http: HttpClient) { }

  getAllForNetworkId(networkId: number): Observable<NetworkTrainLogDto> {
    return this.http.get<NetworkTrainLogDto>(APP_SETTINGS.URLS.NETWORK_MANAGEMENT.NETWORK_LOG.GET_ALL_FOR_NETWORK_ID + networkId);
  }
}
