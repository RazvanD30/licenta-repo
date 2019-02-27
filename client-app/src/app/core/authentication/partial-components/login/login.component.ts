import {AfterViewInit, Component, ElementRef, EventEmitter, OnInit, Output, Renderer2, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../../services/authentication.service';
import {first} from 'rxjs/operators';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material';


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
    private snackBar: MatSnackBar
  ) {
  }


  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    this.authenticationService.login(this.form.value)
      .pipe(first())
      .subscribe(
        data => {
          this.clearData();
          this.snackBar.open('Login successful.', 'Dismiss');
          this.submitEmitter.emit();
        },
        error => {
          this.snackBar.open('Error', 'Dismiss');
        });
  }

  clearData() {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

}
