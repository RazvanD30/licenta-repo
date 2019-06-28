import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {AuthenticationService} from '../services/authentication.service';
import {catchError} from 'rxjs/operators';
import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material';

export interface Error {
  error: string;
  message: string;
  path: string;
  status: number;
  timestamp: string;
}

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService,
              private snackBar: MatSnackBar) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).timeout(30000).pipe(catchError(response => {
      console.log(response);
      let message = '';
      const err = response.error;
      if (response.error != null) {
        if (err.error != null && err.message != null) {
          message = err.error + ': ' + err.message + '!';
        } else if (err.errors != null) {
          err.errors.forEach(er => message += er + '. ');
        }
      }
      this.snackBar.open(message, 'Dismiss', {duration: 60000});
      return throwError(response);
    }));
  }


}
