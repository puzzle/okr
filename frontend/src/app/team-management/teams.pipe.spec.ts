import { TeamsPipe } from './teams.pipe';

describe('TeamsPipe', () => {
  let pipe: TeamsPipe;
  const translateMock: any = {
    instant: jest.fn()
  };

  beforeEach(() => {
    pipe = new TeamsPipe(translateMock);
  });

  it('should return an empty string if teams array is empty', () => {
    expect(pipe.transform([], 2))
      .toEqual('');
  });

  it('should join all team names if maxEntries is not defined', () => {
    expect(pipe.transform(['team1',
      'team2',
      'team3'], undefined))
      .toEqual('team1, team2, team3');
  });

  it('should limit the number of teams if maxEntries is defined', () => {
    translateMock.instant.mockReturnValue('+ 1 Weitere');
    expect(pipe.transform(['team1',
      'team2',
      'team3'], 2))
      .toEqual('team1, team2, + 1 Weitere');
    expect(translateMock.instant)
      .toHaveBeenCalledWith('TEAM_MANAGEMENT.WEITERE', { overflow: 1 });
  });

  it('should join all of teams if maxEntries is defined and matched', () => {
    expect(pipe.transform(['team1',
      'team2'], 2))
      .toEqual('team1, team2');
  });
});
