# IntelliJ Find and Replace

> **Since IDEA 15** you're able to use the below switches to toggle the case of captured expressions. This is now
> [officially documented](https://www.jetbrains.com/idea/help/regular-expression-syntax-reference.html).
>
> * `\l`: lower the case of the one next character
> * `\u`: up the case of the one next character
> * `\L`: lower the case of the next characters until a `\E` or the end of the replacement string
> * `\U`: up the case of the next characters until a `\E` or the end of the replacement string
> * `\E`: mark the end of a case change initiated by `\U` or `\L`
>
> <https://stackoverflow.com/a/32137232/6316545>

Example usage (will convert `FOO_BAR_BAZ` to `foo_bar_baz` etc):

```text
find: (\w+_)+(\w+)
replace: \L$1$2\E
```
