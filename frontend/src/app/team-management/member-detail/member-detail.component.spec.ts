import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MemberDetailComponent} from './member-detail.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ActivatedRoute} from "@angular/router";
import {of} from "rxjs";
import {TranslateModule} from "@ngx-translate/core";
import {CommonModule} from "@angular/common";
import {BrowserModule} from "@angular/platform-browser";
import {SharedModule} from "../../shared/shared.module";
import {UserService} from "../../services/user.service";
import {testUser} from "../../shared/testData";
import {AddUserTeamComponent} from "../add-user-team/add-user-team.component";
import {MatTableModule} from "@angular/material/table";
import {MatIconModule} from "@angular/material/icon";

describe('MemberDetailComponent', () => {
  let component: MemberDetailComponent;
  let fixture: ComponentFixture<MemberDetailComponent>;

  const activatedRouteMock = {
    paramMap: of({
      get: (): any => 1
    })
  }

  const userServiceMock = {
    getUserById: jest.fn(),
    getCurrentUser: jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        MemberDetailComponent,
        AddUserTeamComponent,
      ],
      imports: [
        HttpClientTestingModule,
        TranslateModule.forRoot(),
        CommonModule,
        BrowserModule,
        SharedModule,
        MatTableModule,
        MatIconModule
      ],
      providers: [
        {provide: ActivatedRoute, useValue: activatedRouteMock},
        {provide: UserService, useValue: userServiceMock}
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MemberDetailComponent);
    component = fixture.componentInstance;

    userServiceMock.getUserById.mockReturnValue(of(testUser));
    userServiceMock.getCurrentUser.mockReturnValue(testUser);

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should set selectedUserIsLoggedInUser and currentUserTeams correctly', done => {
    component.ngOnInit();
    component.currentUserTeams$.subscribe(userTeams => {
      expect(userTeams).toStrictEqual(testUser.userTeamList);
      expect(component.user).toStrictEqual(testUser);
      expect(component.selectedUserIsLoggedInUser).toBeTruthy();
      done();
    })
  })


});
