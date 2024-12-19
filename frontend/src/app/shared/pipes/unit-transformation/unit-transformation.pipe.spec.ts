import { UnitTransformationPipe } from './unit-transformation.pipe';
import { Unit } from '../../types/enums/Unit';

describe('UnitTransformationPipe', () => {
  it('create an instance', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe)
      .toBeTruthy();
  });

  it('Format Percent label', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.PERCENT))
      .toBe('1%');
  });

  it('Format FTE label', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.FTE))
      .toBe('1 ' + Unit.FTE);
  });

  it('Format CHF label', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.CHF))
      .toBe('1 ' + Unit.CHF);
  });

  it('Format EUR label', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.EUR))
      .toBe('1 ' + Unit.EUR);
  });

  it('Format Number label', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.NUMBER))
      .toBe('1');
  });

  it('should format as Percent', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(38, Unit.PERCENT))
      .toBe('38%');
  });

  it('should format as Number', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(38000, Unit.NUMBER))
      .toBe("38'000");
  });

  it('should format as CHF as double value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(380.987, Unit.NUMBER))
      .toBe('380.99');
  });

  it('should return with no format if unit is not known', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(134, 'Some Unit'))
      .toBe('134');
  });
});
