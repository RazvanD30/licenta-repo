import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LayerTableComponent } from './layer-table.component';

describe('LayerTableComponent', () => {
  let component: LayerTableComponent;
  let fixture: ComponentFixture<LayerTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LayerTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LayerTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
