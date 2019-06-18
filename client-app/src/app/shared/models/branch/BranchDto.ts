import {BranchType} from './BranchType';
import {PublicUserDto} from '../authentication/PublicUserDto';

export interface BranchDto {
  id: number;
  name: string;
  type: BranchType;
  owner: PublicUserDto;
  contributors: PublicUserDto[];
  created: string;
  updated: string;
}

