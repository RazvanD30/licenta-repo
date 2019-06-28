import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {
  faBug,
  faCalendarAlt,
  faChevronDown,
  faFileCsv,
  faHome,
  faNetworkWired,
  faPlusSquare,
  faSignInAlt,
  faWrench
} from '@fortawesome/free-solid-svg-icons';
import {faCodeBranch} from '@fortawesome/free-solid-svg-icons/faCodeBranch';
import {AuthenticationService} from '../services/authentication.service';
import {BranchService} from '../../shared/services/branch.service';
import {BranchDto} from '../../shared/models/branch/BranchDto';


export function getMarginTop() {
  const el: HTMLCollectionOf<Element> = document.getElementsByClassName('navigation-bar-inner');
  if (el.length > 0) {
    return 100;
  }
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
  public currentBranch: BranchDto;
  public username: string;
  public availableBranches: BranchDto[] = [];


  constructor(private authenticationService: AuthenticationService,
              private branchService: BranchService) {

  }


  getAnimValue(): string {
    return this.isOpen === true ? 'open' : 'closed';
  }

  getMarginTop(): string {
    const st = getComputedStyle(this.navBarInner.nativeElement);
    return '-' + st.height;
  }

  ngOnInit() {
    this.branchService.branchAdded.subscribe(() => this.loadBranchData());
    this.username = localStorage.getItem('username');
    if (this.isLoggedIn()) {
      this.loadBranchData();
    }
    this.links = [
      {text: 'HOME', multiple: false, href: '', icon: faHome},
      {
        text: 'NETWORK MANAGEMENT', multiple: true, icon: faNetworkWired, choices: [
          {text: 'NETWORK INITIALIZATION', href: '/network-management/init', icon: faPlusSquare},
          {text: 'NETWORK CONFIGURATION', href: '/network-management/config', icon: faWrench},
          {text: 'NETWORK DEBUG', href: '/network-management/debug', icon: faBug}
        ]
      },
      {text: 'BRANCH MANAGEMENT', multiple: false, icon: faCodeBranch, href: '/branch-management/dashboard'},
      {text: 'FILE MANAGEMENT', multiple: false, icon: faFileCsv, href: '/file-management/dashboard'},
      {text: 'JOB MANAGEMENT', multiple: false, icon: faCalendarAlt, href: '/job-management/dashboard'}
    ];
    if (this.isLoggedIn()) {
      this.links.push({text: 'LOGOUT', multiple: false, href: '/logout', icon: faSignInAlt});
    } else {
      this.links.push({text: 'AUTHENTICATION', multiple: false, href: '/authenticate', icon: faSignInAlt});
    }

    this.authenticationService.loggedIn.subscribe(() => {
      this.loadBranchData();
      const link = this.links.find(l => l.text === 'AUTHENTICATION');
      if (link != null) {
        link.text = 'LOGOUT';
        link.href = '/logout';
      }
    });
  }

  loadBranchData() {
    this.username = localStorage.getItem('username');
    if (this.username != null) {
      this.branchService.getAllForUser(this.username)
        .subscribe(branches => {
          this.branchService.getCurrentWorkingBranch(this.username)
            .subscribe(current => {
              if (current == null) {
                this.availableBranches = branches;
                this.currentBranch = current;
              } else {
                this.availableBranches = branches.filter(branch => {
                  return branch.name !== current.name;
                });
                this.currentBranch = current;
              }
            });
        });
    }
  }

  delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  logout() {
    this.authenticationService.logout();
    this.closeDelayed();
  }


  async closeDelayed() {
    await this.delay(1000);
    this.isOpen = false;
  }

  openClose(event) {
    event.preventDefault();
    this.isOpen = !this.isOpen;
  }

  isLoggedIn(): boolean {
    return this.authenticationService.isLoggedIn();
  }

  selectBranch(branch: BranchDto) {
    this.branchService.assignWorkingBranch(this.username, branch.name)
      .subscribe(response => {
        this.loadBranchData();
        this.branchService.signalBranchChange(branch.name);
      });
  }
}
