# JSON Schema

[JSON Schema](https://json-schema.org/) is a declarative format for describing the structure, data types,
allowed values, and rules of JSON documents. It can be used to validate both JSON and YAML data and to
provide documentation and editor assistance.
A JSON Schema does not contain the actual application data. Instead, it defines what valid data should look like.

For example, this JSON document describes a user:

```json
{
  "name": "Alice",
  "age": 30,
  "email": "alice@example.com"
}
```

A corresponding JSON Schema could be:

```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "$id": "https://example.com/user.schema.json",
  "title": "User",
  "description": "A user account",
  "type": "object",
  "properties": {
    "name": {"type": "string", "description": "The user's display name"},
    "age": {"type": "integer", "minimum": 0},
    "email": {"type": "string", "format": "email"}
  },
  "required": ["name", "email"],
  "additionalProperties": false
}
```

## JSON Schema Store

The [SchemaStore](https://www.schemastore.org/) provides community-maintained schemas for common files such as:

* package.json
* tsconfig.json
* GitHub Actions workflows
* Docker Compose files
* Kubernetes configuration

## General IDE Support

Many IDEs automatically fetch schemas from public sources, such as the SchemaStore, when you open a file that
matches a file pattern listed in the internal schema catalog.

If the IDE doesn't automatically detect the schema based on the file pattern, you can explicitly specify the schema
in JSON files using the `$schema` property. The path can be either a publicly accessible URL or a relative path on
the file system:

```json
{
  "$schema": "https://docs.renovatebot.com/renovate-schema.json"
}
```

(unfortunately, this doesn't work so well in YAML files)

## IntelliJ IDEA

IntelliJ IDEA includes [built-in JSON Schema support](https://www.jetbrains.com/help/idea/json.html).

For a custom schema:

1. Go to **Languages & Frameworks | Schemas and DTDs | JSON Schema Mappings**.
2. Add the schema file or URL.
3. Associate it with files or filename patterns.

Or create a [.idea/jsonSchemas.xml](.idea/jsonSchemas.xml) file in your workspace.
This is quite useful because you can store both this file and the schema (e.g., [cloudbuild.schema.json](.idea/jsonSchemas/cloudbuild.schema.json))
in the repository, ensuring it works the same way for every developer.

IntelliJ also offers a feature that allows you to [configure language injection via JSON schema](https://youtrack.jetbrains.com/issue/IJPL-63659/Specify-language-injection-in-a-json-schema-file).
For example, you can specify that the `script` property of the following schema should automatically have shell script
syntax highlighting and validation applied to it:

```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "properties": {
    "script": {
      "description": "Specify a shell script to execute in the step.",
      "markdownDescription": "Specify a shell script to execute in the step",
      "type": "string",
      "x-intellij-language-injection": "Shell Script"
    }
  }
}
```

## Visual Studio Code

VS Code also includes [built-in JSON Schema support](https://code.visualstudio.com/docs/languages/json).

For a custom schema, add the following to your `.vscode/settings.json` file:

```json
{
  "json.schemas": [
    {
      "fileMatch": ["**/cloudbuild.yaml"],
      "url": ".vscode/jsonSchemas/cloudbuild.schema.json"
    }
  ]
}
```
