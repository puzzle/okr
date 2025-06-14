import { CUSTOM_ELEMENTS_SCHEMA, NgModule, inject, provideAppInitializer } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {
  HTTP_INTERCEPTORS,
  HttpBackend,
  HttpClient,
  provideHttpClient,
  withInterceptorsFromDi
} from '@angular/common/http';
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
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { OAuthModule, OAuthService, OAuthStorage } from 'angular-oauth2-oidc';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatRadioModule } from '@angular/material/radio';
import { ConfigService } from './services/config.service';
import { firstValueFrom } from 'rxjs';
import { environment } from '../environments/environment';
import { TeamComponent } from './components/team/team.component';
import { OverviewComponent } from './components/overview/overview.component';
import { ObjectiveComponent } from './components/objective/objective.component';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { KeyResultComponent } from './components/key-result/key-result.component';
import { KeyResultDetailComponent } from './components/key-result-detail/key-result-detail.component';
import { ObjectiveDetailComponent } from './components/objective-detail/objective-detail.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { ConfidenceComponent } from './components/confidence/confidence.component';
import { MatSliderModule } from '@angular/material/slider';
import { MatDividerModule } from '@angular/material/divider';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { OverlayModule } from '@angular/cdk/overlay';
import { MatChipsModule } from '@angular/material/chips';
import { Router } from '@angular/router';
import { KeyResultTypeComponent } from './components/key-result-type/key-result-type.component';
import { ObjectiveFilterComponent } from './components/objective-filter/objective-filter.component';
import { ActionPlanComponent } from './components/action-plan/action-plan.component';
import { CdkDrag, CdkDragHandle, CdkDropList } from '@angular/cdk/drag-drop';
import { SharedModule } from './shared/shared.module';
import { OAuthInterceptor } from './interceptors/o-auth.interceptor';
import { ErrorInterceptor } from './interceptors/error.interceptor';
import { CustomRouter } from './shared/custom-router';
import { KeyResultFormComponent } from './components/key-result-form/key-result-form.component';
import { KeyResultDialogComponent } from './components/key-result-dialog/key-result-dialog.component';
import { CheckInHistoryDialogComponent } from './components/check-in-history-dialog/check-in-history-dialog.component';
import { CheckInFormMetricComponent } from './components/checkin/check-in-form-metric/check-in-form-metric.component';
import { CheckInFormOrdinalComponent } from './components/checkin/check-in-form-ordinal/check-in-form-ordinal.component';
import { CheckInFormComponent } from './components/checkin/check-in-form/check-in-form.component';
import { ApplicationTopBarComponent } from './components/application-top-bar/application-top-bar.component';
import { A11yModule } from '@angular/cdk/a11y';
import { CustomizationService } from './services/customization.service';
import { ManageUnitsDialogComponent } from './components/manage-units-dialog/manage-units-dialog.component';
import { UnitTransformationPipe } from './shared/pipes/unit-transformation/unit-transformation.pipe';
import { OverviewBannerComponent } from './components/overview-banner/overview-banner.component';

function initOauthFactory(configService: ConfigService, oauthService: OAuthService) {
  return async() => {
    const config = await firstValueFrom(configService.config$);
    oauthService.configure({
      ...environment.oauth,
      issuer: config.issuer,
      clientId: config.clientId
    });
  };
}

export function createTranslateLoader(http: HttpBackend) {
  return new TranslateHttpLoader(new HttpClient(http), './assets/i18n/', '.json');
}

export function storageFactory(): OAuthStorage {
  return localStorage;
}

export const MY_FORMATS = {
  parse: {
    dateInput: 'LL'
  },
  display: {
    dateInput: 'DD.MM.YYYY',
    monthYearLabel: 'DD.MM.YYYY',
    dateA11yLabel: 'DD.MM.YYYY',
    monthYearA11yLabel: 'DD.MM.YYYY'
  }
};

@NgModule({
  declarations: [
    AppComponent,
    TeamComponent,
    OverviewComponent,
    ObjectiveComponent,
    KeyResultComponent,
    ApplicationTopBarComponent,
    ConfidenceComponent,
    KeyResultDetailComponent,
    ObjectiveDetailComponent,
    KeyResultTypeComponent,
    ObjectiveFilterComponent,
    ActionPlanComponent,
    KeyResultFormComponent,
    KeyResultDialogComponent,
    CheckInHistoryDialogComponent,
    CheckInFormMetricComponent,
    CheckInFormOrdinalComponent,
    CheckInFormComponent,
    ManageUnitsDialogComponent,
    OverviewBannerComponent
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
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
        deps: [HttpBackend]
      }
    }),
    OAuthModule.forRoot(),
    MatRadioModule,
    NgOptimizedImage,
    MatSidenavModule,
    MatSliderModule,
    FormsModule,
    MatDividerModule,
    MatSidenavModule,
    MatCheckboxModule,
    MatChipsModule,
    CdkDropList,
    CdkDrag,
    A11yModule,
    CdkDragHandle,
    SharedModule
  ],
  exports: [ObjectiveFilterComponent],
  providers: [
    {
      provide: MAT_DATE_FORMATS,
      useValue: MY_FORMATS
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: OAuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    },
    {
      provide: OAuthStorage,
      useFactory: storageFactory
    },
    provideAppInitializer(() => {
      const initializerFn = initOauthFactory(inject(ConfigService), inject(OAuthService));
      return initializerFn();
    }),
    {
      provide: Router,
      useClass: CustomRouter
    },
    TranslateService,
    UnitTransformationPipe,
    provideHttpClient(withInterceptorsFromDi())
  ]
})
export class AppModule {
  constructor(private readonly customizationService: CustomizationService) {}
}
