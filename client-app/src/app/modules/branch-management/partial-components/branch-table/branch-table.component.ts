import { Component, OnInit } from '@angular/core';
import {BranchService} from '../../../../shared/services/branch.service';
import {BranchDto} from '../../../../shared/models/branch/BranchDto';
import {BranchType} from '../../../../shared/models/branch/BranchType';

@Component({
  selector: 'app-branch-table',
  templateUrl: './branch-table.component.html',
  styleUrls: ['./branch-table.component.scss']
})
export class BranchTableComponent implements OnInit {

  types: BranchType[] = [ BranchType.TEST, BranchType.QA, BranchType.PROD ];
  branch: BranchDto;
  branches: BranchDto[] = [];
  username: string = localStorage.getItem('username');


  constructor(private branchService: BranchService) { }

  resetModel() {
    this.branch = {
      id: null,
      sourceId: null,
      name: null,
      type: null,
      owner: {
        id: null,
        username: this.username,
        role: null
      },
      networks: [],
      contributors: [],
      created: null,
      updated: null
    };
  }

  ngOnInit() {
    this.resetModel();
    this.loadForUser();
  }

  loadForUser() {
    this.branchService.getAllForUser(this.username).subscribe(data => this.branches = data);
  }

  loadForContributor() {
    this.branchService.getAllForContributor(this.username).subscribe(data => this.branches = data);
  }

  loadForOwner() {
    this.branchService.getAllForOwner(this.username).subscribe(data => this.branches = data);
  }

  create() {
    this.branchService.create(this.branch).subscribe(response => this.resetModel());
  }

  update() {
    this.branchService.update(this.branch).subscribe(response => this.resetModel());
  }

  remove() {
    this.branchService.delete(this.branch.id).subscribe(response => this.resetModel());
  }
}
