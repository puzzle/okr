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
          selector: "typeProperty", types: ["string"], format: ["UPPER_CASE"]
        }, {
          selector: "classProperty", modifiers: ["readonly"], format: ["UPPER_CASE"]
        }, {
          selector: "variable", modifiers: ["const"], types: ["string"], format: ["UPPER_CASE"]
        }, {
          selector: "variable", modifiers: [], format: ["camelCase"]
        }, {
          selector: "enum", format: ["PascalCase"]
        }, {
          selector: "enumMember", format: ["UPPER_CASE"]
        }, {
          selector: ["method", "function"], format: ["camelCase"]
        }, {
          selector: "property", format: ["camelCase"]
        }, {
          selector: "typeParameter", format: ["PascalCase"], prefix: ["T"]
        }
          ],
//        "id-match": [
//          "error",
//          "(?i:key.*result)?^(?:(?!KeyResult).)*"
//        ],
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
