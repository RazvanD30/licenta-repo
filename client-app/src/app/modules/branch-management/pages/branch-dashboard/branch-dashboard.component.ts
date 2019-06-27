import {Component, OnInit} from '@angular/core';
import {BranchService} from '../../../../shared/services/branch.service';
import {BranchDto} from '../../../../shared/models/branch/BranchDto';
import {UserService} from '../../../../core/services/user.service';
import {PublicUserDto} from '../../../../shared/models/authentication/PublicUserDto';

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
              private userService: UserService) {
  }

  ngOnInit() {
    this.username = localStorage.getItem('username');
    this.branchService.getAllForUser(this.username).subscribe(branches => this.branches = branches);
    this.branchService.branchAdded.subscribe(val => {
      this.branchService.getAllForUser(this.username).subscribe(branches => this.branches = branches);
    });
    this.userService.getAll().subscribe(users => this.users = users);
  }

  create(branchDto: BranchDto) {
    branchDto.owner.username = this.username;
    this.branchService.create(branchDto).subscribe(response => {
      this.branchService.branchAdded.emit();
    });
  }

}
