import { ComponentFixture, TestBed } from "@angular/core/testing";
import { UserService } from "../../services/user.service";
import { TeamService } from "../../services/team.service";
import { AddMemberToTeamDialogComponent } from "./add-member-to-team-dialog.component";
import { SharedModule } from "../../shared/shared.module";
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { team1, users } from "../../shared/testData";
import { BehaviorSubject, of, skip } from "rxjs";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { User } from "../../shared/types/model/User";
import { MatTable } from "@angular/material/table";

describe("AddMemberToTeamDialogComponent",
  () => {
    let component: AddMemberToTeamDialogComponent;
    let fixture: ComponentFixture<AddMemberToTeamDialogComponent>;

    const userServiceMock = {
      getUsers: jest.fn(),
      reloadUsers: jest.fn()
    };

    const teamServiceMock = {
      addUsersToTeam: jest.fn()
    };

    const matDialogRefMock = {};

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [AddMemberToTeamDialogComponent],
        imports: [
          SharedModule,
          MatDialogModule,
          MatFormFieldModule,
          MatAutocompleteModule
        ],
        providers: [
          { provide: UserService,
            useValue: userServiceMock },
          { provide: TeamService,
            useValue: teamServiceMock },
          {
            provide: MatDialogRef,
            useValue: matDialogRefMock
          },
          {
            provide: MAT_DIALOG_DATA,
            useValue: { team: team1,
              currentUsersOfTeam: [users[0]] }
          }
        ]
      })
        .compileComponents();

      userServiceMock.getUsers.mockReturnValue(of(users));

      fixture = TestBed.createComponent(AddMemberToTeamDialogComponent);
      component = fixture.componentInstance;

      component.table = {
        renderRows: () => undefined
      } as MatTable<User[]>;

      component.selectedUsers$ = new BehaviorSubject<User[]>([]);
    });

    it("should create",
      () => {
        expect(component)
          .toBeTruthy();
      });

    it("should set allPossibleUsers correctly",
      () => {
        component.ngOnInit();
        component.usersForSelection$!.subscribe((filteredUsers) => {
          expect(filteredUsers.length)
            .toBe(users.length - 1);
          expect(filteredUsers).not.toContain(users[0]);
        });
      });

    it("should set filteredUsers correctly: search by PaCo",
      (done) => {
        component.search = {
          valueChanges: of("PaCo")
        } as any;

        component.ngOnInit();

        component.usersForSelection$!.subscribe((filteredUsers) => {
          expect(filteredUsers.length)
            .toBe(1);
          expect(filteredUsers[0].email)
            .toBe("peggimann@puzzle.ch");
          done();
        });
      });

    it("should set filteredUsers correctly: dont show already selected users",
      (done) => {
        component.search = {
          valueChanges: of("puzzle.ch")
        } as any;

        component.selectedUsers$.next([users[1]]);

        component.ngOnInit();

        component.usersForSelection$!.pipe(skip(1))
          .subscribe((filteredUsers) => {
            expect(filteredUsers.length)
              .toBe(users.length - 2);
            expect(filteredUsers.map((u) => u.id)).not.toContain(users[1].id);
            done();
          });
      });

    it("should set teamname correctly",
      () => {
        expect(component.getDialogTitle())
          .toBe(`Members zu Team ${team1.name} hinzufügen`);
      });

    it("should return correct display value",
      () => {
        expect(component.getDisplayValue(users[0]))
          .toBe(`${users[0].firstname} ${users[0].lastname} (${users[0].email})`);
      });

    it("should add user to selected users and restore search value",
      () => {
        component.search.setValue("test");
        component.selectedUsers$.next([users[1],
          users[2]]);
        component.selectUser(users[3]);
        expect(component.search.value)
          .toBe("");
        expect(component.selectedUsers$.getValue().length)
          .toBe(3);
        expect(component.selectedUsers$.getValue()
          .map((u) => u.id))
          .toStrictEqual([users[1].id,
            users[2].id,
            users[3].id]);
      });

    it("should remove user from selected users",
      () => {
        component.selectedUsers$.next([...users]);
        component.remove(users[0]);
        expect(component.selectedUsers$.getValue().length)
          .toBe(users.length - 1);
        expect(component.selectedUsers$.getValue()
          .map((u) => u.id)).not.toContain(users[0].id);
      });
  });
