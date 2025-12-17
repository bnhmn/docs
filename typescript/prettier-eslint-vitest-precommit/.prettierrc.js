/**
 * See https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
export default {
  plugins: ["prettier-plugin-sort-json"],
  tabWidth: 2,
  useTabs: false,
  printWidth: 100,
  trailingComma: "all",
  singleQuote: false,
  objectWrap: "collapse",
  overrides: [
    {
      files: "schemas/**/*.json",
      options: {
        // https://github.com/Gudahtt/prettier-plugin-sort-json
        jsonRecursiveSort: true,
        jsonSortOrder: JSON.stringify(
          [
            "$schema",
            "$id",
            "$comment",
            "$ref",
            "/^\\$.*/",
            "title",
            "description",
            "type",
            "properties",
            "required",
            "additionalProperties",
            "/^[^\\d]/",
            "/^\\d/",
            "if",
            "then",
            "else",
          ].reduce((object, key) => ({ ...object, [key]: "none" }), {}),
        ),
      },
    },
  ],
};
