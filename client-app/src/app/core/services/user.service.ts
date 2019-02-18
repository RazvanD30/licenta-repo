import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../../shared/models/User';
import {APP_SETTINGS} from '../../configs/app-settings.config';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<User[]>(APP_SETTINGS.URLS.USER_MANAGEMENT.GET_ALL);
  }

  getById(id: number) {
    return this.http.get(APP_SETTINGS.URLS.USER_MANAGEMENT.GET_BY_ID + id);
  }

  update(user: User) {
    return this.http.put(APP_SETTINGS.URLS.USER_MANAGEMENT.UPDATE + user.id, user);
  }

  delete(id: number) {
    return this.http.delete(APP_SETTINGS.URLS.USER_MANAGEMENT.DELETE + id);
  }
}
