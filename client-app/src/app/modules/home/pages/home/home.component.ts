import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';

import {faCoffee} from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  title = 'app';
  faCoffee = faCoffee;

  ngOnInit(): void {
  }


}

