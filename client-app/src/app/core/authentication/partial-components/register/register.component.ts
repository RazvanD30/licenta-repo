import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../../services/authentication.service';
import {MatSnackBar} from '@angular/material';
import {PrivateUserDto} from '../../../../shared/models/authentication/PrivateUserDto';
import {RoleDto} from '../../../../shared/models/authentication/RoleDto';

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

  // convenience getter for easy access to form fields
  get f() {
    return this.form.controls;
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required],
      phone: ['', Validators.required]
    });
  }

  onSubmit() {
    // stop here if form is invalid
    if (this.form.invalid === true) {
      return;
    }

    const user: PrivateUserDto = {
      id: null,
      username: this.form.value.username,
      password: this.form.value.password,
      roles: [RoleDto.ROLE_USER]
    };
    this.authenticationService.register(user).subscribe(() => {
      this.snackBar.open('Your account has been created. You can now login.', 'Dismiss', {duration: 4000});
      this.submitEmitter.emit();
      this.clearData();
    }, () => {
      this.snackBar.open('Account creation failed.', 'Dismiss', {duration: 4000});
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
