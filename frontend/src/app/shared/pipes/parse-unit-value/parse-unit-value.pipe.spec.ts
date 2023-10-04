import { ParseUnitValuePipe } from './parse-unit-value.pipe';

describe('ParseUnitValuePipe', () => {
  it('create an instance', () => {
    const pipe = new ParseUnitValuePipe();
    expect(pipe).toBeTruthy();
  });

  it('should replace characters at end of string', () => {
    const pipe = new ParseUnitValuePipe();
    expect(pipe.transform('200HelloWorld')).toBe(200);
  });

  it('should replace characters at beginning of string', () => {
    const pipe = new ParseUnitValuePipe();
    expect(pipe.transform('HelloWorld200')).toBe(200);
  });

  it('should replace characters between strings', () => {
    const pipe = new ParseUnitValuePipe();
    expect(pipe.transform("200'000")).toBe(200000);
  });

  it('should replace special characters', () => {
    const pipe = new ParseUnitValuePipe();
    expect(pipe.transform('1050&%ç*')).toBe(1050);
  });
});
