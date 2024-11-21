
import eslint from '@eslint/js';
import tseslint from 'typescript-eslint';
import unusedImports from "eslint-plugin-unused-imports";


export default tseslint.config(
  eslint.configs.recommended,
  ...tseslint.configs.recommended,
  {
    files: ['**/*.spec.ts'],
    rules: {
      "@typescript-eslint/no-explicit-any": "off"
    },
  },
  {
    plugins: {
      'unused-imports': unusedImports,
    },
    rules: {
      'unused-imports/no-unused-imports': 'error',
        "@typescript-eslint/no-unused-vars": "off",
        "@typescript-eslint/no-unused-expressions": "off",
        "no-undef": "off",
    },
  }
);
