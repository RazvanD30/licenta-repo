import {RoleDto} from './RoleDto';

export interface PrivateUserDto {
  id: number;
  username: string;
  password: string;
  roles: RoleDto[];
}
