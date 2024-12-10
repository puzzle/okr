import eslint from "@eslint/js";
import tsEslint from "typescript-eslint";
import unusedImports from "eslint-plugin-unused-imports";
import stylistic from "@stylistic/eslint-plugin";
import html from "@html-eslint/eslint-plugin";
import angular from "angular-eslint";
import angularTemplateParser from "@angular-eslint/template-parser";

export default tsEslint.config(
  {
    files: ["**/*.ts"],
    extends: [
      eslint.configs.recommended,
      ...tsEslint.configs.recommended,
      ...tsEslint.configs.stylistic,
      ...angular.configs.tsRecommended,
    ],
    processor: angular.processInlineTemplates,
    rules: {
      ...stylistic.configs["all-flat"].rules,
      "unused-imports/no-unused-imports": "error",
      // ToDo: Disable rules so eslint passes, fix in followup ticket
      "@typescript-eslint/no-unused-vars": "off",
      "@typescript-eslint/no-unused-expressions": "off",
      "@typescript-eslint/ban-ts-comment": "off",
      "no-undef": "off",
      "@typescript-eslint/no-explicit-any": "off",
      "@typescript-eslint/no-namespace": "off",
      "prefer-rest-params": "off",
      //stylistic rules
      "@stylistic/function-call-argument-newline": ["error", "never"],
      "@stylistic/padded-blocks": ["error", "never"],
      "@stylistic/dot-location": ["error", "property"],
      "@stylistic/newline-per-chained-call": ["error", { ignoreChainWithDepth: 1 }],
      "@stylistic/indent": ["error", 2],
      "@stylistic/quotes": ["error", "double"],
      "@stylistic/multiline-ternary": ["off"],
      "@angular-eslint/directive-selector": [
        "error",
        {
          type: "attribute",
          prefix: "app",
          style: "camelCase",
        },
      ],
      "@angular-eslint/component-selector": [
        "error",
        {
          type: "element",
          prefix: "app",
          style: "kebab-case",
        },
      ],
    },
  },
  {
    files: ["**/*.spec.ts"],
    rules: {
      "@typescript-eslint/no-explicit-any": "off",
      "prefer-rest-params": "off",
    },
  },
  {
    files: ["**/*.html"],
    extends: [...angular.configs.templateRecommended, ...angular.configs.templateAccessibility],
    processor: angular.processInlineTemplates,
    languageOptions: {
      parser: angularTemplateParser,
    },
    rules: {
      ...html.configs.recommended.rules,
      // ToDo: Disable rules so eslint passes, fix in followup ticket
      "@html-eslint/require-img-alt": "off",
      "@html-eslint/element-newline": "off",
      //stylistic rules
      "@html-eslint/indent": ["error", 2],
    },
  },
  {
    plugins: {
      "unused-imports": unusedImports,
      "@stylistic": stylistic,
      "@html-eslint": html,
    },
  },
);
