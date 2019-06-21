import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkInitialization } from './network-initialization.component';

describe('NetworkInitialization', () => {
  let component: NetworkInitialization;
  let fixture: ComponentFixture<NetworkInitialization>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkInitialization ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkInitialization);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
