import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkSpecialRoutineComponent } from './network-special-routine.component';

describe('NetworkSpecialRoutineComponent', () => {
  let component: NetworkSpecialRoutineComponent;
  let fixture: ComponentFixture<NetworkSpecialRoutineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkSpecialRoutineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkSpecialRoutineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
