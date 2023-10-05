import { UnitLabelTransformationPipe } from './unit-label-transformation.pipe';
import { Unit } from '../../types/enums/Unit';

describe('UnitLabelTransformationPipe', () => {
  it('create an instance', () => {
    const pipe = new UnitLabelTransformationPipe();
    expect(pipe).toBeTruthy();
  });

  it('Format Percent label', () => {
    const pipe = new UnitLabelTransformationPipe();
    expect(pipe.transform(Unit.PERCENT)).toBe('');
  });

  it('Format FTE label', () => {
    const pipe = new UnitLabelTransformationPipe();
    expect(pipe.transform(Unit.FTE)).toBe(Unit.FTE);
  });

  it('Format CHF label', () => {
    const pipe = new UnitLabelTransformationPipe();
    expect(pipe.transform(Unit.CHF)).toBe(Unit.CHF);
  });

  it('Format Number label', () => {
    const pipe = new UnitLabelTransformationPipe();
    expect(pipe.transform(Unit.NUMBER)).toBe('Zahl');
  });

  it('Format non-default label', () => {
    const pipe = new UnitLabelTransformationPipe();
    const nonDefaultUnit = 'MEMBERS';
    expect(pipe.transform(nonDefaultUnit)).toBe(nonDefaultUnit);
  });
});
