import {BranchType} from './BranchType';
import {PublicUserDto} from '../authentication/PublicUserDto';
import {NetworkDto} from "../network/runtime/NetworkDto";

export interface BranchDto {
  id: number;
  sourceId: number;
  name: string;
  type: BranchType;
  owner: PublicUserDto;
  networks: NetworkDto[];
  contributors: PublicUserDto[];
  created: string;
  updated: string;
}

