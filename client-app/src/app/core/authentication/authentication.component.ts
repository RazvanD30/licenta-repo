import {AfterViewInit, Component, ElementRef, Inject, OnInit, Renderer2, ViewChild} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.scss']
})
export class AuthenticationComponent implements OnInit, AfterViewInit {

  @ViewChild('buttonLogin') buttonLogin: ElementRef;
  @ViewChild('buttonRegister') buttonRegister: ElementRef;
  @ViewChild('loginContent') loginContent: ElementRef;
  @ViewChild('registerContent') registerContent: ElementRef;
  @ViewChild('authentication-container') authenticationContainer: ElementRef;

  loginWindowOpenend: boolean;
  registerWindowOpened: boolean;

  private listener;
  private returnUrl: string;

  constructor(
    @Inject(DOCUMENT) private document,
    private renderer: Renderer2,
    private route: ActivatedRoute,
    private router: Router) {
    this.renderer = renderer;
  }

  ngOnInit() {
    this.loginWindowOpenend = false;
    this.registerWindowOpened = false;
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  expandLogin() {
    this.loginWindowOpenend = true;
    this.expand(this.buttonLogin, this.loginContent, this.buttonRegister);
  }

  expandRegister() {
    this.registerWindowOpened = true;
    this.expand(this.buttonRegister, this.registerContent, this.buttonLogin);
  }


  hideLogin() {
    this.loginWindowOpenend = false;
    this.hide(this.buttonLogin, this.loginContent, this.buttonRegister);
  }

  hideRegister() {
    this.registerWindowOpened = false;
    this.hide(this.buttonRegister, this.registerContent, this.buttonLogin);
  }

  hide(selectedButton: ElementRef, selectedContent: ElementRef, hiddenButton: ElementRef) {

    const selectedButtonNE = selectedButton.nativeElement;
    const selectedContentNE = selectedContent.nativeElement;
    const hiddenButtonNE = hiddenButton.nativeElement;

    this.listener();
    this.renderer.removeClass(selectedButtonNE, 'button--active');

    this.renderer.removeStyle(selectedButtonNE, 'z-index');
    this.renderer.removeStyle(selectedButtonNE, 'transform');
    this.renderer.removeStyle(selectedContentNE, 'transition');

    this.renderer.setStyle(selectedContentNE, 'transition', 'all 0.2s 0s cubic-bezier(0.23, 1, 0.32, 1)');
    this.renderer.removeClass(selectedContentNE, 'button__content--active');

    this.renderer.removeStyle(hiddenButtonNE, 'transform');
    this.renderer.removeClass(hiddenButtonNE, 'button--hidden');
  }

  expand(selectedButton: ElementRef, selectedContent: ElementRef, hiddenButton: ElementRef) {
    if (selectedButton.nativeElement.classList.contains('button--active')) {
      return false;
    } else {
      const halfOfWindowLength = window.innerWidth / 2;
      const selectedButtonNE = selectedButton.nativeElement;
      const hiddenButtonNE = hiddenButton.nativeElement;
      const selectedContentNE = selectedContent.nativeElement;

      const selectedButtonWidth = selectedButtonNE.offsetWidth / 2;
      const selectedButtonOffset = selectedButtonNE.getBoundingClientRect();

      const hiddenButtonWidth = hiddenButtonNE.offsetWidth / 2;
      const hiddenButtonOffset = hiddenButtonNE.getBoundingClientRect();

      // content box stuff
      const selectedContentWidth = selectedContentNE.offsetWidth;
      const selectedContentHeight = selectedContentNE.offsetHeight;


      // transform values for button
      const selectedButtonTransX = halfOfWindowLength - selectedButtonOffset.left - selectedButtonWidth;
      const hiddenButtonTransX = halfOfWindowLength - hiddenButtonOffset.left - hiddenButtonWidth;
      const selectedScaleX = selectedContentWidth / selectedButtonNE.offsetWidth;
      const selectedScaleY = selectedContentHeight / selectedButtonNE.offsetHeight;

      this.renderer.addClass(hiddenButtonNE, 'button--hidden');
      this.renderer.addClass(selectedButtonNE, 'button--active');

      this.renderer.setStyle(selectedButtonNE, 'transform', 'translateX(' + selectedButtonTransX + 'px)');
      this.renderer.setStyle(hiddenButtonNE, 'transform', 'translateX(' + hiddenButtonTransX + 'px)');

      this.listener = this.renderer.listen(selectedButtonNE, 'transitionend', () => {
        this.renderer.setStyle(selectedButtonNE,
          'transform', 'translateX(' + selectedButtonTransX + 'px) scale(' + selectedScaleX + ',' + selectedScaleY + ')');
      });


      this.renderer.addClass(selectedContent.nativeElement, 'button__content--active');
      this.renderer.setStyle(selectedContent.nativeElement, 'transition', 'all 1s 0.3s cubic-bezier(0.23, 1, 0.32, 1)');
    }
  }

  redirectToLogin(event: Event) {
    this.hideRegister();
  }

  redirectToHome(event: Event) {
    this.hideLogin();
    return this.router.navigateByUrl(this.returnUrl);
  }

  loadScript(src: any) {
    const s = this.renderer.createElement('script');
    s.type = 'text/javascript';
    s.src = src;
    this.renderer.appendChild(this.document.body, s);
  }

  ngAfterViewInit(): void {
    this.loadScript('https://s3-us-west-2.amazonaws.com/s.cdpn.io/499416/TweenLite.min.js');
    this.loadScript('https://s3-us-west-2.amazonaws.com/s.cdpn.io/499416/EasePack.min.js');
    this.loadScript('../assets/demo.js');
  }

}

