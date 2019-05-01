import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkConsoleComponent } from './network-console.component';

describe('NetworkConsoleComponent', () => {
  let component: NetworkConsoleComponent;
  let fixture: ComponentFixture<NetworkConsoleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkConsoleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkConsoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
