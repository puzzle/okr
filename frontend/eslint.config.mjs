import eslint from '@eslint/js'
import tsEslint from 'typescript-eslint'
import unusedImports from 'eslint-plugin-unused-imports'
import stylistic from '@stylistic/eslint-plugin'
import html from '@html-eslint/eslint-plugin'
import angular from 'angular-eslint'
import htmlParser from '@html-eslint/parser'

export default tsEslint.config(
  {
    files: ['**/*.ts'],
    extends: [
      eslint.configs.recommended,
      ...tsEslint.configs.recommended,
      ...tsEslint.configs.stylistic,
      ...angular.configs.tsRecommended,
    ],
    processor: angular.processInlineTemplates,
    rules: {
      ...stylistic.configs['all-flat'].rules,
      'unused-imports/no-unused-imports': 'error',

      // ToDo: Disable rules so eslint passes, fix in followup ticket
      '@typescript-eslint/no-unused-vars': 'off',
      '@typescript-eslint/no-unused-expressions': 'off',
      '@typescript-eslint/ban-ts-comment': 'off',
      'no-undef': 'off',
      '@typescript-eslint/no-explicit-any': 'off',
      '@typescript-eslint/no-namespace': 'off',
      'prefer-rest-params': 'off',
      '@typescript-eslint/no-empty-function': ['off'],
      '@stylistic/lines-around-comment': ['off'],
      '@angular-eslint/no-empty-lifecycle-method': 'off',
      '@angular-eslint/component-class-suffix': 'off',
      '@angular-eslint/template/eqeqeq': 'off',
      '@angular-eslint/template/interactive-supports-focus': 'off',
      '@typescript-eslint/no-non-null-asserted-optional-chain': 'off',
      '@typescript-eslint/no-non-null-assertion': 'off',
      '@stylistic/no-extra-parens': 'off',
      '@typescript-eslint/no-confusing-non-null-assertion': 'off',
      //Delete these rules after fixing all the issues and enabling the actual rules
      '@stylistic/quotes': 'off',
      '@stylistic/function-call-argument-newline': 'off',

      //Actual formatting rules
      // '@stylistic/function-call-argument-newline': ['error', 'never'],
      // '@stylistic/quotes': ['error', 'double'],
      '@stylistic/padded-blocks': ['error', 'never'],
      '@stylistic/dot-location': ['error', 'property'],
      '@stylistic/newline-per-chained-call': ['error', { ignoreChainWithDepth: 1 }],
      '@stylistic/indent': ['error', 2],
      '@stylistic/quote-props': ['error', 'as-needed'],
      '@stylistic/object-property-newline': ['error'],
      '@stylistic/multiline-ternary': ['off'],
      '@stylistic/object-curly-spacing': ['error', 'always'],
      '@stylistic/array-bracket-newline': ['error', { minItems: 4 }],
      '@stylistic/semi-style': ['error'],
      "space-before-function-paren": ["error", "always"],
      '@stylistic/function-paren-newline': ['error', { minItems: 4 }],
     " @stylistic/space-before-function-paren": ["error", "never"],

      '@angular-eslint/directive-selector': [
        'error',
        {
          type: 'attribute',
          // reenable this after fixing all the directives
          // prefix: 'app',
          style: 'camelCase',
        },
      ],
      '@angular-eslint/component-selector': [
        'error',
        {
          type: 'element',
          prefix: 'app',
          style: 'kebab-case',
        },
      ],
    },
  },
  {
    files: ['**/*.spec.ts'],
    rules: {
      '@typescript-eslint/no-explicit-any': 'off',
      'prefer-rest-params': 'off',
      '@typescript-eslint/no-empty-function': 'off',
    },
  },

  {
    files: ['**/*.html'],
    // recommended configuration included in the plugin
    ...html.configs['flat/recommended'],
    languageOptions: {
      parser: htmlParser,
    },
    rules: {
      ...html.configs['flat/recommended'].rules,
      // Must be defined. If not, all recommended rules will be lost
      '@html-eslint/indent': ['error', 2],
      '@html-eslint/require-img-alt': 'off',
      '@html-eslint/element-newline': 'off',
      '@html-eslint/require-closing-tags': ['error', { selfClosing: 'always' }],
    },
  },
  {
    plugins: {
      'unused-imports': unusedImports,
      '@stylistic': stylistic,
      '@html-eslint': html,
    },
  }
)
