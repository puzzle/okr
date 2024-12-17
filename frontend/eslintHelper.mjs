
export function createRegexForWords(wordList) {

// This function builds a case-insensitive regex pattern for a given word.


  const part1List = [];
  const part2List = [];

  wordList.forEach(word => {
    const part1 = createRegexToCheckIfWordLookAlike(word);
    const part2 = createFallbackRegex(word);
    part1List.push(part1);
    part2List.push(part2);
  });
  return `(${part1List.join('|')})|(${part2List.join('')})`;
}

function getCaseInsensitiveRegexForChar(c){
  return `[${c.toUpperCase()}${c.toLowerCase()}]`
}

function createCaseInsensitiveRegexForWord(word) {
  return word
    .match(/[A-Z][a-z]+/g)
    .join(".?")
    .split("")
    .map(c => /[a-zA-Z]/g.test(c) ?getCaseInsensitiveRegexForChar(c) : c)
    .join('');
}

function transformToUnderscoreUppercase(word) {
  return word
    .split(/(?=[A-Z])/) // Split at uppercase letters without removing them
    .join('_')
    .toUpperCase();
}

function transformToHyphenLowercase(word) {
  return word
    .split(/(?=[A-Z])/) // Split at uppercase letters without removing them
    .join('-')
    .toLowerCase();
}

function getWordRegexWithOptionalLetters(word) {
  return word.replace(/(\[[^\[\]]+\])(?![.?])/g, "$1?"); // Puts a "?" between the case-insensitive braces if there is no "?" or "." already
}

function createRegexToCheckIfWordLookAlike(word) {
  let wordLooksLikeRegex = createCaseInsensitiveRegexForWord(word);
  wordLooksLikeRegex = getWordRegexWithOptionalLetters(wordLooksLikeRegex)
  const wordCorrectRegex = getCaseInsensitiveRegexForChar(word[0]) + word.slice(1);
  const wordInUpperCase = transformToUnderscoreUppercase(word)
  const wordInLowerCase = transformToHyphenLowercase(word)
  return `(?=.*${wordLooksLikeRegex}.*)(.*${wordCorrectRegex}.*|[A-Z_]*${wordInUpperCase}[A-Z_]*|[a-z-]*${wordInLowerCase}[a-z-]*)`;
}

function createFallbackRegex(word) {
  const caseInsensitiveWordRegex = createCaseInsensitiveRegexForWord(word);
  return `(?=^(?!.*${caseInsensitiveWordRegex}).*)`;
}

