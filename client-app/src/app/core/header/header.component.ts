import {Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {
  faBug,
  faCalendarAlt,
  faChevronDown,
  faHome,
  faNetworkWired,
  faPlusSquare,
  faSignInAlt, faWrench
} from '@fortawesome/free-solid-svg-icons';
import {faCodeBranch} from '@fortawesome/free-solid-svg-icons/faCodeBranch';
import {AuthenticationService} from '../services/authentication.service';


export function getMarginTop() {
  const el: HTMLCollectionOf<Element> = document.getElementsByClassName('navigation-bar-inner');
  if (el.length > 0) {
    console.log('yes');
    return 100;
  }
  console.log('nope');
  return 63;
}

@Component({
  selector: 'app-header',
  animations: [
    trigger('openCloseNavbar', [
      // ...
      state('open', style({
        'margin-top': '0px'
      })),
      state('closed', style({
        'margin-top': '{{margin_top}}'
      }), {params: {margin_top: '-63px'}}),
      transition('open => closed', [
        animate('0.5s')
      ]),
      transition('closed => open', [
        animate('0.5s')
      ]),
    ]),
    trigger('openCloseHeader', [
      state('open', style({
        'box-shadow': '0 12px 6px -6px rgba(0,0,0, .2)'
      })),
      state('closed', style({
        'box-shadow': 'none'
      })),
      transition('open => closed', [
        animate('0.5s')
      ]),
      transition('closed => open', [
        animate('0.5s')
      ]),
    ])
  ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  faChevronDown = faChevronDown;
  @ViewChild('toggle') toggler: ElementRef;
  @ViewChild('navBarInner') navBarInner: ElementRef;

  public isOpen = false;
  public links = [];
  public marginTop = 100;


  constructor(private authenticationService: AuthenticationService) {

  }


  getAnimValue(): string {
    return this.isOpen === true ? 'open' : 'closed';
  }

  getMarginTop(): string {
    const st = getComputedStyle(this.navBarInner.nativeElement);
    return '-' + st.height;
  }

  ngOnInit() {
    this.links = [
      {text: 'HOME', multiple: false, href: '', icon: faHome},
      {
        text: 'NETWORK MANAGEMENT', multiple: true, icon: faNetworkWired, choices: [
          {text: 'CREATE NETWORK', href: '/network/create', icon: faPlusSquare},
          {text: 'NETWORK CONFIGURATION', href: '/network/config', icon: faWrench},
          {text: 'NETWORK DEBUG', href: '/network/debug', icon: faBug}
        ]
      },
      {
        text: 'BRANCH MANAGEMENT', multiple: true, icon: faCodeBranch, choices: [
          {text: 'BRANCH DASHBOARD', href: '/branch-management/dashboard'},
          {text: 'CREATE BRANCH', href: '', icon: faPlusSquare},
          {text: 'UPDATE BRANCH', href: ''}
        ]
      },
      {text: 'JOB MANAGEMENT', multiple: false, icon: faCalendarAlt, href: ''}
    ];
    if (this.authenticationService.isLoggedIn()) {
      this.links.push({text: 'LOGOUT', multiple: false, href: '/logout', icon: faSignInAlt});
    } else {
      this.links.push({text: 'AUTHENTICATION', multiple: false, href: '/authenticate', icon: faSignInAlt});
    }

  }

  logout() {
    this.authenticationService.logout();
  }

  openClose(event) {
    event.preventDefault();
    this.isOpen = !this.isOpen;
  }
}
