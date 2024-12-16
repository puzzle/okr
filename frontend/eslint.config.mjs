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
         "((?=.*[Kk]?[Ee]?[Yy].?[Rr]?[Ee]?[Ss]?[Uu]?[Ll]?[Tt]?.*)(.*[Kk]eyResult.*|[A-Z_]*KEY_RESULT[A-Z_]*)|(?=.*[Cc]?[Hh]?[Ee]?[Cc]?[Kk].?[Ii]?[Nn]?.*)(.*[Cc]heckIn.*|[A-Z_]*CHECK_IN[A-Z_]*))|(?=(^(?!.*[Cc][Hh][Ee][Cc][Kk].?[Ii][Nn]).*)(?=^(?!.*[Kk][Ee][Yy].?[Rr][Ee][Ss][Uu][Ll][Tt]).*))"
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

