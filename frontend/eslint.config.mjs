
import eslint from "@eslint/js";
import tsEslint from "typescript-eslint";
import unusedImports from "eslint-plugin-unused-imports";
import stylistic from "@stylistic/eslint-plugin";


export default tsEslint.config(
  eslint.configs.recommended,
  ...tsEslint.configs.recommended,
    stylistic.configs['all-flat'],
    {
      files: ['**/*.spec.ts'],
      rules: {
        "@typescript-eslint/no-explicit-any": "off"
    },
  },
  {
    plugins: {
      'unused-imports': unusedImports,
      '@stylistic': stylistic
    },
    rules: {
      'unused-imports/no-unused-imports': 'error',
        "@typescript-eslint/no-unused-vars": "off",
        "@typescript-eslint/no-unused-expressions": "off",
        "no-undef": "off",
    },
  }
);
