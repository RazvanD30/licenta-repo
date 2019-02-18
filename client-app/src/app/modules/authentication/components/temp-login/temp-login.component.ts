import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../../../core/services/authentication.service';
import {AlertService} from '../../../../core/services/alert.service';
import {first} from 'rxjs/operators';
import {LoginForm} from '../../../../shared/models/LoginForm';

@Component({
  selector: 'app-login',
  templateUrl: './temp-login.component.html',
  styleUrls: ['./temp-login.component.scss']
})
export class TempLoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  loginSelected = false;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private alertService: AlertService) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    // reset login status
    this.authenticationService.logout();

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    const loginForm = new LoginForm(this.f.username.value, this.f.password.value);
    this.authenticationService.login(loginForm)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.alertService.error(error);
          this.loading = false;
        });
  }

  getContFormsClassName() {
    if (this.loginSelected === true) {
      return 'cont_forms cont_forms_active_login';
    }
    return 'cont_forms';
  }

  onLoginClick() {
    this.loginSelected = true;
  }

  getConstFormLoginStyle() {

  }

  cambiar_login() {
    document.querySelector('.cont_forms').className = 'cont_forms cont_forms_active_login';
    (document.querySelector('.cont_form_login') as HTMLElement).style.display = 'block';
    (document.querySelector('.cont_form_sign_up') as HTMLElement).style.opacity = '0';
    (document.querySelector('.col_md_login .cont_ba_opcitiy') as HTMLElement).style.opacity = '0';
    (document.querySelector('.col_md_sign_up .cont_ba_opcitiy') as HTMLElement).style.opacity = '1';
    (document.querySelector('.cont_back_info') as HTMLElement).style.boxShadow = 'none';

    setTimeout(() => {
      (document.querySelector('.cont_form_login') as HTMLElement).style.opacity = '1';
    }, 400);

    setTimeout(() => {
      (document.querySelector('.cont_form_sign_up') as HTMLElement).style.display = 'none';
    }, 200);
  }

  cambiar_sign_up(at) {
    document.querySelector('.cont_forms').className = 'cont_forms cont_forms_active_sign_up';
    (document.querySelector('.cont_form_sign_up') as HTMLElement).style.display = 'block';
    (document.querySelector('.cont_form_login') as HTMLElement).style.opacity = '0';
    (document.querySelector('.col_md_login .cont_ba_opcitiy') as HTMLElement).style.opacity = '1';
    (document.querySelector('.col_md_sign_up .cont_ba_opcitiy') as HTMLElement).style.opacity = '0';
    (document.querySelector('.cont_back_info') as HTMLElement).style.boxShadow = 'none';



    setTimeout(() => {
      (document.querySelector('.cont_form_sign_up') as HTMLElement).style.opacity = '1';
    }, 400);

    setTimeout(() => {
      (document.querySelector('.cont_form_login') as HTMLElement).style.display = 'none';
    }, 200);
  }

  ocultar_login_sign_up() {

    document.querySelector('.cont_forms').className = 'cont_forms';
    (document.querySelector('.cont_form_sign_up') as HTMLElement).style.opacity = '0';
    (document.querySelector('.cont_form_login') as HTMLElement).style.opacity = '0';

    setTimeout(() => {
      (document.querySelector('.cont_form_sign_up') as HTMLElement).style.display = 'none';
      (document.querySelector('.cont_form_login') as HTMLElement).style.display = 'none';
    }, 100);
  }

}


