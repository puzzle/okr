import { TestBed } from '@angular/core/testing';

import { DialogService } from './dialog.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TranslateCompiler, TranslateModule, TranslateService } from '@ngx-translate/core';
import { TranslateTestingModule } from 'ngx-translate-testing';

describe('DialogService', () => {
  let service: DialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [TranslateModule.forRoot()], providers: [TranslateService] });
    service = TestBed.inject(DialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
