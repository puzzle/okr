import * as eslintHelper from "./eslintHelper.mjs"
import { $locationShim } from "@angular/common/upgrade";

describe("eslintHelper", () => {
  const combinedRegex:string   = "((?=.*[Kk]?[Ee]?[Yy].?[Rr]?[Ee]?[Ss]?[Uu]?[Ll]?[Tt]?.*)(.*[Kk]eyResult.*|[A-Z_]*KEY_RESULT[A-Z_]*)|(?=.*[Cc]?[Hh]?[Ee]?[Cc]?[Kk].?[Ii]?[Nn]?.*)(.*[Cc]heckIn.*|[A-Z_]*CHECK_IN[A-Z_]*))|((?=^(?!.*[Kk][Ee][Yy].?[Rr][Ee][Ss][Uu][Ll][Tt]).*)(?=^(?!.*[Cc][Hh][Ee][Cc][Kk].?[Ii][Nn]).*))"
  const keyResultRegex:string   = "((?=.*[Kk]?[Ee]?[Yy].?[Rr]?[Ee]?[Ss]?[Uu]?[Ll]?[Tt]?.*)(.*[Kk]eyResult.*|[A-Z_]*KEY_RESULT[A-Z_]*))|((?=^(?!.*[Kk][Ee][Yy].?[Rr][Ee][Ss][Uu][Ll][Tt]).*))"
  const checkInRegex:string   = "((?=.*[Cc]?[Hh]?[Ee]?[Cc]?[Kk].?[Ii]?[Nn]?.*)(.*[Cc]heckIn.*|[A-Z_]*CHECK_IN[A-Z_]*))|((?=^(?!.*[Cc][Hh][Ee][Cc][Kk].?[Ii][Nn]).*))"

  it.each([
    [["KeyResult"], keyResultRegex],
    [["CheckIn"], checkInRegex],
    [["KeyResult", "CheckIn"], combinedRegex],
  ])("should return regex %p", (wordToRegex, expectedRegex) => {
    expect(eslintHelper.createRegexForWords(wordToRegex)).toEqual(expectedRegex);
  });

  it.each([
    [["KeyResult"], ["KeyResult", "CurrentKeyResult", "keyResult", "keyResultId"],["Keyresult", "CurrentKeyresult", "keyresult", "keyresultId", "KEyResult", "KeyResUlt"]],
    [["CheckIn"], ["CheckIn", "CurrentCheckIn", "checkIn", "checkInId"],["Checkin", "CurrentCheckin", "checkin", "checkinId", "cHeckIn", "checkIN"]],
    [["KeyResult", "CheckIn"], ["KeyResult", "CurrentKeyResult", "keyResult", "keyResultId", "CheckIn", "CurrentCheckIn", "checkIn", "checkInId"],["Keyresult", "CurrentKeyresult", "keyresult", "keyresultId", "KEyResult", "KeyResUlt", "Checkin", "CurrentCheckin", "checkin", "checkinId", "cHeckIn", "checkIN"]],
  ])("should run regex %p threw the matching and not matching list", (wordToRegex:string[], matchingListToRegex:string[], notMatchingListToRegex:string[]) => {
    const regexOfCustomWord = new RegExp(eslintHelper.createRegexForWords(wordToRegex));
    matchingListToRegex = matchingListToRegex.filter(word => regexOfCustomWord.test(word));
    notMatchingListToRegex = notMatchingListToRegex.filter(word => regexOfCustomWord.test(word));

    expect(matchingListToRegex.length).toBe(matchingListToRegex.length);
    expect(notMatchingListToRegex.length).toBe(0)
  });

})

