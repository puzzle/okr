import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [AppComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create the app', () => {
    expect(component).toBeTruthy();
  });

  test('should preventLinkAction return always false', () => {
    expect(component.preventLinkAction()).toBeFalsy();
  });

  test('should render OKRs navigation item', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('pzsh-nav-item')?.textContent).toContain(
      'OKRs'
    );
  });

  describe('isOverview + isTeam', () => {
    test("should handle '' url ", () => {
      component.currentUrl = '';
      expect(component.isOverview()).toEqual(true);
      expect(component.isTeam()).toEqual(null);
    });

    test("should handle '/' url ", () => {
      component.currentUrl = '/';
      expect(component.isOverview()).toEqual(true);
      expect(component.isTeam()).toEqual(null);
    });

    test("should handle '/objectives/new' url ", () => {
      component.currentUrl = '/objectives/new';
      expect(component.isOverview()).toEqual(true);
      expect(component.isTeam()).toEqual(null);
    });

    test("should handle '/teams' url ", () => {
      component.currentUrl = '/teams';
      expect(component.isOverview()).toEqual(null);
      expect(component.isTeam()).toEqual(true);
    });

    test("should handle '/team' url ", () => {
      component.currentUrl = '/team';
      expect(component.isOverview()).toEqual(null);
      expect(component.isTeam()).toEqual(true);
    });
  });

  describe('convertFalseToNull', () => {
    test('should convert true value to true', () => {
      expect(component.convertFalseToNull(true)).toEqual(true);
    });
    test('should convert false value to null', () => {
      expect(component.convertFalseToNull(false)).toEqual(null);
    });
  });
});
