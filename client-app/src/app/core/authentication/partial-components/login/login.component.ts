import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../../services/authentication.service';
import {first} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material';
import {Router} from '@angular/router';
import {BranchService} from '../../../../shared/services/branch.service';


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
    private branchService: BranchService,
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
          this.authenticationService.loggedIn.emit();
          this.snackBar.open('Login successful.', 'Dismiss', {duration: 4000});
          this.branchService.branchAdded.emit();
          this.submitEmitter.emit();
        },
        error => {
          this.snackBar.open('Invalid credentials', 'Dismiss', {duration: 4000});
        });

  }

  clearData() {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

}
