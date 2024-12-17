import * as eslintHelper from "./eslintHelper.mjs"

describe("eslintHelper", () => {
  const combinedRegex:string   = "((?=.*[Kk]?[Ee]?[Yy].?[Rr]?[Ee]?[Ss]?[Uu]?[Ll]?[Tt]?.*)(.*[Kk]eyResult.*|[A-Z_]*KEY_RESULT[A-Z_]*)|(?=.*[Cc]?[Hh]?[Ee]?[Cc]?[Kk].?[Ii]?[Nn]?.*)(.*[Cc]heckIn.*|[A-Z_]*CHECK_IN[A-Z_]*))|((?=^(?!.*[Kk][Ee][Yy].?[Rr][Ee][Ss][Uu][Ll][Tt]).*)(?=^(?!.*[Cc][Hh][Ee][Cc][Kk].?[Ii][Nn]).*))"
  const keyResultRegex:string   = "((?=.*[Kk]?[Ee]?[Yy]?[Rr]?[Ee]?[Ss]?[Uu]?[Ll]?[Tt]?.*)(.*[Kk]eyResult.*|[A-Z_]*KEY_RESULT[A-Z_]*))|((?=^(?!.*[Kk][Ee][Yy][Rr][Ee][Ss][Uu][Ll][Tt]).*))"
  const CheckInRegex:string   = "((?=.*[Cc]?[Hh]?[Ee]?[Cc]?[Kk]?[Ii]?[Nn]?.*)(.*[Cc]heckIn.*|[A-Z_]*CHECK_IN[A-Z_]*))|((?=^(?!.*[Cc][Hh][Ee][Cc][Kk][Ii][Nn]).*))"


  it.each([
    // [["KeyResult"], /KeyResult/],
    // [["CheckIn"], /CheckIn/],
    [["KeyResult", "CheckIn"], /KeyResult|CheckIn/],
  ])("should return regex %p", (wordToRegex) => {
    expect(eslintHelper.createRegexForWords(wordToRegex)).toEqual(combinedRegex);

  });
})

