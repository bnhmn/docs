# Cron

[cron](https://en.wikipedia.org/wiki/Cron) is a job scheduler in Unix-like operating systems that automates the
execution of tasks at specified intervals or times, configured through a file called *crontab*.
Scheduled cron tasks are also known as *cron jobs*.

## Cron Expressions

Cron expressions are a syntax for scheduling cron jobs, consisting of five or six fields that specify when a task should
run (minute, hour, day of the month, month, day of the week, and optionally year).
For example, `0 12 * * 1` runs a job every Monday at noon.

The website [crontab.guru](https://crontab.guru/#0_12_*_*_1) helps you understand cron expressions.

## View Cron Tasks

To view your current crontab, run:

```bash
crontab -l
```

## Edit Cron Tasks

To add or modify cron jobs, run:

```bash
crontab -e
```

This opens the crontab in the default text editor, allowing you to add or change cron jobs.
