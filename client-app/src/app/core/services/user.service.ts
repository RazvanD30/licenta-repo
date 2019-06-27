import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {PublicUserDto} from '../../shared/models/authentication/PublicUserDto';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<PublicUserDto[]> {
    return this.http.get<PublicUserDto[]>(APP_SETTINGS.URLS.USER_MANAGEMENT.GET_ALL);
  }

}
