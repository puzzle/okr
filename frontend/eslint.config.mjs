
import eslint from '@eslint/js';
import tseslint from 'typescript-eslint';
import {rules} from "@typescript-eslint/eslint-plugin";

export default tseslint.config(
    eslint.configs.recommended,
    ...tseslint.configs.recommended,
  {
    rules: {
      "unused-imports/no-unused-imports": "error",
    }
  }
);
