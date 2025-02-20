import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ShowEditRoleComponent } from './show-edit-role.component';
import { testUser } from '../../shared/test-data';
import { TranslateTestingModule } from 'ngx-translate-testing';
// @ts-ignore
import * as de from '../../../assets/i18n/de.json';

describe('ShowEditRoleComponent', () => {
  let component: ShowEditRoleComponent;
  let fixture: ComponentFixture<ShowEditRoleComponent>;
  const userTeam = { ...testUser.userTeamList[0] };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [ShowEditRoleComponent],
      imports: [TranslateTestingModule.withTranslations({ de: de })]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShowEditRoleComponent);
    component = fixture.componentInstance;

    component.userTeam = userTeam;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should set edit to false using saveIsAdmin', () => {
    component.edit = true;
    component.saveIsAdmin(true);
    expect(component.edit)
      .toBeFalsy();
  });

  it('should set edit to given value using setEditAsync', fakeAsync(() => {
    component.edit = false;
    const mouseEvent = {
      stopPropagation: () => undefined
    } as any;
    component.setEditAsync(mouseEvent, true);
    tick();
    expect(component.edit)
      .toBeTruthy();
  }));
});
