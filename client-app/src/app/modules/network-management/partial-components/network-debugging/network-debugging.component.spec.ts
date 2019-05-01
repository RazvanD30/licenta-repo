import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkDebuggingComponent } from './network-debugging.component';

describe('NetworkDebuggingComponent', () => {
  let component: NetworkDebuggingComponent;
  let fixture: ComponentFixture<NetworkDebuggingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkDebuggingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkDebuggingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
