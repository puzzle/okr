import { UnitValueTransformationPipe } from './unit-value-transformation.pipe';

describe('UnitTransformationPipe', () => {
  it('create an instance', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe).toBeTruthy();
  });

  it('should format as Percent', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(380)).toBe('380');
  });

  it('should format as Number', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(38000)).toBe("38'000");
  });

  it('should format as CHF as double value', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(380.987)).toBe('380.99');
  });

  it('should return with no format if unit is not preset one', () => {
    const pipe = new UnitValueTransformationPipe();
    expect(pipe.transform(140)).toBe('140');
  });
});
