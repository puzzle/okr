import { UnitTransformationPipe } from './unit-transformation.pipe';
import { Unit } from '../../types/enums/Unit';

describe('UnitTransformationPipe', () => {
  it('should create an instance', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe)
      .toBeTruthy();
  });

  it('should format percent label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.PERCENT))
      .toBe('1%');
  });

  it('should format FTE label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.FTE))
      .toBe('1 ' + Unit.FTE);
  });

  it('should format CHF label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.CHF))
      .toBe('1 ' + Unit.CHF);
  });

  it('should format EUR label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.EUR))
      .toBe('1 ' + Unit.EUR);
  });

  it('should format Number label based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(1, Unit.NUMBER))
      .toBe('1');
  });

  it('should format as Number with delimiter based on value', () => {
    const pipe = new UnitTransformationPipe();
    expect(pipe.transform(38000, Unit.NUMBER))
      .toBe("38'000");
  });

  it('should format as Number with two digits after dot', () => {
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
