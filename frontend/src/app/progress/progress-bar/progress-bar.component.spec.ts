import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressBarComponent } from './progress-bar.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { SvgService } from '../../shared/services/svg/svg.service';

describe('ProgressBarComponent', () => {
  let component: ProgressBarComponent;
  let fixture: ComponentFixture<ProgressBarComponent>;

  const mockSvgService = {
    getSvg: jest.fn(),
  };

  beforeEach(async () => {
    mockSvgService.getSvg.mockReturnValue(of());

    await TestBed.configureTestingModule({
      providers: [{ provide: SvgService, useValue: mockSvgService }],
      declarations: [ProgressBarComponent],
      imports: [HttpClientTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(ProgressBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    mockSvgService.getSvg.mockClear();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should addCards', () => {
    component.paintProgressBar();
    expect(mockSvgService.getSvg).toHaveBeenCalledTimes(1);
  });
});
