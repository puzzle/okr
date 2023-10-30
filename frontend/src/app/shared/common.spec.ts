import {
  areEqual,
  calculateCurrentPercentage,
  formInputCheck,
  getNumberOrNull,
  getQueryString,
  getValueFromQuery,
  optionalReplaceWithNulls,
  optionalValue,
  sanitize,
} from './common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from './types/model/User';
import { keyResultMetricMinScoring, keyResultOrdinalMinScoring } from './testData';
import { KeyResultMetricMin } from './types/model/KeyResultMetricMin';

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

  it.each([
    [[], [], true],
    [[1], [], false],
    [[1, 2], [], false],
    [[1], [1], true],
    [[1, 2], [1, 2], true],
    [[1, 2, 3], [1, 2, 3], true],
    [[1, 2, 3], [1, 2, 3, 4], false],
    [[1, 2, 3], [1, 2, 3, 3], false],
    [[1, 2, 3], [3, 2, 1], true],
  ])('should give correct output for deep equal', (arr1: number[], arr2: number[], output: boolean) => {
    expect(areEqual(arr1, arr2)).toBe(output);
    expect(areEqual(arr2, arr1)).toBe(output);
  });

  it.each([
    [[], undefined, []],
    [undefined, undefined, []],
    ['', undefined, []],
    [[1], undefined, [1]],
    [[1, NaN], undefined, [1]],
    [[1, '', 3], undefined, [1, 3]],
    ['1,3', undefined, [1, 3]],
    ['1,3.5', undefined, [1]],
    ['1,3.5,3', undefined, [1, 3]],
    ['1,nonsense,3', undefined, [1, 3]],
    [0, undefined, [0]],
    [[0], undefined, [0]],
    ['0', undefined, [0]],
    ['1,0', undefined, [1, 0]],
    ['0,1', undefined, [0, 1]],
    ['', 1, [1]],
    [[], 1, [1]],
  ])('should give correct output for getValueFromQuery', (value: any, fallback: number | undefined, arr2: number[]) => {
    expect(getValueFromQuery(value, fallback)).toStrictEqual(arr2);
  });

  it.each([
    [{ v: undefined }, {}],
    [{ v: '' }, {}],
    [{ v: [] }, {}],
    [{ v: [1] }, { v: [1] }],
    [{ v: 1 }, { v: 1 }],
    [{ v: 1, v2: undefined }, { v: 1 }],
    [{ v: undefined, v2: 1 }, { v2: 1 }],
  ])('should give correct output for optionalValue', (value: { [p: string]: any }, expected: { [p: string]: any }) => {
    expect(optionalValue(value)).toStrictEqual(expected);
  });

  it.each([
    [' test ', 'test'],
    ['ffF', 'fff'],
  ])('test sanitize function', (str: any, expected: string) => {
    expect(sanitize(str)).toBe(expected);
  });

  it.each([
    [0, 100, 20, 20],
    [100, 0, 20, 80],
    [500, 400, 460, 40],
    [100, 10, 110, 0],
    [0, 100, -10, 0],
    [0, 100, -10, 0],
    [0, 100, -10, 0],
  ])(
    'should calculate progress correctly',
    async (baseline: number, stretchGoal: number, value: number, filledPercentage: number) => {
      const keyResult = {
        ...keyResultMetricMinScoring,
        baseline: baseline,
        stretchGoal: stretchGoal,
        lastCheckIn: { ...keyResultOrdinalMinScoring.lastCheckIn, value: value },
      } as KeyResultMetricMin;
      let percentage = calculateCurrentPercentage(keyResult);
      expect(percentage).toBe(filledPercentage);
    },
  );

  it.each([
    ['t%20t', 't t'],
    ['%20', ''],
    ['f%20', 'f'],
    ['%20f', 'f'],
    ['test', 'test'],
  ])('test getQueryString function', (str: any, expected: string) => {
    expect(getQueryString(str)).toBe(expected);
  });

  it.each([
    [{ v: undefined }, { v: null }],
    [{ v: '' }, { v: null }],
    [{ v: [] }, { v: null }],
    [{ v: [1] }, { v: [1] }],
    [{ v: 1 }, { v: 1 }],
    [
      { v: 1, v2: undefined },
      { v: 1, v2: null },
    ],
    [
      { v: undefined, v2: 1 },
      { v: null, v2: 1 },
    ],
  ])(
    'should give correct output for optionalReplaceWithNulls',
    (obj1: { [p: string]: any }, obj2: { [p: string]: any }) => {
      expect(optionalReplaceWithNulls(obj1)).toStrictEqual(obj2);
    },
  );

  test('should return correct class', () => {
    let testForm = new FormGroup({
      title: new FormControl<string | undefined>(undefined, [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(250),
      ]),
      description: new FormControl<string>('', [Validators.maxLength(4096)]),
      code: new FormControl<number>(0, [Validators.min(5), Validators.max(10)]),
    });
    testForm.controls['code'].markAsDirty();
    testForm.controls['title'].markAsTouched();
    testForm.controls['description'].markAsDirty();

    //False check
    expect(formInputCheck(testForm, 'code')).toBe('dialog-form-field-error');
    expect(formInputCheck(testForm, 'title')).toBe('dialog-form-field-error');

    //Fill in value to match validation
    testForm.controls.code.setValue(8);
    testForm.controls.title.setValue('Test');

    //True check
    expect(formInputCheck(testForm, 'description')).toBe('dialog-form-field');
    expect(formInputCheck(testForm, 'code')).toBe('dialog-form-field');
    expect(formInputCheck(testForm, 'title')).toBe('dialog-form-field');
  });
});
