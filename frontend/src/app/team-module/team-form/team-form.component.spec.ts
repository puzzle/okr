import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamFormComponent } from './team-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

let component: TeamFormComponent;
let fixture: ComponentFixture<TeamFormComponent>;

beforeEach(async () => {
  await TestBed.configureTestingModule({
    imports: [HttpClientTestingModule, RouterTestingModule, FormsModule],
    declarations: [TeamFormComponent],
  }).compileComponents();

  fixture = TestBed.createComponent(TeamFormComponent);
  component = fixture.componentInstance;
  fixture.detectChanges();
});

describe('TeamFormComponent Creating', () => {
  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should isCreating set on true default', () => {
    expect(component.isCreating).toEqual(true);
  });

  test('should have 1 input filed with right placeholder', () => {
    const inputFileds = fixture.debugElement.queryAll(By.css('input'));
    expect(inputFileds.length).toEqual(1);

    expect(inputFileds[0].nativeElement.placeholder).toEqual('Team hinzufügen');
  });
});

describe('TeamFormComponent Editing', () => {
  beforeEach(async () => {
    component.isCreating = false;
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should isCreating set on false', () => {
    expect(component.isCreating).toEqual(false);
  });

  test('should have 1 input filed with right placeholder', () => {
    const inputFileds = fixture.debugElement.queryAll(By.css('input'));
    expect(inputFileds.length).toEqual(1);

    expect(inputFileds[0].nativeElement.placeholder).toEqual('Team bearbeiten');
  });
});
