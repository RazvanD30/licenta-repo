import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {APP_SETTINGS} from '../../configs/app-settings.config';

@Injectable()
export class AuthenticationService {

  constructor(private http: HttpClient) {
  }


  obtainAccessToken(loginData) {

    const params = new HttpParams({
      fromObject: {
        grant_type: 'password',
        username: loginData.username,
        password: loginData.password,
      }
    });

    const httpOptions = {
      headers: new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': 'Basic ' + btoa('testjwtclientid' + ':' + 'XY7kmzoNzl100')
    })};

    console.log(APP_SETTINGS.URLS.AUTHENTICATION.GET_TOKEN);
    return this.http.post(APP_SETTINGS.URLS.AUTHENTICATION.GET_TOKEN, params,httpOptions);
  }

  saveToken(token) {
    localStorage.setItem('access_token', token.access_token);
  }

  checkCredentials() {
    return localStorage.getItem('access_token');
  }

  logout() {
    localStorage.removeItem('access_token');
  }

}
