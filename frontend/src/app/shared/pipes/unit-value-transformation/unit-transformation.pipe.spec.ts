import { UnitValueTransformationPipe } from './unit-value-transformation.pipe';
import { Unit } from '../../types/enums/Unit';

describe('UnitTransformationPipe', () => {
  it('create an instance', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe).toBeTruthy();
  });

  it('should format as Percent', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(380, Unit.PERCENT)).toBe('380%');
  });

  it('should format as Number', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(380, Unit.NUMBER)).toBe('380');
  });

  it('should format as FTE', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(380, Unit.FTE)).toBe('380');
  });

  it('should format as CHF without double value', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(380, Unit.CHF)).toBe('380.-');
  });

  it('should format as CHF as double value', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(380.987, Unit.CHF)).toBe('380.99');
  });

  it('should return with no format if unit is not preset one', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(140, 'MEMBERS')).toBe('140');
  });
});
