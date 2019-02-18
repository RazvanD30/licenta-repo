import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  @ViewChild('buttonLogin') buttonLogin: ElementRef;
  @ViewChild('buttonRegister') buttonRegister: ElementRef;
  @ViewChild('loginContent') loginContent: ElementRef;
  @ViewChild('registerContent') registerContent: ElementRef;
  private listener;


  constructor(private renderer: Renderer2) {
    this.renderer = renderer;
  }

  ngOnInit() {

  }

  expandLogin() {
    this.expand(this.buttonLogin, this.loginContent, this.buttonRegister);
  }

  expandRegister() {
    this.expand(this.buttonRegister, this.registerContent, this.buttonLogin);
  }


  hideLogin() {
    this.hide(this.buttonLogin, this.loginContent, this.buttonRegister);
  }

  hideRegister() {
    this.hide(this.buttonRegister, this.registerContent, this.buttonLogin);
  }

  hide(selectedButton: ElementRef, selectedContent: ElementRef, hiddenButton: ElementRef) {
    const selectedButtonNE = selectedButton.nativeElement;
    const selectedContentNE = selectedContent.nativeElement;
    const hiddenButtonNE = hiddenButton.nativeElement;

    this.listener();
    this.renderer.removeClass(selectedButtonNE, 'button--active');
    this.renderer.removeClass(selectedContentNE, 'button__content--active');
    this.renderer.removeStyle(selectedButtonNE, 'z-index');
    this.renderer.removeStyle(selectedButtonNE, 'transform');

    this.renderer.removeStyle(selectedContentNE, 'transition');
    this.renderer.setStyle(selectedContentNE, 'transition', 'all 0.1s 0 cubic-bezier(0.23, 1, 0.32, 1)');

    this.renderer.removeStyle(hiddenButtonNE, 'z-index');
    this.renderer.removeStyle(hiddenButtonNE, 'opacity');
    this.renderer.removeStyle(hiddenButtonNE, 'transition-delay');
    this.renderer.removeStyle(hiddenButtonNE, 'transform');
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

      this.renderer.setStyle(selectedButtonNE, 'z-index', 2);
      this.renderer.setStyle(selectedButtonNE, 'transform', 'translateX(' + selectedButtonTransX + 'px)');

      this.renderer.setStyle(hiddenButtonNE, 'z-index', 0);
      this.renderer.setStyle(hiddenButtonNE, 'opacity', 0);
      this.renderer.setStyle(hiddenButtonNE, 'transition-delay', '0.05s');
      this.renderer.setStyle(hiddenButtonNE, 'transform', 'translateX(' + hiddenButtonTransX + 'px)');

      this.listener = this.renderer.listen(selectedButtonNE, 'transitionend', () => {
        this.renderer.setStyle(selectedButtonNE,
          'transform', 'translateX(' + selectedButtonTransX + 'px) scale(' + selectedScaleX + ',' + selectedScaleY + ')');
      });

      this.renderer.addClass(selectedButtonNE, 'button--active');
      this.renderer.addClass(selectedContent.nativeElement, 'button__content--active');
      this.renderer.setStyle(selectedContent.nativeElement, 'transition', 'all 1s 0.3s cubic-bezier(0.23, 1, 0.32, 1)');

      this.renderer.listen(selectedButtonNE, 'transitionend', () => {
        console.log('aaa');
      });
    }
  }
}
