import { ComponentFixture, TestBed } from "@angular/core/testing";
import { TeamRoleDropdownComponent } from "./team-role-dropdown.component";
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { MatOptionModule } from "@angular/material/core";
import { TranslateModule } from "@ngx-translate/core";
import { takeLast } from "rxjs";

describe("TeamRoleDropdownComponent",
  () => {
    let component: TeamRoleDropdownComponent;
    let fixture: ComponentFixture<TeamRoleDropdownComponent>;

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          CommonModule,
          FormsModule,
          MatOptionModule,
          TranslateModule.forRoot()
        ],
        declarations: [TeamRoleDropdownComponent]
      })
        .compileComponents();
    });

    beforeEach(() => {
      fixture = TestBed.createComponent(TeamRoleDropdownComponent);
      component = fixture.componentInstance;
    });

    it("should create",
      () => {
        expect(component)
          .toBeTruthy();
      });

    it("component onInit should create formControl",
      () => {
        component.isAdmin = false;
        component.ngOnInit();
        expect(JSON.stringify(component.adminControl))
          .toStrictEqual(JSON.stringify(new FormControl(component.isAdmin,
            [Validators.required])));
      });

    it("triggerIsAdminChange should submit next value",
      (done) => {
        component.isAdminChange.pipe(takeLast(1))
          .subscribe((val) => {
            expect(val)
              .toBeFalsy();
            done();
          });
        component.isAdminChange.next(false);
        component.triggerIsAdminChange();
        component.isAdminChange.complete();
      });
  });
