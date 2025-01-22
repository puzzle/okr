import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { ObjectiveFormComponent } from './dialog/objective-dialog/objective-form.component';
import { ConfirmDialogComponent } from './dialog/confirm-dialog/confirm-dialog.component';
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
import { SidePanelComponent } from './side-panel/side-panel.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { CdkConnectedOverlay, CdkOverlayOrigin } from '@angular/cdk/overlay';
import { A11yModule } from '@angular/cdk/a11y';
import { RouterOutlet } from '@angular/router';
import { SpinnerComponent } from './custom/spinner/spinner.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DialogTemplateCoreComponent } from './custom/dialog-template-core/dialog-template-core.component';
import { MatDividerModule } from '@angular/material/divider';
import { UnitTransformationPipe } from './pipes/unit-transformation/unit-transformation.pipe';
import { MatTooltip } from '@angular/material/tooltip';
import { ErrorComponent } from './custom/error/error.component';

@NgModule({
  declarations: [
    ObjectiveFormComponent,
    ConfirmDialogComponent,
    UnitTransformationPipe,
    ScoringComponent,
    CompleteDialogComponent,
    OkrTangramComponent,
    SidePanelComponent,
    SpinnerComponent,
    DialogTemplateCoreComponent,
    ErrorComponent
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
    MatTooltip
  ],
  exports: [
    ObjectiveFormComponent,
    ConfirmDialogComponent,
    ScoringComponent,
    CompleteDialogComponent,
    OkrTangramComponent,
    UnitTransformationPipe,
    SidePanelComponent,
    SpinnerComponent,
    DialogTemplateCoreComponent,
    ErrorComponent

  ]
})
export class SharedModule {}
