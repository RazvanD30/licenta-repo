import {AfterViewInit, ChangeDetectorRef, Component, Inject, Input, OnInit} from '@angular/core';
import {faDirections, faInfo, faWeightHanging} from '@fortawesome/free-solid-svg-icons';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {NeuralNodeGui} from '../../../../shared/models/network/gui/NeuralNodeGui';
import {Status} from '../../../../shared/models/network/Status';
import {LinkGui} from '../../../../shared/models/network/gui/LinkGui';

@Component({
  selector: 'app-node-details',
  templateUrl: './node-details.component.html',
  styleUrls: ['./node-details.component.scss']
})
export class NodeDetailsComponent implements OnInit, AfterViewInit {

  public faDirections = faDirections;
  public faWeightHanging = faWeightHanging;
  public faInfo = faInfo;
  public loaded = false;
  public IGNORED = Status.IGNORED;
  public WAIT = Status.WAIT;
  public BREAKPOINT = Status.BREAKPOINT;
  public UNDER_WATCH = Status.UNDER_WATCH;
  public neighbours: NeuralNodeGui[];


  constructor(
    public dialogRef: MatDialogRef<NodeDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: NeuralNodeGui,
    public cdRef: ChangeDetectorRef
  ) {
    this.neighbours = this.getNeighours();
  }

  ngOnInit() {
  }

  async ngAfterViewInit() {
    const delay = ms => new Promise(res => setTimeout(res, ms));
    await delay(100);
    this.loaded = true;
    this.cdRef.detectChanges();
  }

  private getNeighours(): NeuralNodeGui[] {
    const result = [];
    this.data.inputLinks.forEach(conn => result.push(conn.source));
    this.data.inputLinks.forEach(conn => result.push(conn.destination));
    return result;
  }

  public getInputLinks(): LinkGui[] {
    return Array.from(this.data.inputLinks.values());
  }

  public getOutputLinks(): LinkGui[] {
    return Array.from(this.data.outputLinks.values());
  }
}
