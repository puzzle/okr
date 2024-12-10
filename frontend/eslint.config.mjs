import tsEslint from "typescript-eslint";
import html from "@html-eslint/eslint-plugin";

export default tsEslint.config(
  {
      extends: [tsEslint.configs.base],
  },
  {
      files: ["**/*.ts"],
      rules: {
          "@typescript-eslint/naming-convention": [
              "error",
              {
                  selector: "variable",
                  format: ["camelCase"]
              },
              {
                  selector: "enum",
                  format: ["PascalCase"]
              },
              {
                  selector: "enumMember",
                  format: ["UPPER_CASE"]
              },
              {
                  selector: ["class", "interface"],
                  format: ["PascalCase"]
              },
              // {
              //     selector: "class",
              //     format: ["PascalCase"],
              //     filter: {
              //         regex: "^e2e/.*\\.ts$",
              //         match: true
              //     }
              // }
          ]
      }
  },
  // {
  //     ...html.configs["flat/recommended"],
  //     files: ["**/*.html"],
  //     // rules: {
  //     //     "@typescript-eslint/naming-convention": [
  //     //         "error",
  //     //         {
  //     //             selector: "default",
  //     //             format: ["camelCase"],
  //     //             leadingUnderscore: "allow"
  //     //         },
  //     //         {
  //     //             selector: "variable",
  //     //             format: ["camelCase", "UPPER_CASE"]
  //     //         },
  //     //         {
  //     //             selector: "function",
  //     //             format: ["camelCase"]
  //     //         },
  //     //         {
  //     //             selector: ["class", "interface", "typeAlias", "enum"],
  //     //             format: ["PascalCase"]
  //     //         },
  //     //         {
  //     //             selector: "enumMember",
  //     //             format: ["UPPER_CASE"]
  //     //         },
  //     //         {
  //     //             selector: "property",
  //     //             modifiers: ["private"],
  //     //             format: ["camelCase"],
  //     //             leadingUnderscore: "require"
  //     //         },
  //     //         {
  //     //             selector: "method",
  //     //             format: ["camelCase"]
  //     //         },
  //     //         {
  //     //             selector: "typeParameter",
  //     //             format: ["PascalCase"],
  //     //             prefix: ["T"]
  //     //         }
  //     //     ]
  //     // }
  // },
  {
    ...html.configs["flat/recommended"],
      files: ["**/*.scss"],
      rules: {
          "@html-eslint/id-naming-convention": [
              "error",
              "kebab-case"
          ]
      }
  },
);
