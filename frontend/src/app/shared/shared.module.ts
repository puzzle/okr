import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { ExampleDialogComponent } from './dialog/example-dialog/example-dialog.component';
import { ObjectiveFormComponent } from './dialog/objective-dialog/objective-form.component';
import { UnitValueTransformationPipe } from './pipes/unit-value-transformation/unit-value-transformation.pipe';
import { ConfirmDialogComponent } from './dialog/confirm-dialog/confirm-dialog.component';
import { UnitLabelTransformationPipe } from './pipes/unit-label-transformation/unit-label-transformation.pipe';
import { ParseUnitValuePipe } from './pipes/parse-unit-value/parse-unit-value.pipe';
import { ScoringComponent } from './custom/scoring/scoring.component';
import { CompleteDialogComponent } from './dialog/complete-dialog/complete-dialog.component';
import { DialogHeaderComponent } from './custom/dialog-header/dialog-header.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { OkrTangramComponent } from './custom/okr-tangram/okr-tangram.component';

@NgModule({
  declarations: [
    ExampleDialogComponent,
    ObjectiveFormComponent,
    UnitValueTransformationPipe,
    ConfirmDialogComponent,
    UnitLabelTransformationPipe,
    ParseUnitValuePipe,
    ScoringComponent,
    CompleteDialogComponent,
    DialogHeaderComponent,
    OkrTangramComponent,
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
  ],
  exports: [
    ExampleDialogComponent,
    ObjectiveFormComponent,
    UnitValueTransformationPipe,
    ConfirmDialogComponent,
    UnitLabelTransformationPipe,
    ParseUnitValuePipe,
    ScoringComponent,
    CompleteDialogComponent,
    DialogHeaderComponent,
    OkrTangramComponent,
  ],
})
export class SharedModule {}
