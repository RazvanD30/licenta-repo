import {AfterViewInit, Component, ElementRef, HostListener, Inject, OnInit, Renderer2, ViewChild} from '@angular/core';

import {faCoffee} from '@fortawesome/free-solid-svg-icons';
import {ActivatedRoute, Router} from '@angular/router';
import {DOCUMENT} from '@angular/common';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, AfterViewInit {
  title = 'app';
  faCoffee = faCoffee;

  constructor(
    @Inject(DOCUMENT) private document,
    private renderer: Renderer2,
    private route: ActivatedRoute,
    private router: Router) {
  }


  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.loadScript('https://s3-us-west-2.amazonaws.com/s.cdpn.io/499416/TweenLite.min.js');
    this.loadScript('https://s3-us-west-2.amazonaws.com/s.cdpn.io/499416/EasePack.min.js');
    this.loadScript('../assets/home-canvas.js');
  }

  loadScript(src: any) {
    const s = this.renderer.createElement('script');
    s.type = 'text/javascript';
    s.src = src;
    this.renderer.appendChild(this.document.body, s);
  }

}

