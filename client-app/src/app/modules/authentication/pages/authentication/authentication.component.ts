import {Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';

@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.scss']
})
export class AuthenticationComponent implements OnInit {


  @ViewChild('loginButton', {read: ElementRef}) loginButton: ElementRef;
  @ViewChild('registerButton', {read: ElementRef}) registerButton: ElementRef;
  @ViewChild('authenticationContainer', {read: ElementRef}) authenticationContainer: ElementRef;
  @ViewChild('loginContent', {read: ElementRef}) loginContent: ElementRef;
  @ViewChild('registerContent', {read: ElementRef}) registerContent: ElementRef;


  constructor(private renderer: Renderer2) {
  }

  ngOnInit() {

  }

  hide() {

  }


  expandRegister() {
    if (this.registerButton.nativeElement.classList.contains('button--active')) {
      return false;
    }

  }

  expandLogin() {
    if (this.loginButton.nativeElement.classList.contains('button--active')) {
      return false;
    }
    const windowWidth = this.authenticationContainer.nativeElement.width;
    const loginWidth = this.loginButton.nativeElement.innerWidth;
    const loginHeight = this.loginButton.nativeElement.innerHeight;
    const loginOffset = this.loginButton.nativeElement.offset;

    const registerWidth = this.registerButton.nativeElement.innerWidth;
    const registerOffset = this.registerButton.nativeElement.offset;

    const loginContentWidth = this.loginContent.nativeElement.innerWidth;
    const loginContentHeight = this.loginContent.nativeElement.innerHeight;

    const loginTransX = (windowWidth / 2) - loginOffset.left - (loginWidth / 2);
    const registerTransX = (windowWidth / 2) - registerOffset.left - (registerWidth / 2);

    const loginContentScaleX = loginContentWidth / loginWidth;
    const loginContentScaleY = loginContentHeight / loginHeight;

    this.renderer.setStyle(this.loginButton.nativeElement, 'z-index', 2);
    this.renderer.setStyle(this.loginButton.nativeElement, 'transform', 'translateX(' + loginTransX + 'px)');

    this.renderer.setStyle(this.registerButton.nativeElement, 'z-index', 0);
    this.renderer.setStyle(this.registerButton.nativeElement, 'opacity', 0);
    this.renderer.setStyle(this.registerButton.nativeElement, 'transition-delay', '0.05a');
    this.renderer.setStyle(this.registerButton.nativeElement, 'transform', 'translateX(' + registerTransX + 'px)');

    this.renderer.listen(this.loginButton.nativeElement, 'transitionend webkitTransitionEnd', () => {
      this.renderer.setStyle(this.loginButton.nativeElement,
        'translateX(' + loginTransX + 'px) scale(' + loginContentScaleX + ',' + loginContentScaleY + ')', []);
    });
    this.renderer.addClass(this.loginButton, 'button--active');
    this.renderer.addClass(this.loginContent, 'button__content--active');

    this.renderer.setStyle(this.loginContent, 'transition', 'll 1s 0.1s cubic-bezier(0.23, 1, 0.32, 1)');
    this.loginButton.nativeElement.off('ransitionend webkitTransitionEnd');

  }

}
