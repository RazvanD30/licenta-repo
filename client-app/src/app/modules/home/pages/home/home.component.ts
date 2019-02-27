import {AfterViewInit, Component, Inject, OnInit, Renderer2} from '@angular/core';
import {User} from '../../../../shared/models/User';
import {UserService} from '../../../../core/services/user.service';
import {first} from 'rxjs/operators';
import {DOCUMENT} from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, AfterViewInit {
  currentUser: User;
  users: User[] = [];

  constructor(
    @Inject(DOCUMENT) private document,
    private userService: UserService,
    private renderer: Renderer2) {
    console.log('loaded home');
    this.currentUser = JSON.parse(localStorage.getItem('current-user'));
  }

  ngOnInit() {
  }

  loadScript(src: any) {
    let s = this.renderer.createElement('script');
    s.type = 'text/javascript';
    s.src = src;
    this.renderer.appendChild(this.document.body, s);
  }

  ngAfterViewInit(): void {
    //  this.loadScript('https://s3-us-west-2.amazonaws.com/s.cdpn.io/499416/TweenLite.min.js');
    //  this.loadScript('https://s3-us-west-2.amazonaws.com/s.cdpn.io/499416/EasePack.min.js');
    //  this.loadScript('https://s3-us-west-2.amazonaws.com/s.cdpn.io/499416/demo.js');
  }


}
