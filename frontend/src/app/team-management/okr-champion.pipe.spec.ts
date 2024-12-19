import { OkrChampionPipe } from './okr-champion.pipe';

describe('OkrChampionPipe', () => {
  const translateMock = {
    instant: () => 'Ja'
  } as any;

  it('create an instance', () => {
    const pipe = new OkrChampionPipe(translateMock);
    expect(pipe)
      .toBeTruthy();
  });

  it('should display "Ja" if user is okrChampion', () => {
    const pipe = new OkrChampionPipe(translateMock);
    expect(pipe.transform(true))
      .toBe('Ja');
  });

  it('should display "-" if user is not okrChampion', () => {
    const pipe = new OkrChampionPipe(translateMock);
    expect(pipe.transform(false))
      .toBe('-');
  });
});
