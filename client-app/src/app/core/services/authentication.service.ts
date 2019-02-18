import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {LoginForm} from '../../shared/models/LoginForm';
import {RegisterForm} from '../../shared/models/RegisterForm';
import {SessionForm} from '../../shared/models/SessionForm';
import {Observable} from 'rxjs';

@Injectable()
export class AuthenticationService {

  constructor(private http: HttpClient) {
  }

  login(loginForm: LoginForm): Observable<SessionForm> {
    return this.http.post<SessionForm>(APP_SETTINGS.URLS.AUTHENTICATION.LOGIN, loginForm)
      .pipe(map(sessionForm => {
        if (sessionForm && sessionForm.token) {
          localStorage.setItem('current-user', JSON.stringify(sessionForm));
        }
        return sessionForm;
      }));
  }

  logout(): Observable<SessionForm> {
    const currentUser = localStorage.getItem('current-user');
    return this.http.post<SessionForm>(APP_SETTINGS.URLS.AUTHENTICATION.LOGOUT, currentUser)
      .pipe(map(sessionForm => {
        if (sessionForm) {
          localStorage.removeItem('current-user');
        }
        return sessionForm;
      }));
  }

  register(registerForm: RegisterForm): Observable<SessionForm> {
    return this.http.post<SessionForm>(APP_SETTINGS.URLS.AUTHENTICATION.REGISTER, registerForm)
      .pipe(map(sessionForm => {
        if (sessionForm) {
          localStorage.removeItem('current-user');
        }
        return sessionForm;
      }));
  }
}
