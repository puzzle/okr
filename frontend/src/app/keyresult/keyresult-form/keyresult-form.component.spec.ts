import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyresultFormComponent } from './keyresult-form.component';

describe('KeyresultFormComponent', () => {
  let component: KeyresultFormComponent;
  let fixture: ComponentFixture<KeyresultFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ KeyresultFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KeyresultFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
