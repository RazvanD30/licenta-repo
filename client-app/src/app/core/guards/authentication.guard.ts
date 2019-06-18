import {Injectable} from '@angular/core';
import {
  CanActivate,
  CanActivateChild,
  CanLoad,
  Route,
  UrlSegment,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
  Router
} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthenticationService} from '../services/authentication.service';
import {share} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate, CanLoad {

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return this.isLoggedIn(state.url);
  }

  canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
    let path = '';
    segments.forEach(segment => path += '/' + segments[0].path);

    if (path === '') {
      path = '/';
    }

    return this.isLoggedIn(path);
  }


  isLoggedIn(returnUrl: string): boolean {
    if (localStorage.getItem('token')) {
      return true;
    }
    this.router.navigate(['/authenticate'], {queryParams: {returnUrl}});
    return false;
  }
}
