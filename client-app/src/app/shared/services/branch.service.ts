import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BranchDto} from '../models/branch/BranchDto';
import {Observable} from 'rxjs';
import {APP_SETTINGS} from '../../configs/app-settings.config';

@Injectable({
  providedIn: 'root'
})
export class BranchService {


  public branchChanged = new EventEmitter<string>();

  constructor(private http: HttpClient) {
  }


  getAllForOwner(username: string): Observable<BranchDto[]> {
    return this.http.get<BranchDto[]>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_ALL_FOR_OWNER + username);
  }

  getAllForContributor(username: string): Observable<BranchDto[]> {
    return this.http.get<BranchDto[]>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_ALL_FOR_CONTRIBUTOR + username);
  }

  getAllForUser(username: string): Observable<BranchDto[]> {
    return this.http.get<BranchDto[]>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_ALL_FOR_USER + username);
  }

  getById(id: number): Observable<BranchDto> {
    return this.http.get<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_BY_ID + id);
  }

  getByName(name: string): Observable<BranchDto> {
    return this.http.get<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_BY_NAME + name);
  }

  create(branch: BranchDto): Observable<BranchDto> {
    return this.http.post<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.POST_CREATE, branch);
  }

  update(branch: BranchDto): Observable<BranchDto> {
    return this.http.put<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.PUT_UPDATE, branch);
  }

  delete(id: number): Observable<BranchDto> {
    return this.http.delete<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.DELETE_DELETE + id);
  }

  assignWorkingBranch(username: string, branchName: string): Observable<void> {
    return this.http.post<void>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.POST_ASSIGN_WORKING_BRANCH + branchName, username);
  }

  signalBranchChange(branchName: string) {
    this.branchChanged.emit(branchName);
  }

  getCurrentWorkingBranch(username: string): Observable<BranchDto> {
    return this.http.get<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_WORKING_BRANCH + username);
  }

}
