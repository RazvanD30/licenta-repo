import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroupDirective, NgForm} from '@angular/forms';
import {ErrorStateMatcher, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return (control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}


@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.scss']
})
export class UsersTableComponent implements OnInit {

  ngOnInit(): void {
  }

  /*
    users: PublicUserDto[];
    displayedColumns: string[];
    editable: boolean;
    usersDataSource: MatTableDataSource<PublicUserDto>;
    filters;
    gradeErrorMatcher;
    fc;
    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

    constructor(private userService: UserService) {
    }

    ngOnInit() {
      this.filters = {
        username: '',
        name: '',
        role: '',
        supervisorUsername: '',
        permissions: ''
      };
      this.userService.getAll().subscribe(users => {
        this.users = users;
        this.usersDataSource = new MatTableDataSource(this.users);
        this.usersDataSource.paginator = this.paginator;
        this.usersDataSource.sortingDataAccessor = (user: User, property: string) => {
          switch (property) {
            case 'role':
              return user.role.type;
            default:
              return user[property];
          }
        };
        this.usersDataSource.filterPredicate = (user: User, filters: string) => this.filterPredicate(user);
        this.usersDataSource.sort = this.sort;

        this.resetFilters();
      });
      this.displayedColumns = ['name', 'username', 'role', 'permissions', 'email', 'phoneNumber', 'supervisorUsername', 'lastActiveOn'];
      this.editable = true;
      this.gradeErrorMatcher = new MyErrorStateMatcher();
      this.fc = [];
      this.resetFormControl();

    }

    filterPredicate(user: User): boolean {
      let ok = true;
      if (this.filters.username !== '') {
        ok = ok === true && user.username.toLowerCase().includes(this.filters.username.toLowerCase());
      }
      if (this.filters.name !== '') {
        ok = ok === true && ((user.lastName + ' ' + user.firstName).trim().toLowerCase().includes(this.filters.name.trim().toLowerCase()));
      }
      if (this.filters.role !== '') {
        ok = ok === true && user.role.type.toLowerCase().includes(this.filters.role.toLowerCase());
      }
      if (this.filters.permissions !== '') {
        ok = ok === true && (user.role.permissions.find(p => p.name === this.filters.permissions) !== null
          || user.additionalPermissions === this.filters.permissions);
      }
      if (this.filters.supervisorUsername !== '') {
        ok = ok === true && user.supervisorUsername.toLowerCase().includes(this.filters.supervisorUsername.toLowerCase());
      }
      return ok;
    }


    getRowIndexForUser(user: User) {
      return this.users.findIndex(u => u.id === user.id);
    }

    resetFormControl() {
      // this.fc = this.gradeFormControls;
      // this.gradeFormControls.forEach(f => this.fc.push(new FormControl(f.value)));
    }

    applyFilter(filterValue: string, filterColumn: string) {

      this.filters[filterColumn] = filterValue;
      this.usersDataSource.filter = 'a' + filterValue.trim().toLowerCase();

      if (this.usersDataSource.paginator) {
        this.usersDataSource.paginator.firstPage();
      }
    }

    resetFilters() {
      this.filters = {
        username: '',
        name: '',
        role: '',
        supervisorUsername: '',
        permissions: ''
      };
      this.applyFilter('', null);
    }

    getAveragePresence(): number {
      return 5;
    }

    getAverageGrade(): number {
      return 10;

    }

    save() {

    }*/
}
