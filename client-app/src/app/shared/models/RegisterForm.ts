export class RegisterForm {
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  role: string;
  phoneNumber: string;
  email: string;
  supervisorUsername: string;

  constructor(username: string,
              password: string,
              firstName: string,
              lastName: string,
              role: string,
              phoneNumber: string,
              email: string,
              supervisorUsername: string) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.supervisorUsername = supervisorUsername;
  }

}
