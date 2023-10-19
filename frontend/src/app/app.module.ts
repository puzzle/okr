import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
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
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { OAuthModule, OAuthService, OAuthStorage } from 'angular-oauth2-oidc';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { A11yModule } from '@angular/cdk/a11y';
import { ExampleDialogComponent } from './shared/dialog/example-dialog/example-dialog.component';
import { MatRadioModule } from '@angular/material/radio';
import { ConfigService } from './config.service';
import { firstValueFrom } from 'rxjs';
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
import { CheckInBaseInformationsComponent } from './shared/dialog/checkin/check-in-base-informations/check-in-base-informations.component';
import { CustomInputComponent } from './shared/custom/custom-input/custom-input.component';
import { KeyResultDialogComponent } from './key-result-dialog/key-result-dialog.component';
import { ConfirmDialogComponent } from './shared/dialog/confirm-dialog/confirm-dialog.component';
import { CheckInFormComponent } from './shared/dialog/checkin/check-in-form/check-in-form.component';
import { UnitLabelTransformationPipe } from './shared/pipes/unit-label-transformation/unit-label-transformation.pipe';
import { ParseUnitValuePipe } from './shared/pipes/parse-unit-value/parse-unit-value.pipe';
import { SidepanelComponent } from './shared/custom/sidepanel/sidepanel.component';
import { CdkConnectedOverlay, CdkOverlayOrigin, OverlayModule } from '@angular/cdk/overlay';
import { ScoringComponent } from './shared/custom/scoring/scoring.component';
import { CompleteDialogComponent } from './shared/dialog/complete-dialog/complete-dialog.component';
import { QuarterFilterComponent } from './quarter-filter/quarter-filter.component';

function initOauthFactory(configService: ConfigService, oauthService: OAuthService) {
  return async () => {
    const config = await firstValueFrom(configService.config$);
    oauthService.configure({ ...environment.oauth, issuer: config.issuer, clientId: config.clientId });
  };
}

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export function storageFactory(): OAuthStorage {
  return localStorage;
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
    KeyResultDialogComponent,
    ConfirmDialogComponent,
    CheckInFormComponent,
    CheckInFormMetricComponent,
    UnitValueTransformationPipe,
    CheckInFormOrdinalComponent,
    CheckInBaseInformationsComponent,
    CustomInputComponent,
    CheckInFormComponent,
    UnitLabelTransformationPipe,
    ConfirmDialogComponent,
    ParseUnitValuePipe,
    ObjectiveFormComponent,
    CompleteDialogComponent,
    QuarterFilterComponent,
    SidepanelComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
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
        deps: [HttpClient],
      },
    }),
    OAuthModule.forRoot(),
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
    { provide: OAuthStorage, useFactory: storageFactory },
    { provide: APP_INITIALIZER, useFactory: initOauthFactory, deps: [ConfigService, OAuthService], multi: true },
    UnitValueTransformationPipe,
    ParseUnitValuePipe,
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}
