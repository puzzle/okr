import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpBackend, HttpClient, HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule } from '@angular/material/dialog';
import { ToastrModule } from 'ngx-toastr';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { A11yModule } from '@angular/cdk/a11y';
import { ExampleDialogComponent } from './shared/dialog/example-dialog/example-dialog.component';
import { MatRadioModule } from '@angular/material/radio';
import { ConfigService } from './config.service';
import { config, firstValueFrom, map } from 'rxjs';
import { environment } from '../environments/environment';
import { OauthInterceptor } from './shared/interceptors/oauth.interceptor';
import { ApplicationTopBarComponent } from './application-top-bar/application-top-bar.component';
import { TeamComponent } from './team/team.component';
import { OverviewComponent } from './overview/overview.component';
import { ObjectiveComponent } from './objective/objective.component';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { KeyresultComponent } from './keyresult/keyresult.component';
import { KeyresultDetailComponent } from './keyresult-detail/keyresult-detail.component';
import { ObjectiveDetailComponent } from './objective-detail/objective-detail.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { ConfidenceComponent } from './confidence/confidence.component';
import { MatSliderModule } from '@angular/material/slider';
import { ErrorInterceptor } from './shared/interceptors/error-interceptor.service';
import { CheckInHistoryDialogComponent } from './shared/dialog/check-in-history-dialog/check-in-history-dialog.component';
import { MatDividerModule } from '@angular/material/divider';
import { ObjectiveFormComponent } from './shared/dialog/objective-dialog/objective-form.component';
import { ApplicationBannerComponent } from './application-banner/application-banner.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { CheckInFormMetricComponent } from './shared/dialog/checkin/check-in-form-metric/check-in-form-metric.component';
import { UnitValueTransformationPipe } from './shared/pipes/unit-value-transformation/unit-value-transformation.pipe';
import { CheckInFormOrdinalComponent } from './shared/dialog/checkin/check-in-form-ordinal/check-in-form-ordinal.component';
import { ConfirmDialogComponent } from './shared/dialog/confirm-dialog/confirm-dialog.component';
import { CheckInFormComponent } from './shared/dialog/checkin/check-in-form/check-in-form.component';
import { UnitLabelTransformationPipe } from './shared/pipes/unit-label-transformation/unit-label-transformation.pipe';
import { ParseUnitValuePipe } from './shared/pipes/parse-unit-value/parse-unit-value.pipe';
import { SidepanelComponent } from './shared/custom/sidepanel/sidepanel.component';
import { CdkConnectedOverlay, CdkOverlayOrigin, OverlayModule } from '@angular/cdk/overlay';
import { ScoringComponent } from './shared/custom/scoring/scoring.component';
import { CompleteDialogComponent } from './shared/dialog/complete-dialog/complete-dialog.component';
import { QuarterFilterComponent } from './quarter-filter/quarter-filter.component';
import { KeyResultFormComponent } from './shared/dialog/key-result-form/key-result-form.component';
import { TeamFilterComponent } from './team-filter/team-filter.component';
import { MatChipsModule } from '@angular/material/chips';
import { Router } from '@angular/router';
import { CustomRouter } from './shared/customRouter';
import { KeyresultTypeComponent } from './keyresult-type/keyresult-type.component';
import { DialogHeaderComponent } from './shared/custom/dialog-header/dialog-header.component';
import { ObjectiveFilterComponent } from './objective-filter/objective-filter.component';
import { ActionPlanComponent } from './action-plan/action-plan.component';
import { CdkDrag, CdkDragHandle, CdkDropList } from '@angular/cdk/drag-drop';
import { TeamManagementComponent } from './shared/dialog/team-management/team-management.component';
import { KeyresultDialogComponent } from './shared/dialog/keyresult-dialog/keyresult-dialog.component';
import { CustomizationService } from './shared/services/customization.service';
import {
  AuthModule,
  DefaultLocalStorageService,
  AbstractSecurityStorage,
  StsConfigLoader,
  StsConfigHttpLoader,
  AuthInterceptor,
} from 'angular-auth-oidc-client';
import { CallbackComponent } from './callback/callback.component';

function initOauthFactory(configService: ConfigService) {
  const config$ = configService.config$.pipe(
    map((config) => {
      return { ...environment.oauth, authority: config.issuer, clientId: config.clientId };
    }),
  );
  return new StsConfigHttpLoader(config$);
}

export function createTranslateLoader(http: HttpBackend) {
  return new TranslateHttpLoader(new HttpClient(http), './assets/i18n/', '.json');
}

export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'DD.MM.YYYY',
    monthYearLabel: 'DD.MM.YYYY',
    dateA11yLabel: 'DD.MM.YYYY',
    monthYearA11yLabel: 'DD.MM.YYYY',
  },
};

@NgModule({
  declarations: [
    AppComponent,
    ExampleDialogComponent,
    TeamComponent,
    OverviewComponent,
    ObjectiveComponent,
    KeyresultComponent,
    ApplicationTopBarComponent,
    ConfidenceComponent,
    CheckInHistoryDialogComponent,
    ScoringComponent,
    KeyresultDetailComponent,
    ObjectiveDetailComponent,
    ApplicationBannerComponent,
    KeyResultFormComponent,
    ConfirmDialogComponent,
    CheckInFormComponent,
    CheckInFormMetricComponent,
    UnitValueTransformationPipe,
    CheckInFormOrdinalComponent,
    UnitLabelTransformationPipe,
    ParseUnitValuePipe,
    ObjectiveFormComponent,
    CompleteDialogComponent,
    QuarterFilterComponent,
    SidepanelComponent,
    TeamFilterComponent,
    KeyresultTypeComponent,
    DialogHeaderComponent,
    ObjectiveFilterComponent,
    ActionPlanComponent,
    TeamManagementComponent,
    KeyresultDialogComponent,
    CallbackComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AuthModule.forRoot({
      loader: {
        provide: StsConfigLoader,
        useFactory: initOauthFactory,
        deps: [ConfigService],
      },
    }),
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    MatProgressBarModule,
    MatDialogModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatExpansionModule,
    MatInputModule,
    MatTooltipModule,
    MatAutocompleteModule,
    OverlayModule,
    ToastrModule.forRoot(),
    MatProgressSpinnerModule,
    TranslateModule.forRoot({
      defaultLanguage: 'de',
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpBackend],
      },
    }),
    A11yModule,
    MatRadioModule,
    NgOptimizedImage,
    MatSidenavModule,
    MatSliderModule,
    FormsModule,
    MatDividerModule,
    MatSidenavModule,
    MatCheckboxModule,
    CdkOverlayOrigin,
    CdkConnectedOverlay,
    CdkOverlayOrigin,
    MatChipsModule,
    CdkDropList,
    CdkDrag,
    CdkDragHandle,
  ],
  providers: [
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE],
    },
    { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS },
    { provide: HTTP_INTERCEPTORS, useClass: OauthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    {
      provide: AbstractSecurityStorage,
      useClass: DefaultLocalStorageService,
    },
    {
      provide: Router,
      useClass: CustomRouter,
    },
    UnitValueTransformationPipe,
    ParseUnitValuePipe,
    TranslateService,
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {
  constructor(customizationService: CustomizationService) {}
}
