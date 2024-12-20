import { ComponentFixture, TestBed } from "@angular/core/testing";

import { MemberListMobileComponent } from "./member-list-mobile.component";
import { Team } from "../../../shared/types/model/Team";
import { BehaviorSubject } from "rxjs";
import { team1 } from "../../../shared/testData";
import { UserTableEntry } from "../../../shared/types/model/UserTableEntry";
import { MatTableDataSource } from "@angular/material/table";

describe("MemberListMobileComponent", () => {
  let component: MemberListMobileComponent;
  let fixture: ComponentFixture<MemberListMobileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MemberListMobileComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberListMobileComponent);
    component = fixture.componentInstance;

    component.dataSource = new MatTableDataSource<UserTableEntry>([]);

    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component)
      .toBeTruthy();
  });

  it("should navigate to correct path if no team is selected", () => {
    component.selectedTeam$ = new BehaviorSubject<Team | undefined>(undefined);
    const userTableEntry: any = {
      id: 1
    };
    expect(component.getMemberDetailsLink(userTableEntry))
      .toStrictEqual("/team-management/details/member/1");
  });

  it("should navigate to correct path team is selected", () => {
    component.selectedTeam$ = new BehaviorSubject<Team | undefined>(team1);
    const userTableEntry: any = {
      id: 1
    };
    expect(component.getMemberDetailsLink(userTableEntry))
      .toStrictEqual(`/team-management/${team1.id}/details/member/1`);
  });
});
