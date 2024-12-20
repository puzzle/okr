import { ComponentFixture, TestBed } from "@angular/core/testing";

import { CheckInHistoryDialogComponent } from "./check-in-history-dialog.component";
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { provideHttpClientTesting } from "@angular/common/http/testing";
import { checkInMetric, checkInMetricWriteableFalse, keyResult } from "../../shared/testData";
import { By } from "@angular/platform-browser";
import { DialogService } from "../../services/dialog.service";
import { TranslateModule, TranslateService } from "@ngx-translate/core";
import { MatIconModule } from "@angular/material/icon";
import { SpinnerComponent } from "../../shared/custom/spinner/spinner.component";
import { MatProgressSpinner } from "@angular/material/progress-spinner";
import { provideRouter } from "@angular/router";
import { provideHttpClient } from "@angular/common/http";
import { DialogTemplateCoreComponent } from "../../shared/custom/dialog-template-core/dialog-template-core.component";
import { MatDividerModule } from "@angular/material/divider";

const checkInService = {
  getAllCheckInOfKeyResult: jest.fn()
};

describe("CheckInHistoryDialogComponent", () => {
  let component: CheckInHistoryDialogComponent;
  let fixture: ComponentFixture<CheckInHistoryDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CheckInHistoryDialogComponent,
        DialogTemplateCoreComponent,
        SpinnerComponent],

      imports: [
        TranslateModule.forRoot(),
        MatIconModule,
        MatProgressSpinner,
        MatDividerModule,
        MatDialogModule
      ],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        TranslateService,
        DialogService,
        { provide: MAT_DIALOG_DATA,
          useValue: { keyResult: keyResult } },
        { provide: MatDialogRef,
          useValue: {} }
      ]
    });
    jest
      .spyOn(checkInService, "getAllCheckInOfKeyResult")
      .mockReturnValue([checkInMetric,
        checkInMetricWriteableFalse]);
    fixture = TestBed.createComponent(CheckInHistoryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component)
      .toBeTruthy();
  });

  it.skip("should not display edit check-in button if writeable is false", async() => {
    const buttons = fixture.debugElement.queryAll(By.css("button"));
    expect(buttons.length)
      .toBe(1);
  });
});
