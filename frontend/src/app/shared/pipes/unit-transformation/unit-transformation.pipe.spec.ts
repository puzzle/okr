import { UnitTransformationPipe } from './unit-transformation.pipe';
import { UNIT_CHF, UNIT_EUR, UNIT_FTE, UNIT_NUMBER, UNIT_PERCENT } from '../../test-data';

describe('UnitTransformationPipe', () => {
  it('should create an instance', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe)
      .toBeTruthy();
  });

  it('should format percent label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, UNIT_PERCENT))
      .toBe('1%');
  });

  it('should format FTE label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, UNIT_FTE))
      .toBe('1 ' + UNIT_FTE.unitName);
  });

  it('should format CHF label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, UNIT_CHF))
      .toBe('1 ' + UNIT_CHF.unitName);
  });

  it('should format EUR label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, UNIT_EUR))
      .toBe('1 ' + UNIT_EUR.unitName);
  });

  it('should format Number label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, UNIT_NUMBER))
      .toBe('1');
  });

  it('should format as Number with delimiter based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(38000, UNIT_NUMBER))
      .toBe('38\'000');
  });

  it('should format as Number with two digits after dot', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(380.987, UNIT_NUMBER))
      .toBe('380.99');
  });
});
