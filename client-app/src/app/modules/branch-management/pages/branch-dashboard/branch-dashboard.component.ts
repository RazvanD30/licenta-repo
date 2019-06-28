import {Component, OnInit} from '@angular/core';
import {BranchService} from '../../../../shared/services/branch.service';
import {BranchDto} from '../../../../shared/models/branch/BranchDto';
import {UserService} from '../../../../core/services/user.service';
import {PublicUserDto} from '../../../../shared/models/authentication/PublicUserDto';
import {AuthenticationService} from '../../../../core/services/authentication.service';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-branch-dashboard',
  templateUrl: './branch-dashboard.component.html',
  styleUrls: ['./branch-dashboard.component.scss']
})
export class BranchDashboardComponent implements OnInit {


  branches: BranchDto[] = [];
  users: PublicUserDto[] = [];
  username: string;

  constructor(private branchService: BranchService,
              private authenticationService: AuthenticationService,
              private userService: UserService,
              private snackBar: MatSnackBar) {
  }


  refreshData() {
    this.username = localStorage.getItem('username');
    if (this.username != null) {
      this.branchService.getAllForUser(this.username).subscribe(branches => {
        this.branches = branches;
      });
    }
  }

  ngOnInit() {
    this.refreshData();
    this.authenticationService.loggedIn.subscribe(() => this.refreshData());
    this.userService.getAll().subscribe(users => this.users = users);
    this.branchService.branchAdded.subscribe(() => {
      if (this.username != null) {
        this.branchService.getAllForUser(this.username).subscribe(branches => this.branches = branches);
      }
    });
  }

  create(branchDto: BranchDto) {
    branchDto.owner.username = this.username;
    this.branchService.create(branchDto).subscribe(() => {
      this.branchService.branchAdded.emit();
      this.snackBar.open('Branch created successfully', 'Dismiss', {duration: 3000});
    });
  }

}
