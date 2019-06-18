import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../../services/authentication.service';
import {first} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material';
import {Router} from "@angular/router";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  @Output() submitEmitter = new EventEmitter();

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
  }


  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    this.authenticationService.obtainAccessToken(this.form.value)
      .pipe(first())
      .subscribe(
        data => {
          localStorage.setItem('username', this.form.value.username);
          this.clearData();
          this.authenticationService.saveToken(data);
          this.snackBar.open('Login successful.', 'Dismiss');
          this.submitEmitter.emit();
        },
        error => {
          this.snackBar.open('Invalid credentials', 'Dismiss');
        });

  }

  clearData() {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

}
