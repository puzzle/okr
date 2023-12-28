import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ActivatedRoute} from '@angular/router';
import {ChangeDetectorRef} from '@angular/core';
import {of} from 'rxjs';
import {MemberListComponent} from './member-list.component';
import {UserService} from '../../services/user.service';

const userServiceMock = {
  getUsers: jest.fn()
}

const activatedRouteMock = {
  paramMap: jest.fn()
}

describe('MemberListComponent', () => {
  let component: MemberListComponent;
  let fixture: ComponentFixture<MemberListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemberListComponent],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        ChangeDetectorRef,
      ],
    });

    fixture = TestBed.createComponent(MemberListComponent);
    component = fixture.componentInstance;

    jest.spyOn(userServiceMock, 'getUsers').mockReturnValue(of([]));
    jest.spyOn(activatedRouteMock, 'paramMap').mockReturnValue(of(1));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Add more test cases as needed
});
