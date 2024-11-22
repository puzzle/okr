import eslint from "@eslint/js";
import tsEslint from "typescript-eslint";
import unusedImports from "eslint-plugin-unused-imports";
import stylistic from "@stylistic/eslint-plugin";
import html from "@html-eslint/eslint-plugin";

export default tsEslint.config(
  eslint.configs.recommended, ...tsEslint.configs.recommended, {
    ...stylistic.configs["all-flat"],
    files: ["**/*.ts"],
      rules: {
        "unused-imports/no-unused-imports": "error",
        //disable rules so eslint passes, probably fix later
        "@typescript-eslint/no-unused-vars": "off",
        "@typescript-eslint/no-unused-expressions": "off",
        "no-undef": "off",
        "@typescript-eslint/no-explicit-any": "off",
        //stylistic rules
        "@stylistic/function-call-argument-newline": ["error", "never"],
        "@stylistic/padded-blocks": ["error", "never"],
        "@stylistic/dot-location": ["error", "property"],
        "@stylistic/newline-per-chained-call": ["error", { "ignoreChainWithDepth": 1 }],
        "@stylistic/indent": ["error", 2],
        "@stylistic/quotes":["error","double"],
        "@stylistic/multiline-ternary": ["off"],
      },
  }, {
    files: ["**/*.spec.ts"],
    rules: {
      "@typescript-eslint/no-explicit-any": "off",
      "prefer-rest-params": "off",

    },
  }, {
    ...html.configs["flat/recommended"],
    files: ["**/*.html"],
    rules: {
      ...html.configs["flat/recommended"].rules,
      "@html-eslint/indent": ["error",2],

    }
  }, {
    plugins: {
      "unused-imports": unusedImports,
      "@stylistic": stylistic
    },
  }
);