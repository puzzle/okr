import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [AppComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
  });

  test('should create the app', () => {
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  test('should render OKRs navigation item', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('pzsh-nav-item')?.textContent).toContain(
      'OKRs'
    );
  });
});
