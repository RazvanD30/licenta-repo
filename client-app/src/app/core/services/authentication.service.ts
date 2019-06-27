import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {Observable, of} from 'rxjs';
import {map, share} from 'rxjs/operators';
import {JwtHelper} from 'angular2-jwt';
import {Router} from "@angular/router";

@Injectable()
export class AuthenticationService {

  constructor(private http: HttpClient,
              private decoder: JwtHelper,
              private router: Router) {
  }


  refreshToken(): Observable<string> {
    const url = 'url to refresh token here';

    // append refresh token if you have one
    const refreshToken = localStorage.getItem('refreshToken');
    const expiredToken = localStorage.getItem('token');

    return this.http
      .get(url, {
        headers: new HttpHeaders()
          .set('refreshToken', refreshToken)
          .set('token', expiredToken),
        observe: 'response'
      })
      .pipe(
        share(), // <========== YOU HAVE TO SHARE THIS OBSERVABLE TO AVOID MULTIPLE REQUEST BEING SENT SIMULTANEOUSLY
        map(res => {
          const token = res.headers.get('token');
          const newRefreshToken = res.headers.get('refreshToken');

          // store the new tokens
          localStorage.setItem('refreshToken', newRefreshToken);
          localStorage.setItem('token', token);

          console.log('requested refresh token' + JSON.stringify(token));
          return token;
        })
      );
  }

  getToken(): Observable<string> {
    const token = localStorage.getItem('token');
    const isTokenExpired = this.decoder.isTokenExpired(token);

    if (!isTokenExpired) {
      return of(token);
    }

    return this.refreshToken();
  }


  obtainAccessToken(loginData) {

    const params = new HttpParams({
      fromObject: {
        client_id: 'fooClientIdPassword',
        client_secret: 'secret',
        grant_type: 'password',
        username: loginData.username,
        password: loginData.password,
        scope: 'read'
      }
    });

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
        Authorization: 'Basic ' + btoa('fooClientIdPassword:secret')
      })
    };
    return this.http.post(APP_SETTINGS.URLS.AUTHENTICATION.TOKEN_REQUEST, params, httpOptions);
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('token') != null;
  }

  saveToken(token) {
    localStorage.setItem('token', token.access_token);
    localStorage.setItem('refreshToken', token.refresh_token);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('branch');
    localStorage.removeItem('username');
    this.router.navigateByUrl('/authenticate');
  }

}
