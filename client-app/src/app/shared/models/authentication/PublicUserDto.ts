import {RoleDto} from './RoleDto';

export interface PublicUserDto {
  id: number;
  username: string;
  role: RoleDto;
}
