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
            {
                "@typescript-eslint/naming-convention": [
                    "error",
                    {
                        "selector": "default",
                        "format": ["camelCase"],
                        "leadingUnderscore": "allow"
                    },
                    {
                        "selector": "variable",
                        "format": ["camelCase", "UPPER_CASE"]
                    },
                    {
                        "selector": "function",
                        "format": ["camelCase"]
                    },
                    {
                        "selector": ["class", "interface", "typeAlias", "enum"],
                        "format": ["PascalCase"]
                    },
                    {
                        "selector": "enumMember",
                        "format": ["UPPER_CASE"]
                    },
                    {
                        "selector": "property",
                        "modifiers": ["private"],
                        "format": ["camelCase"],
                        "leadingUnderscore": "require"
                    },
                    {
                        "selector": "method",
                        "format": ["camelCase"]
                    },
                    {
                        "selector": "typeParameter",
                        "format": ["PascalCase"],
                        "prefix": ["T"]
                    }
                ]
            }
    }
    ,
    {
        ...
            css.configs["flat/recommended"],
        files:
            ["**/*.scss"],
        rules:
            {
                "@typescript-eslint/naming-convention": [
                    "error",
                    {
                        "selector": "default",
                        "format": ["kebab-case"]
                    },
                    {
                        "selector": "variable",
                        "format": ["kebab-case"]
                    },
                    {
                        "selector": "class",
                        "format": ["kebab-case"]
                    },
                    {
                        "selector": "id",
                        "format": ["kebab-case"]
                    }
                ]
            }
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