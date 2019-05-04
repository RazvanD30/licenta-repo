import {Injectable} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {delay, dematerialize, materialize, mergeMap} from 'rxjs/operators';
import {APP_SETTINGS} from '../../configs/app-settings.config';
import {Layer} from '../../shared/models/network/Layer';
import {NeuralNode} from '../../shared/models/network/NeuralNode';
import {Link} from '../../shared/models/network/Link';
import {Status} from '../../shared/models/network/Status';
import {Network} from '../../shared/models/network/Network';
import {NetworkConnection} from '../../shared/models/network/NetworkConnection';

@Injectable()
export class AuthenticateMock implements HttpInterceptor {


  intercept(request: HttpRequest<any>, next: HttpHandler) {


    const users: any[] = JSON.parse(localStorage.getItem('mock-users')) || [];
    return of(null).pipe(mergeMap(() => {


      if (request.url === APP_SETTINGS.URLS.AUTHENTICATION.LOGIN && request.method === 'POST') {

        const filteredUsers = users.filter(user => {
          return user.username === request.body.username && user.password === request.body.password;
        });
        if (filteredUsers.length) {
          // if login details are valid return 200 OK with user details and fake jwt token
          const user = filteredUsers[0];
          const body = {
            id: user.id,
            username: user.username,
            firstName: user.firstName,
            lastName: user.lastName,
            token: 'fake-jwt-token'
          };

          return of(new HttpResponse({status: 200, body}));
        } else {
          // else return 400 bad request
          return throwError({error: {message: 'Username or password is incorrect'}});
        }

      }

      if (request.url === APP_SETTINGS.URLS.AUTHENTICATION.LOGOUT && request.method === 'POST') {

        const filteredUsers = users.filter(user => {
          return user.username === request.body.username;
        });

        if (filteredUsers.length) {
          // if login details are valid return 200 OK with user details and fake jwt token
          const user = filteredUsers[0];
          const body = {
            id: user.id,
            username: user.username,
            firstName: user.firstName,
            lastName: user.lastName,
            token: 'fake-jwt-token'
          };

          return of(new HttpResponse({status: 200, body}));
        } else {
          // else return 400 bad request
          return throwError({error: {message: 'Username not logged in'}});
        }

      }


      if (request.url === APP_SETTINGS.URLS.AUTHENTICATION.REGISTER && request.method === 'POST') {
        // get new user object from post body
        const newUser = request.body;

        // validation
        const duplicateUser = users.filter(user => {
          return user.username === newUser.username;
        }).length;
        if (duplicateUser) {
          return throwError({error: {message: 'Username "' + newUser.username + '" is already taken'}});
        }

        // save new user
        newUser.id = users.length + 1;
        users.push(newUser);
        const body = {
          id: newUser.id,
          username: newUser.username,
          firstName: newUser.firstName,
          lastName: newUser.lastName,
          token: 'fake-jwt-token'
        };
        localStorage.setItem('mock-users', JSON.stringify(users));

        // respond 200 OK
        return of(new HttpResponse({status: 200, body}));
      }

      if (request.url === APP_SETTINGS.URLS.USER_MANAGEMENT.GET_ALL && request.method === 'GET') {
        return of(new HttpResponse({status: 200, body: users}));
      }

      if (request.url === APP_SETTINGS.URLS.NETWORK_DEBUG.GET_ALL && request.method === 'GET') {


        const network1 = this.getNetwork(10, 1, 2, 10, 0);
        const network2 = this.getNetwork(6, 1, 1, 12, 100000);
        const network3 = this.getNetwork(3, 3, 1, 15, 100000000);

        const networks: Network[] = [network1, network2, network3];

        return of(new HttpResponse({status: 200, body: networks}));
      }

      if (request.url.startsWith(APP_SETTINGS.URLS.NETWORK_DEBUG.GET_CONNECTIONS) && request.method === 'GET') {

        const connection1 = new NetworkConnection(0, 100000000, 11, 100000000);
        const connection2 = new NetworkConnection(100000,100000000,100007,100000000);

        for (let i = 0; i < 2; i++) {
          let link = new Link(-1 * i, 1, Status.INPUT, 11000 + i, 100000000 + i);
          connection1.links.push(link);
        }

        for(let i = 2; i < 3; i++){
          let link = new Link(-1*i,1,Status.INPUT,107000,100000000 + i );
          connection2.links.push(link);
        }

        const connections: NetworkConnection[] = [connection1,connection2];

        return of(new HttpResponse({status: 200, body: connections}))
      }

      if (request.url.startsWith(APP_SETTINGS.URLS.USER_MANAGEMENT.DELETE) && request.method === 'DELETE') {

        const id = parseInt(request.url[request.url.length - 1], 10);
        console.log(id);
        let deletedUser;
        const filteredUsers = users.filter(currentUser => {
          if (currentUser.id === id) {
            deletedUser = currentUser;
            return false;
          }
          return true;
        });
        console.log(filteredUsers);
        localStorage.setItem('mock-users', JSON.stringify(filteredUsers));

        return of(new HttpResponse({status: 200, body: deletedUser}));
      }
    })).pipe(materialize())
      .pipe(delay(500))
      .pipe(dematerialize());
  }

  getNetwork(HIDDEN_LAYERS: number, INPUT_NODES: number, OUTPUT_NODES: number, NODES_PER_HIDDEN_LAYER: number, ID_ADD: number): Network {
    const TOTAL_LAYERS = HIDDEN_LAYERS + 2;

    const layers: Layer[] = [];

    // layers
    for (let i = 0; i < TOTAL_LAYERS; i++) {
      const layer: Layer = new Layer(ID_ADD + i);
      if (i > 0) {
        layers[i - 1].next = layer;
      }
      layers.push(layer);
    }

    // input nNodes
    for (let i = 0; i < INPUT_NODES; i++) {
      const node: NeuralNode = new NeuralNode(ID_ADD + i, Status.INPUT);
      layers[0].nodes.push(node);
    }

    // output nNodes
    for (let i = 0; i < OUTPUT_NODES; i++) {
      const node: NeuralNode = new NeuralNode(ID_ADD + (layers.length - 1) * 1000 + i, Status.IGNORED);
      layers[layers.length - 1].nodes.push(node);
    }

    // hidden nNodes
    for (let l = 1; l <= HIDDEN_LAYERS; l++) {
      for (let n = 0; n < NODES_PER_HIDDEN_LAYER; n++) {
        const node: NeuralNode = new NeuralNode(ID_ADD + l * 1000 + n, Status.IGNORED);
        layers[l].nodes.push(node);
      }
    }

    // links
    for (let l = 0; l < TOTAL_LAYERS; l++) {
      for (let ns = 0; ns < layers[l].nodes.length; ns++) {
        for (let nt = 0; layers.length > l + 1 && nt < layers[l + 1].nodes.length; nt++) {
          const id = ID_ADD + l * 100000 + ns * 1000 + nt;
          const weight = Math.random();
          const status = id % 10 === 9 ? Status.UNDER_WATCH : Status.IGNORED;
          const source = layers[l].nodes[ns];
          const target = layers[l + 1].nodes[nt];
          const link: Link = new Link(id, weight, status, source.id, target.id);
          source.outputLinks.push(link);
        }
      }
    }
    return new Network(ID_ADD, 'Mocked neural network', 1234, 0.2, 500, 10, INPUT_NODES, OUTPUT_NODES, layers[0]);
  }

}


export let authenticateMockProvider = {
  // use fake backend in place of Http service for backend-less development
  provide: HTTP_INTERCEPTORS,
  useClass: AuthenticateMock,
  multi: true
};
