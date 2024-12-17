import tsEslint from "typescript-eslint";
import html from "@html-eslint/eslint-plugin";
import { createRegexForWords } from "./eslintHelper.mjs"


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
       ]
      }
  }, {

    ...html.configs["flat/recommended"],
    files: ["**/*.html"],
    rules: {
      "@html-eslint/id-naming-convention": ["error", "regex", {pattern: `(?=(^[a-z]+(-[a-z]+)*$))(?=(${createRegexForWords(["KeyResult", "CheckIn", "TeamManagement", "StretchGoal"])}))`}],
    }
  },
);


