import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';


export enum SpecialRoutineOperation {
  IGNORE_NODE,
  DECREASE_OUTPUT_WEIGHTS,
  DECREASE_INPUT_WEIGHTS,
  INCREASE_OUTPUT_WEIGHTS,
  INCREASE_INPUT_WEIGHTS,
  ALWAYS_OUTPUT_MAX,
  ALWAYS_OUTPUT_MIN,
  ADD_TO_WATCHLIST
}

export interface SpecialRoutineData {
  id: number;
  operation: SpecialRoutineOperation;
  parameter: string;
}

@Component({
  selector: 'app-network-special-routine',
  templateUrl: './network-special-routine.component.html',
  styleUrls: ['./network-special-routine.component.scss']
})
export class NetworkSpecialRoutineComponent {

  operations = [
    {name: 'Ignore targetNode', value: SpecialRoutineOperation.IGNORE_NODE},
    {
      name: 'Decrease output weights',
      value: SpecialRoutineOperation.DECREASE_OUTPUT_WEIGHTS,
      paramDesc: 'The relative amount to subtract'
    },
    {
      name: 'Decrease input weights',
      value: SpecialRoutineOperation.DECREASE_INPUT_WEIGHTS,
      paramDesc: 'The relative amount to subtract'
    },
    {
      name: 'Increase output weights',
      value: SpecialRoutineOperation.INCREASE_OUTPUT_WEIGHTS,
      paramDesc: 'The relative amount to add'
    },
    {
      name: 'Increase input weights',
      value: SpecialRoutineOperation.INCREASE_INPUT_WEIGHTS,
      paramDesc: 'The relative amount to add'
    },
    {name: 'Always output maximum value', value: SpecialRoutineOperation.ALWAYS_OUTPUT_MAX},
    {name: 'Always output minimum value', value: SpecialRoutineOperation.ALWAYS_OUTPUT_MIN},
    {
      name: 'Add to watchlist',
      value: SpecialRoutineOperation.ADD_TO_WATCHLIST,
      paramDesc: 'Tag associated to targetNode'
    }
  ];

  constructor(
    public dialogRef: MatDialogRef<NetworkSpecialRoutineComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SpecialRoutineData) {
  }

  getParamDescFor(operation: SpecialRoutineOperation) {
    const foundOp = this.operations.find(op => op.value === operation);
    return foundOp != null ? foundOp.paramDesc : null;
  }

  onClose() {
    this.dialogRef.close();
  }

}
