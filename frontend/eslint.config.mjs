import eslint from '@eslint/js'
import tsEslint from 'typescript-eslint'
import unusedImports from 'eslint-plugin-unused-imports'
import stylistic from '@stylistic/eslint-plugin'
import html from '@html-eslint/eslint-plugin'
import angular from 'angular-eslint'

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
    languageOptions: {
      globals: {
        //Cypress Undefined
        cy: 'readonly',
        Cypress: 'readonly',
        it: 'readonly',
        describe: 'readonly',
        expect: 'readonly',
        beforeEach: 'readonly',
        before: 'readonly',
        //Dom undefined
        localStorage: 'readonly',
        console: 'readonly',
        window: 'readonly',
        document: 'readonly',
        //Event undefined
        MouseEvent: 'readonly',
        KeyboardEvent: 'readonly',
        Event: 'readonly',
        //HTML Elements undefined
        HTMLDivElement: 'readonly',
        HTMLInputElement: 'readonly',
        HTMLSpanElement: 'readonly',
        HTMLElement: 'readonly',
        //Other undefined
        ResizeObserver: 'readonly',
        ResizeObserverEntry: 'readonly',
        setTimeout: 'readonly',
        JQuery: 'readonly',
        Document: 'readonly',
        URL: 'readonly',
      },
    },
    rules: {
      ...stylistic.configs['all-flat'].rules,
      'unused-imports/no-unused-imports': 'error',

      // ToDo: Disable rules so eslint passes, fix in followup ticket
      '@typescript-eslint/no-unused-vars': [
        'error',
        {
          args: 'none',
        },
      ],
      '@typescript-eslint/ban-ts-comment': 'error',
      '@typescript-eslint/no-unused-expressions': [
        'error',
        {
          allowTernary: true,
        },
      ],
      'no-undef': 'error',
      '@typescript-eslint/no-explicit-any': 'off',
      '@typescript-eslint/no-namespace': [
        'error',
        {
          allowDeclarations: true,
        },
      ],
      'prefer-rest-params': 'error',
      '@typescript-eslint/no-empty-function': ['error', { allow: ['arrowFunctions', 'constructors'] }],
      '@stylistic/lines-around-comment': ['error'],
      '@angular-eslint/no-empty-lifecycle-method': 'error',
      '@angular-eslint/component-class-suffix': 'error',
      '@typescript-eslint/no-non-null-asserted-optional-chain': 'error',
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
      '@stylistic/function-paren-newline': ['error', { minItems: 4 }],
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
    extends: [...tsEslint.configs.recommended],
    rules: {
      '@typescript-eslint/no-explicit-any': 'off',
      'prefer-rest-params': 'off',
      '@typescript-eslint/no-empty-function': 'off',
      '@typescript-eslint/ban-ts-comment': 'off',
    },
  },

  {
    files: ['**/*.html'],
    extends: [
      ...angular.configs.templateRecommended,
      // Add any other recommended configs here if needed
    ],
    languageOptions: {
      // Choose one parser. For Angular templates, usually angular.templateParser is needed.
      parser: angular.templateParser,
    },
    rules: {
      // Combine all sets of rules
      ...html.configs.recommended.rules,
      ...html.configs['flat/recommended'].rules,

      '@angular-eslint/template/eqeqeq': 'error',
      '@html-eslint/indent': ['error', 2],
      '@html-eslint/require-img-alt': 'error',
      '@html-eslint/require-closing-tags': ['error', { selfClosing: 'always' }],
      '@angular-eslint/template/banana-in-box': 'error',
      '@angular-eslint/template/no-negated-async': 'error',
      //For Some reason the following rule does not work with the angular-parser
      '@html-eslint/element-newline': 'off',
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
