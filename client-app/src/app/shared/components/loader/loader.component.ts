import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';

import {Operation} from './Operation';
import {map, share} from 'rxjs/operators';
import {interval, Observable} from 'rxjs';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {


  @ViewChild('startLoading') startLoadingBtn;
  @Input() operations: Operation[];
  private clock: Observable<Date>;
  private startingTime: any;
  private currentTime: any;

  constructor() {
  }

  ngOnInit() {
    this.startingTime = new Date();

    this.clock = interval(1000)
      .pipe(map(tick => new Date()),
        share()
      );
    this.clock.subscribe(v => this.currentTime = v);
    const op1: Operation = {
      id: 0,
      description: 'Creating schemas',
      estimatedSeconds: 8,
      done: false
    };
    const op2: Operation = {
      id: 1,
      description: 'Validating input',
      estimatedSeconds: 15,
      done: false
    };
    const op3: Operation = {
      id: 2,
      description: 'Blah blah blah',
      estimatedSeconds: 23,
      done: false
    };
    this.operations = [op1, op2, op3];
  }

  updateOperation(newOperation: Operation): void {
    const index: number = this.operations.findIndex(operation => operation.id === newOperation.id);
    this.operations[index] = newOperation;
  }

  getRemainingMinutes(operation: Operation): number {
    const elapsedSeconds = Math.floor((this.currentTime - this.startingTime) / 1000);
    return Math.floor((operation.estimatedSeconds - elapsedSeconds) / 60);
  }

  getRemainingSeconds(operation: Operation): number {
    const elapsedSeconds = Math.floor((this.currentTime - this.startingTime) / 1000);
    const result = (operation.estimatedSeconds - elapsedSeconds);
    return result >= 0 ? result % 60 : 0;
  }

  getStatusText(operation: Operation): string {
    if (!this.currentTime) {
      return '';
    }
    let result = 'Remaining: ';
    if (operation.estimatedSeconds > 60) {
      const minutes = this.getRemainingMinutes(operation);
      if (minutes > 0) {
        if (minutes > 1) {
          result += minutes + ' minutes ';
        } else {
          result += minutes + ' minute ';
        }
      }
    }
    const seconds = this.getRemainingSeconds(operation);
    if(seconds == 1){
      result += this.getRemainingSeconds(operation) + ' second';
    } else {
      result += this.getRemainingSeconds(operation) + ' seconds';
    }
    return result;
  }

  incrementTime(): void {
    this.operations[0].estimatedSeconds += 2;
  }

  decrementTime(): void {
    this.operations[0].estimatedSeconds -= 2;
  }

  done(): void {
    this.operations[0].estimatedSeconds = 0;
    this.operations[0].done = true;
  }
}
