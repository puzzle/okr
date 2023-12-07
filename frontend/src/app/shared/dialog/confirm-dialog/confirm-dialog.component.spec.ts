import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDialogComponent } from './confirm-dialog.component';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatButtonHarness } from '@angular/material/button/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { ReactiveFormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

const dialogMock = {
  close: jest.fn(),
};

describe('ConfirmDialogComponent', () => {
  let component: ConfirmDialogComponent;
  let fixture: ComponentFixture<ConfirmDialogComponent>;
  let loader: HarnessLoader;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        NoopAnimationsModule,
        MatSelectModule,
        MatInputModule,
        MatRadioModule,
        ReactiveFormsModule,
        TranslateModule.forRoot(),
      ],
      declarations: [ConfirmDialogComponent],
      providers: [
        TranslateService,
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: dialogMock },
      ],
    });
    fixture = TestBed.createComponent(ConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loader = TestbedHarnessEnvironment.loader(fixture);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call close method with parameter: true if clicked to submit button', async () => {
    let buttons = await loader.getAllHarnesses(MatButtonHarness);
    const submitButton = buttons[0];
    await submitButton.click();

    expect(dialogMock.close).toHaveBeenCalledWith(true);
  });

  it('should call close method with parameter: "" if clicked to cancel button', async () => {
    let buttons = await loader.getAllHarnesses(MatButtonHarness);
    const cancelButton = buttons[1];
    await cancelButton.click();

    expect(dialogMock.close).toHaveBeenCalledWith('');
  });
});
