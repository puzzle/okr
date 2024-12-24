import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewUserComponent } from './new-user.component';
import { FormControl, FormGroup, FormsModule, NgForm, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';
import { PuzzleIconButtonComponent } from '../../shared/custom/puzzle-icon-button/puzzle-icon-button.component';
import { PuzzleIconComponent } from '../../shared/custom/puzzle-icon/puzzle-icon.component';
import { CommonModule } from '@angular/common';
import { NewUserForm } from '../../shared/types/model/new-user-form';

describe('NewUserComponent', () => {
  let component: NewUserComponent;
  let fixture: ComponentFixture<NewUserComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [NewUserComponent,
        PuzzleIconButtonComponent,
        PuzzleIconComponent],
      imports: [
        FormsModule,
        ReactiveFormsModule,
        SharedModule,
        CommonModule
      ],
      providers: [NgForm]
    })
      .compileComponents();

    fixture = TestBed.createComponent(NewUserComponent);
    component = fixture.componentInstance;

    component.userFormGroup = new FormGroup<NewUserForm<FormControl>>({
      firstName: new FormControl<any>('user1'),
      lastName: new FormControl<any>('user'),
      email: new FormControl<any>('test@test.ch')
    });

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
