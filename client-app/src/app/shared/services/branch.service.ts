import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BranchDto} from '../models/branch/BranchDto';
import {Observable} from 'rxjs';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {TIMEOUT} from '../config/timeout-config';

@Injectable({
  providedIn: 'root'
})
export class BranchService {


  public branchChanged = new EventEmitter<string>();
  public branchAdded = new EventEmitter<void>();

  constructor(private http: HttpClient) {
  }


  getAllForOwner(username: string): Observable<BranchDto[]> {
    return this.http.get<BranchDto[]>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_ALL_FOR_OWNER + username).timeout(TIMEOUT);
  }

  getAllForContributor(username: string): Observable<BranchDto[]> {
    return this.http.get<BranchDto[]>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_ALL_FOR_CONTRIBUTOR + username).timeout(TIMEOUT);
  }

  getAllForUser(username: string): Observable<BranchDto[]> {
    return this.http.get<BranchDto[]>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_ALL_FOR_USER + username).timeout(TIMEOUT);
  }

  getById(id: number): Observable<BranchDto> {
    return this.http.get<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_BY_ID + id).timeout(TIMEOUT);
  }

  getByName(name: string): Observable<BranchDto> {
    return this.http.get<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_BY_NAME + name).timeout(TIMEOUT);
  }

  create(branch: BranchDto): Observable<BranchDto> {
    return this.http.post<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.POST_CREATE, branch).timeout(TIMEOUT);
  }

  update(branch: BranchDto): Observable<BranchDto> {
    return this.http.put<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.PUT_UPDATE, branch).timeout(TIMEOUT);
  }

  delete(id: number): Observable<BranchDto> {
    return this.http.delete<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.DELETE_DELETE + id).timeout(TIMEOUT);
  }

  assignWorkingBranch(username: string, branchName: string): Observable<void> {
    return this.http.post<void>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.POST_ASSIGN_WORKING_BRANCH + branchName, username).timeout(TIMEOUT);
  }

  signalBranchChange(branchName: string) {
    this.branchChanged.emit(branchName);
  }

  getCurrentWorkingBranch(username: string): Observable<BranchDto> {
    return this.http.get<BranchDto>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_WORKING_BRANCH + username).timeout(TIMEOUT);
  }

  pull(destinationId: number, sourceId: number): Observable<void> {
    return this.http.get<void>(APP_SETTINGS.URLS.BRANCH_MANAGEMENT.GET_PULL_DESTINATION_SOURCE
      + destinationId + '/' + sourceId).timeout(TIMEOUT);
  }

}
