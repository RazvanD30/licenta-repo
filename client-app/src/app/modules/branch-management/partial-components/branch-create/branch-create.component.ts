import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {BranchTypeParser} from '../../../../shared/models/branch/BranchType';
import {BranchDto} from '../../../../shared/models/branch/BranchDto';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PublicUserDto} from '../../../../shared/models/authentication/PublicUserDto';

@Component({
  selector: 'app-branch-create',
  templateUrl: './branch-create.component.html',
  styleUrls: ['./branch-create.component.scss']
})
export class BranchCreateComponent implements OnInit {

  types: string[] = BranchTypeParser.names();
  branch: BranchDto;
  @Input() branches: BranchDto[];
  @Input() users: PublicUserDto[];
  @Input() loggedUsername: string;
  branchFormGroup: FormGroup;
  @Output() branchCreated = new EventEmitter<BranchDto>();

  constructor(private formBuilder: FormBuilder) {
  }

  resetModel() {
    this.branch = {
      id: null,
      sourceId: null,
      name: null,
      type: null,
      owner: {
        id: null,
        username: null,
        role: null
      },
      contributors: [],
      created: null,
      updated: null
    };
    this.branchFormGroup = this.formBuilder.group({
      name: ['', [Validators.required]],
      type: ['', [Validators.required]],
      contributors: [[], []],
      sourceId: [null]
    });
  }

  ngOnInit() {
    this.resetModel();
  }

  getUsers() {
    return this.users.filter(u => u.username !== this.loggedUsername);
  }

  create() {
    this.branchCreated.emit({
      id: null,
      sourceId: this.branchFormGroup.value.sourceId,
      name: this.branchFormGroup.value.name,
      type: this.branchFormGroup.value.type,
      owner: {
        id: null,
        username: null,
        role: null
      },
      contributors: this.branchFormGroup.value.contributors,
      created: null,
      updated: null
    });
  }
}
