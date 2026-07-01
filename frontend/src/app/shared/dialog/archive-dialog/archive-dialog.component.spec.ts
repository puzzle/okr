import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ArchiveTeamDialogComponent } from './archive-dialog.component';
import { MatDialogRef } from '@angular/material/dialog';
import { QuarterService } from '../../../services/quarter.service';
import { of } from 'rxjs';
import { quarter1, quarter2, quarterList } from '../../test-data';

describe('ArchiveTeamDialogComponent', () => {
  let component: ArchiveTeamDialogComponent;
  let fixture: ComponentFixture<ArchiveTeamDialogComponent>;

  let mockQuarterService: { getAllQuarters: jest.Mock;
    getCurrentQuarter: jest.Mock; };
  let mockDialogRef: { close: jest.Mock };

  beforeEach(async() => {
    mockQuarterService = {
      getAllQuarters: jest.fn()
        .mockReturnValue(of(quarterList)),
      getCurrentQuarter: jest.fn()
        .mockReturnValue(of(quarter1))
    };

    mockDialogRef = {
      close: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [ArchiveTeamDialogComponent],
      providers: [{ provide: QuarterService,
        useValue: mockQuarterService },
      { provide: MatDialogRef,
        useValue: mockDialogRef }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ArchiveTeamDialogComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should load available quarters and set the current quarter as selected', () => {
      fixture.detectChanges();

      expect(component.availableQuarters())
        .toEqual(quarterList);

      expect(component.selectedQuarter())
        .toEqual(quarter1);
      expect(mockQuarterService.getAllQuarters)
        .toHaveBeenCalledTimes(1);
      expect(mockQuarterService.getCurrentQuarter)
        .toHaveBeenCalledTimes(1);
    });
  });

  describe('compareById', () => {
    it('should return true if both quarters have the same id', () => {
      expect(component.compareById(quarter1, quarter1))
        .toBe(true);
    });

    it('should return false if quarters have different ids', () => {
      expect(component.compareById(quarter1, quarter2))
        .toBe(false);
    });

    it('should return true if both are null or undefined', () => {
      expect(component.compareById(null as any, null as any))
        .toBe(true);
      expect(component.compareById(undefined as any, undefined as any))
        .toBe(true);
    });

    it('should return false if one is null and the other is defined', () => {
      expect(component.compareById(quarter1, null as any))
        .toBe(false);
    });
  });

  describe('onSave', () => {
    it('should close the dialog and pass the selected quarter', () => {
      fixture.detectChanges();

      component.onSave();

      expect(mockDialogRef.close)
        .toHaveBeenCalledTimes(1);
      expect(mockDialogRef.close)
        .toHaveBeenCalledWith(quarter1);
    });
  });
});
