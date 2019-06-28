import {PreloadingStrategy, Route} from '@angular/router';
import {Observable, of} from 'rxjs';

export class CustomPreloader implements PreloadingStrategy {
  preload(route: Route, load: Function): Observable<any> {
    return route.data != null && route.data.preload === true ? load() : of(null);
  }
}
