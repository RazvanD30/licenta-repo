import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-network-console',
  templateUrl: './network-console.component.html',
  styleUrls: ['./network-console.component.scss']
})
export class NetworkConsoleComponent implements OnInit {

  @ViewChild('input') input: ElementRef;
  @ViewChild('output') output: ElementRef;
  public outputs: string[] = [];
  public connected = false;
  public time: string;
  public active = false;
  public savedText = '';

  @HostListener('document:keydown', ['$event'])
  onMouseMove(event) {
    if (event.keyCode === 113) {
      this.active = !this.active;
      if (this.active === true) {
        this.input.nativeElement.value = this.savedText;
      } else {
        this.savedText = this.input.nativeElement.value;
      }
    }
  }

  ngOnInit(): void {

  }

  consoleKeyPress(event) {
    let command;
    if (event.which === 13) {
      command = this.input.nativeElement.value.trim();
      this.input.nativeElement.value = '';
    }
    switch (command) {
      case 'help':
        this.help();
        break;
      case 'clear':
        this.clear();
        break;
      case 'force-connect':
        this.forceConnect();
        break;
    }
  }


  forceConnect() {
    this.time = '0.1234';
    this.connected = true;
  }

  help() {
    this.outputText('Want help?');
  }

  clear() {

  }

  focusInput() {
    this.input.nativeElement.focus();
  }


  outputText(text: string): void {
    this.outputs.push(text);
  }

}
