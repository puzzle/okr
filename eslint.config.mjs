import eslint from "@eslint/js";
import tsEslint from "typescript-eslint";
import unusedImports from "eslint-plugin-unused-imports";
import stylistic from "@stylistic/eslint-plugin";
import html from "@html-eslint/eslint-plugin";
import css from "eslint-plugin-css-modules";

export default tsEslint.config(
    eslint.configs.recommended, ...tsEslint.configs.recommended, {
        ...stylistic.configs["all-flat"],
        files: ["**/*.ts"],
        rules: {
            "@typescript-eslint/naming-convention": [
                "error",
                {
                    "selector": "variable",
                    "format": ["camelCase"]
                },
                {
                    "selector": "enum",
                    "format": ["PascalCase"]
                },
                {
                    "selector": "enumMember",
                    "format": ["UPPER_CASE"]
                },
                {
                    "selector": ["class", "interface"],
                    "format": ["PascalCase"]
                },
            ]
        },
        overrides: [
            {
                files: ["e2e/*.ts"],
                rules: {
                    "@typescript-eslint/naming-convention": [
                        "error",
                        {
                            "selector": "class",
                            "format": ["kebab-case"]
                        }
                    ]
                },
            }
        ]
    },
    {
        ...
            html.configs["flat/recommended"],
        files:
            ["**/*.html"],
        rules:
            {}
    }
    ,
    {
        ...
            css.configs["flat/recommended"],
        files:
            ["**/*.scss"],
        rules:
            {}
    }
    ,
    {
        plugins: {
            "unused-imports":
            unusedImports,
            "@stylistic":
            stylistic
        }
        ,
    }
)
;