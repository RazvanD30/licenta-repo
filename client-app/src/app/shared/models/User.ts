import {Role} from './Role';
import {Permission} from './Permission';

export class User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  role: Role;
  phoneNumber: string;
  email: string;
  supervisorUsername: string;
  lastActiveOn: string;
  additionalPermissions: Permission[];

  constructor(id: number, username: string, firstName: string, lastName: string, role: Role, phoneNumber: string, email: string,
              supervisorUsername: string, lastActiveOn: string, additionalPermissions: Permission[]) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.supervisorUsername = supervisorUsername;
    this.lastActiveOn = lastActiveOn;
    this.additionalPermissions = additionalPermissions;
  }

}
