import {Injectable, Injector} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {AuthenticationService} from '../services/authentication.service';
import {catchError, switchMap, timeout} from 'rxjs/operators';
import {Router} from '@angular/router';
import "rxjs-compat/add/operator/timeout";

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

  inflightAuthRequest = null;

  blacklist: object = [
    /(((http?):\/\/|www\.).*\/authentication\/.*)/,
  ];

  constructor(private injector: Injector, private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {


    if (this.isBlacklisted(request.url)) {
      return next.handle(request);
    }
    const authService = this.injector.get(AuthenticationService);

    if (!this.inflightAuthRequest) {
      this.inflightAuthRequest = authService.getToken();
    }

    return this.inflightAuthRequest.pipe(
      timeout(30000),
      switchMap((newToken: string) => {
        // unset request inflight
        this.inflightAuthRequest = null;

        // use the newly returned token
        const authReq = request.clone({
          headers: request.headers.set('token', newToken ? newToken : '')
            .set('Authorization', 'Basic ' + btoa('fooClientIdPassword:secret'))
        });

        return next.handle(authReq).timeout(30000);
      }),
      catchError(error => {
        // checks if a url is to an admin api or not
        if (error.status === 401) {
          // check if the response is from the token refresh end point
          const isFromRefreshTokenEndpoint = !!error.headers.get(
            'unableToRefreshToken'
          );

          if (isFromRefreshTokenEndpoint) {
            localStorage.clear();
            this.router.navigate(['/authentication/login']);
            return throwError(error);
          }

          if (!this.inflightAuthRequest) {
            this.inflightAuthRequest = authService.refreshToken();

            if (!this.inflightAuthRequest) {
              // remove existing tokens
              localStorage.clear();
              this.router.navigate(['/authentication/login']);
              return throwError(error);
            }
          }

          return this.inflightAuthRequest.pipe(
            switchMap((newToken: string) => {
              // unset inflight request
              this.inflightAuthRequest = null;

              // clone the original request
              const authReqRepeat = request.clone({
                headers: request.headers.set('token', newToken)
                  .set('Authorization', 'Basic ' + btoa('fooClientIdPassword:secret'))
              });

              // resend the request
              return next.handle(authReqRepeat).timeout(30000);
            })
          );
        } else {
          return throwError(error);
        }
      })
    );
  }

  isBlacklisted($url: string): boolean {
    let returnValue = false;

    for (const i of Object.keys(this.blacklist)) {
      if (this.blacklist[i].exec($url) !== null) {
        returnValue = true;
        break;
      }
    }

    return returnValue;
  }
}
