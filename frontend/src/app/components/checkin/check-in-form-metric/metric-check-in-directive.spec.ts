import { MetricCheckInDirective } from './metric-check-in-directive';

describe('MetricCheckInDirective', () => {
  it('create an instance', () => {
    const directive = new MetricCheckInDirective();
    expect(directive).toBeTruthy();
  });

  it.each([
    ['HelloWorld200', 200],
    ['200HelloWorld', 200],
    ["200'000", 200000],
    ['1050&%ç*', 1050],
    ['-1', -1],
    ['-ç13&%', -13],
  ])('should parse value %s correctly to %s', (value: string, expected: number) => {
    const mockOnChange = jest.fn();
    const directive = new MetricCheckInDirective();
    directive.registerOnChange(mockOnChange);

    directive.handleInput(value);

    expect(mockOnChange).toHaveBeenCalledWith(expected);
  });
});
