import {Injectable} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {delay, dematerialize, materialize, mergeMap} from 'rxjs/operators';
import {APP_SETTINGS} from '../../configs/app-settings.config';

@Injectable()
export class AuthenticateMock implements HttpInterceptor {


  intercept(request: HttpRequest<any>, next: HttpHandler) {

    const users: any[] = JSON.parse(localStorage.getItem('mock-users')) || [];
    return of(null).pipe(mergeMap(() => {

      if (request.url === APP_SETTINGS.URLS.AUTHENTICATION.LOGIN && request.method === 'POST') {

        const filteredUsers = users.filter(user => {
          return user.username === request.body.username && user.password === request.body.password;
        });
        if (filteredUsers.length) {
          // if login details are valid return 200 OK with user details and fake jwt token
          const user = filteredUsers[0];
          const body = {
            id: user.id,
            username: user.username,
            firstName: user.firstName,
            lastName: user.lastName,
            token: 'fake-jwt-token'
          };
          console.log(body);

          return of(new HttpResponse({status: 200, body}));
        } else {
          // else return 400 bad request
          return throwError({error: {message: 'Username or password is incorrect'}});
        }

      }

      if (request.url === APP_SETTINGS.URLS.AUTHENTICATION.LOGOUT && request.method === 'POST') {

        const filteredUsers = users.filter(user => {
          return user.username === request.body.username;
        });

        if (filteredUsers.length) {
          // if login details are valid return 200 OK with user details and fake jwt token
          const user = filteredUsers[0];
          const body = {
            id: user.id,
            username: user.username,
            firstName: user.firstName,
            lastName: user.lastName,
            token: 'fake-jwt-token'
          };

          return of(new HttpResponse({status: 200, body}));
        } else {
          // else return 400 bad request
          return throwError({error: {message: 'Username not logged in'}});
        }

      }

      if (request.url === APP_SETTINGS.URLS.AUTHENTICATION.REGISTER && request.method === 'POST') {
        // get new user object from post body
        const newUser = request.body;

        // validation
        const duplicateUser = users.filter(user => {
          return user.username === newUser.username;
        }).length;
        if (duplicateUser) {
          return throwError({error: {message: 'Username "' + newUser.username + '" is already taken'}});
        }

        // save new user
        newUser.id = users.length + 1;
        users.push(newUser);
        const body = {
          id: newUser.id,
          username: newUser.username,
          firstName: newUser.firstName,
          lastName: newUser.lastName,
          token: 'fake-jwt-token'
        };
        localStorage.setItem('mock-users', JSON.stringify(users));

        // respond 200 OK
        return of(new HttpResponse({status: 200, body}));
      }

      if (request.url === APP_SETTINGS.URLS.USER_MANAGEMENT.GET_ALL && request.method === 'GET') {
        return of(new HttpResponse({status: 200, body: users}));
      }

      if (request.url.startsWith(APP_SETTINGS.URLS.USER_MANAGEMENT.DELETE) && request.method === 'DELETE') {

        const id = parseInt(request.url[request.url.length - 1],10);
        console.log(id);
        let deletedUser;
        const filteredUsers = users.filter(currentUser => {
          if (currentUser.id === id) {
            deletedUser = currentUser;
            return false;
          }
          return true;
        });
        console.log(filteredUsers);
        localStorage.setItem('mock-users', JSON.stringify(filteredUsers));

        return of(new HttpResponse({status: 200, body: deletedUser}));
      }
    })).pipe(materialize())
      .pipe(delay(500))
      .pipe(dematerialize());
  }

  f(): any {

    if (1 === 1) {
      return [{a: 'b'}];
    }


    return true;
  }

}


export let authenticateMockProvider = {
  // use fake backend in place of Http service for backend-less development
  provide: HTTP_INTERCEPTORS,
  useClass: AuthenticateMock,
  multi: true
};
