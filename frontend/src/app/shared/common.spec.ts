import { getNumberOrNull } from './common';

describe('test common functions', () => {
  describe('getNumberOrNull', () => {
    test('should get simple number correctly', () => {
      expect(getNumberOrNull('-123456789')).toBe(-123456789);
      expect(getNumberOrNull('-5')).toBe(-5);
      expect(getNumberOrNull('-0')).toBe(-0);
      expect(getNumberOrNull('0')).toBe(0);
      expect(getNumberOrNull('+0')).toBe(0);
      expect(getNumberOrNull('3')).toBe(3);
      expect(getNumberOrNull('123456789')).toBe(123456789);
    });

    test('should get special number correctly', () => {
      expect(getNumberOrNull('03')).toBe(3);
      expect(getNumberOrNull(' 3')).toBe(3);
      expect(getNumberOrNull(' 3 ')).toBe(3);
      expect(getNumberOrNull('3 ')).toBe(3);
      expect(getNumberOrNull('+3 ')).toBe(3);
      expect(getNumberOrNull('-3 ')).toBe(-3);
      expect(getNumberOrNull('3foo')).toBe(3);
      expect(getNumberOrNull('+3foo')).toBe(3);
      expect(getNumberOrNull('-3foo')).toBe(-3);
      expect(getNumberOrNull(' 3foo')).toBe(3);
      expect(getNumberOrNull(' +3foo')).toBe(3);
      expect(getNumberOrNull(' -3foo')).toBe(-3);
      expect(getNumberOrNull('03.2')).toBe(3);
      expect(getNumberOrNull('03,2')).toBe(3);
      expect(getNumberOrNull('3+2')).toBe(3);
    });

    test('should get null if the argument has no digit', () => {
      expect(getNumberOrNull(null)).toBe(null);
      // expect(getNumberOrNull(undefined)).toBe(null);
      expect(getNumberOrNull('')).toBe(null);
      expect(getNumberOrNull(' ')).toBe(null);
      expect(getNumberOrNull('foo')).toBe(null);
      expect(getNumberOrNull('null')).toBe(null);
      expect(getNumberOrNull('undefined')).toBe(null);
    });

    test('should get null if the argument more that spaces, + or - a the beginning', () => {
      expect(getNumberOrNull('foo3')).toBe(null);
      expect(getNumberOrNull('foo+3')).toBe(null);
      expect(getNumberOrNull('+foo3')).toBe(null);
      expect(getNumberOrNull(' + 3')).toBe(null);
      expect(getNumberOrNull('+ 3')).toBe(null);
      expect(getNumberOrNull('+ 3 ')).toBe(null);
      expect(getNumberOrNull(' - 3')).toBe(null);
      expect(getNumberOrNull('- 3')).toBe(null);
      expect(getNumberOrNull('- 3 ')).toBe(null);
    });
  });
});
