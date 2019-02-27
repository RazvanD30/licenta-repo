import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/operators';
import {AlertService} from '../../../services/alert.service';
import {AuthenticationService} from '../../../services/authentication.service';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  form: FormGroup;
  @Output() submitEmitter = new EventEmitter();

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required],
      phone: ['', Validators.required]
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.form.controls;
  }

  onSubmit() {

    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }

    this.authenticationService.register(this.form.value)
      .pipe(first())
      .subscribe(
        data => {
          this.clearData();
          this.snackBar.open('Registration successful.', 'Dismiss');
          this.submitEmitter.emit();
        },
        error => {
          this.snackBar.open('Error', 'Dismiss');
        });
  }

  clearData() {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required],
      phone: ['', Validators.required]
    });
  }
}
