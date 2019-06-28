import {Component} from '@angular/core';
import {
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  RouteConfigLoadEnd,
  RouteConfigLoadStart,
  Router,
  RouterEvent
} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  public isShowingRouteLoadIndicator: boolean;

  constructor(router: Router) {

    this.isShowingRouteLoadIndicator = false;
    let asyncLoadCount = 0;
    let navigationCount = 0;
    router.events.subscribe(
      (event: RouterEvent): void => {

        if (event instanceof RouteConfigLoadStart) {

          asyncLoadCount++;

        } else if (event instanceof RouteConfigLoadEnd) {

          asyncLoadCount--;

        } else if (event instanceof NavigationStart) {

          navigationCount++;

        } else if (
          (event instanceof NavigationEnd) ||
          (event instanceof NavigationError) ||
          (event instanceof NavigationCancel)
        ) {

          navigationCount--;

        }
        this.isShowingRouteLoadIndicator = !!(navigationCount && asyncLoadCount);

      }
    );
  }
}
