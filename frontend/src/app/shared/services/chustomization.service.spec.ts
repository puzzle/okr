import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CustomizationService } from './customization.service';
import { DOCUMENT } from '@angular/common';
import { ConfigService } from '../../config.service';
import { Observable, of } from 'rxjs';

describe('CustomizationService', () => {
  let service: CustomizationService;

  const body = {
    title: 'title',
    favicon: 'favicon',
    logo: 'logo',
    customStyles: { cssVar1: 'foo' },
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: DOCUMENT, useValue: undefined },
        {
          provide: ConfigService,
          useValue: {
            config$: of(body),
          },
        },
      ],
    });
    service = TestBed.inject(CustomizationService);
  });

  it('should be created', () => {
    const currentConfig = service.getCurrentConfig();
    expect(currentConfig?.title).toBe(body.title);
    expect(currentConfig?.logo).toBe(body.logo);
    expect(currentConfig?.favicon).toBe(body.favicon);
    expect(currentConfig?.customStyles['cssVar1']).toBe(body.customStyles['cssVar1']);
  });
});
