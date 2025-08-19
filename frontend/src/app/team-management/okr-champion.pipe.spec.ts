import { OkrChampionPipe } from './okr-champion.pipe';
import { Injector } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

describe('OkrChampionPipe', () => {
  const translateMock = {
    instant: () => 'Ja'
  } as any;

  it('should create an instance', () => {
    const pipe = Injector.create({ providers: [{ provide: OkrChampionPipe },
      { provide: TranslateService,
        useValue: translateMock }] })
      .get(OkrChampionPipe);
    expect(pipe)
      .toBeTruthy();
  });

  it('should display "Ja" if user is okrChampion', () => {
    const pipe = Injector.create({ providers: [{ provide: OkrChampionPipe },
      { provide: TranslateService,
        useValue: translateMock }] })
      .get(OkrChampionPipe);
    expect(pipe.transform(true))
      .toBe('Ja');
  });

  it('should display "-" if user is not okrChampion', () => {
    const pipe = Injector.create({ providers: [{ provide: OkrChampionPipe },
      { provide: TranslateService,
        useValue: translateMock }] })
      .get(OkrChampionPipe);
    expect(pipe.transform(false))
      .toBe('-');
  });
});
