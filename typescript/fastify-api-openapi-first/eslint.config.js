import eslint from "@eslint/js";
import { globalIgnores } from "eslint/config";
import tseslint from "typescript-eslint";

// See https://typescript-eslint.io/getting-started/
// See https://typescript-eslint.io/getting-started/typed-linting

export default tseslint.config(
  eslint.configs.recommended,
  tseslint.configs.recommendedTypeChecked,
  {
    languageOptions: {
      parserOptions: {
        projectService: true,
        tsconfigRootDir: import.meta.dirname,
      },
    },
    rules: {
      // See https://typescript-eslint.io/rules/ and https://eslint.org/docs/latest/rules/
    },
  },
  globalIgnores(["eslint.config.js"]),
);
