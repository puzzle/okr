import tsEslint from "typescript-eslint";
import html from "@html-eslint/eslint-plugin";

export default tsEslint.config(
  {
      extends: [tsEslint.configs.base],
  },
  {
    files: ["**/*.ts"], languageOptions: {
      parserOptions: {
        projectService: true, tsconfigRootDir: import.meta.dirname
      }
    },
      rules: {
        "@typescript-eslint/naming-convention": ["error", {

          selector: ["class", "interface"], format: ["PascalCase"]
        }, {
          selector: "typeProperty", types: ["string"], format: ["UPPER_CASE"]
        }, {
          selector: "variable", modifiers: ["const"], types: ["string"], format: ["UPPER_CASE"]
        }, {
          selector: "variable", format: ["camelCase"]
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
          ]
      }
  }, {
    ...html.configs["flat/recommended"],
    files: ["**/*.html"],
    rules: {
      "@html-eslint/id-naming-convention": ["error", "kebab-case"],
      // "@typescript-eslint/naming-convention": ["error", {
      //
      //   selector: ["class", "interface", "typeAlias"], format: ["PascalCase"]
      // }
      // ]
    }
  },
  {
      files: ["**/*.scss"], rules: {}
  },
);
