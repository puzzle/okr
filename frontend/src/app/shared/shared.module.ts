import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { ExampleDialogComponent } from './dialog/example-dialog/example-dialog.component';
import { ObjectiveFormComponent } from './dialog/objective-dialog/objective-form.component';
import { UnitValueTransformationPipe } from './pipes/unit-value-transformation/unit-value-transformation.pipe';
import { ConfirmDialogComponent } from './dialog/confirm-dialog/confirm-dialog.component';
import { ParseUnitValuePipe } from './pipes/parse-unit-value/parse-unit-value.pipe';
import { ScoringComponent } from './custom/scoring/scoring.component';
import { CompleteDialogComponent } from './dialog/complete-dialog/complete-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { OkrTangramComponent } from './custom/okr-tangram/okr-tangram.component';
import { MatButtonModule } from '@angular/material/button';
import { SidepanelComponent } from './sidepanel/sidepanel.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { CdkConnectedOverlay, CdkOverlayOrigin } from '@angular/cdk/overlay';
import { A11yModule } from '@angular/cdk/a11y';
import { RouterOutlet } from '@angular/router';
import { SpinnerComponent } from './custom/spinner/spinner.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DialogTemplateCoreComponent } from './custom/dialog-template-core/dialog-template-core.component';
import { MatDividerModule } from '@angular/material/divider';
import { UnitLabelTransformationPipe } from './pipes/unit-label-transformation/unit-label-transformation.pipe';
import { MatAutocomplete, MatAutocompleteTrigger } from '@angular/material/autocomplete';

@NgModule({
  declarations: [
    ExampleDialogComponent,
    ObjectiveFormComponent,
    UnitValueTransformationPipe,
    ConfirmDialogComponent,
    ParseUnitValuePipe,
    UnitLabelTransformationPipe,
    ScoringComponent,
    CompleteDialogComponent,
    OkrTangramComponent,
    SidepanelComponent,
    SpinnerComponent,
    DialogTemplateCoreComponent,
  ],
  imports: [
    CommonModule,
    MatDialogModule,
    MatCheckboxModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatRadioModule,
    MatIconModule,
    NgOptimizedImage,
    MatButtonModule,
    MatSidenavModule,
    CdkOverlayOrigin,
    CdkConnectedOverlay,
    CdkOverlayOrigin,
    A11yModule,
    RouterOutlet,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatAutocompleteTrigger,
    MatAutocomplete,
  ],
  exports: [
    ExampleDialogComponent,
    ObjectiveFormComponent,
    UnitValueTransformationPipe,
    ConfirmDialogComponent,
    ParseUnitValuePipe,
    UnitLabelTransformationPipe,
    ScoringComponent,
    CompleteDialogComponent,
    OkrTangramComponent,
    SidepanelComponent,
    SpinnerComponent,
    DialogTemplateCoreComponent,
  ],
})
export class SharedModule {}
