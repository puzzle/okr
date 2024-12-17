import tsEslint from "typescript-eslint";
import html from "@html-eslint/eslint-plugin";

export default tsEslint.config(
  {
      extends: [tsEslint.configs.base],
  },
  {
    files: ["**/*.ts"], languageOptions: {
      parserOptions: {
        project: [ "./tsconfig.json","./tsconfig.spec.json"],
      }
    },
      rules: {
        "@typescript-eslint/naming-convention": ["error", {

          selector: ["class", "interface"], format: ["PascalCase"]
        }, {
        //   It has to many exceptions for me to check this usefully
        //
        //   selector: "typeProperty", types: ["string"], modifiers: ["readonly"], format: ["UPPER_CASE"]
        // },{
        //   selector: "classProperty", modifiers: ["readonly"], format: ["UPPER_CASE"]
        // }, {
        //   selector: "variable", modifiers: ["global"], types: ["string"], format: ["UPPER_CASE"]
        // }, {
        //     selector: "variable", modifiers: [], format: ["camelCase"]
        // },  {
          selector: "enum", format: ["PascalCase"]
        }, {
          selector: "enumMember", format: ["UPPER_CASE"]
        }, {
          selector: ["method", "function"], format: ["camelCase"]
        }, {
        //   selector: "property", format: ["camelCase"]
        // }, {
          selector: "typeParameter", format: ["PascalCase"]
        }
          ],
       "id-match": [
         "error",
         createRegexForWords(["KeyResult", "CheckIn", "TeamManagement", "StretchGoal"])
         // "((?=.*[Kk]?[Ee]?[Yy].?[Rr]?[Ee]?[Ss]?[Uu]?[Ll]?[Tt]?.*)(.*[Kk]eyResult.*|[A-Z_]*KEY_RESULT[A-Z_]*)|(?=.*[Cc]?[Hh]?[Ee]?[Cc]?[Kk].?[Ii]?[Nn]?.*)(.*[Cc]heckIn.*|[A-Z_]*CHECK_IN[A-Z_]*))|((?=^(?!.*[Kk][Ee][Yy].?[Rr][Ee][Ss][Uu][Ll][Tt]).*)(?=^(?!.*[Cc][Hh][Ee][Cc][Kk].?[Ii][Nn]).*))"
         // "((?=.*[Ss]?[Tt]?[Rr]?[Ee]?[Tt]?[Cc]?[Hh].?[Gg]?[Oo]?[Aa]?[Ll]?.*)(.*[Ss]tretchGoal.*|[A-Z_]*STRETCH_GOAL[A-Z_]*)|^(?!.*[Ss][Tt][Rr][Ee][Tt][Cc][Hh].?[Gg][Oo][Aa][Ll]).*)"
         // "((?=.*[Kk]?[Ee]?[Yy].?[Rr]?[Ee]?[Ss]?[Uu]?[Ll]?[Tt]?.*)(.*[Kk]eyResult.*|[A-Z_]*KEY_RESULT[A-Z_]*)|^(?!.*[Kk][Ee][Yy].?[Rr][Ee][Ss][Uu][Ll][Tt]).*)"
         // "((?=.*[Cc]?[Hh]?[Ee]?[Cc]?[Kk].?[Ii]?[Nn]?.*)(.*[Cc]heckIn.*|[A-Z_]*CHECK_IN[A-Z_]*)|^(?!.*[Cc][Hh][Ee][Cc][Kk].?[Ii][Nn]).*)"
         // "((?=.*[Tt]?[Ee]?[Aa]?[Mm].?[Mm]?[Aa]?[Nn]?[Aa]?[Gg]?[Ee]?[Mm]?[Ee]?[Nn]?[Tt]?.*)(.*[Tt]eamManagement.*|[A-Z_]*TEAM_MANAGEMENT[A-Z_]*)|^(?!.*[Tt][Ee][Aa][Mm].?[Mm][Aa][Nn][Aa][Gg][Ee][Mm][Ee][Nn][Tt]).*)"
       ]
      }
  }, {

    ...html.configs["flat/recommended"],
    files: ["**/*.html"],

    rules: {
      "@html-eslint/id-naming-convention": ["error", "kebab-case"],
    }
  },
  // {
  //     files: ["**/*.scss"], rules: {}
  // },
);


//Creates a big regex to check the right spelling on Words in the List
function createRegexForWords(wordList) {
// This function builds a case-insensitive regex pattern for a given word.
  function makeRegexForWord(word, ignoreQuestionMark = false) {
    return word
      .split('')
      .map((char, index) => {
        const regex = `[${char.toUpperCase()}${char.toLowerCase()}]`;

        if (index === 0) return regex;
        if (index === word.length - 1) return ignoreQuestionMark ? regex : `?${regex}?`;
        if (char.match(/[A-Z]/)) return `.?${regex}`;

        return ignoreQuestionMark ? regex : `?${regex}`;
      })
      .join('');
  }

  function transformToUnderscoreUppercase(word) {
    return word
      .split(/(?=[A-Z])/) // Split at uppercase letters without removing them
      .join('_')
      .toUpperCase();
  }

  function buildPart2(word) {
    const flexibleWord = makeRegexForWord(word, true);
    return `(?=^(?!.*${flexibleWord}).*)`;
  }

  function buildPart1(word) {
    const flexibleWord = makeRegexForWord(word, false);
    const wordPart = `[${word[0]}${word[0].toLowerCase()}]${word.slice(1)}`; // Make the first character case-insensitive
    return `(?=.*${flexibleWord}.*)(.*${wordPart}.*|[A-Z_]*${transformToUnderscoreUppercase(word)}[A-Z_]*)`;
  }
  const part1List = [];
  const part2List = [];

  wordList.forEach(word => {
    const part1 = buildPart1(word);
    const part2 = buildPart2(word);
    part1List.push(part1);
    part2List.push(part2);
  });

  return `(${part1List.join('|')})|(${part2List.join('')})`;
}
