# Auto-generate Release Notes by Label

Auto-generate the changelog for a GitHub release from categorized pull requests. See [Sample Output](#sample-output).

## How to Setup

Create the labels at <https://github.com/[owner-name]/[repo-name]/labels>.

| Label        | Description                               | RGB Color Code |
|--------------|-------------------------------------------|----------------|
| breaking     | Breaking changes                          | #ad3b24      |
| bug          | Something isn't working as expected       | #d73a4a      |
| cicd         | GitHub workflow enhancements              | #0052cc      |
| dependencies | Dependency updates                        | #682733      |
| documentation| Improvements or additions to documentation| #ecd5cd      |
| feature      | New feature or request                    | #5319e7      |
| performance  | Performance enhancements                  | #fbca04      |
| refactor     | Code refactoring                          | #1d76db      |
| style        | A new code style was applied              | #1d76db      |
| test         | Tests were enhanced or added              | #0e8a16      |

Copy the [release.yml](release.yml) file to `.github/release.yml` inside your GitHub repository.

## How to Use

* Make some code changes, open a pull request and label it accordingly.
* Create a new GitHub release at <https://github.com/[owner-name]/[repo-name]/releases/new>.

## Sample Output

### What's Changed

#### 🚀 New Features

* Add user dashboard with real-time analytics by [@user](.) in [#80](.)
* Introduce dark mode toggle in settings by [@user](.) in [#81](.)
* Implement export to CSV for reports by [@user](.) in [#82](.)

#### 🐞 Bug Fixes

* Fix crash when saving profile with empty phone number by [@user](.) in [#83](.)
* Resolve layout issue on mobile for login screen by [@user](.) in [#84](.)
* Correct error message when uploading invalid file types by [@user](.) in [#85](.)

#### 📚 Documentation

* Add README section on environment setup by [@user](.) in [#86](.)
* Document new API endpoints for user roles by [@user](.) in [#87](.)
* Improve contribution guidelines with examples by [@user](.) in [#88](.)

#### 🔨 Dependency Upgrades

* Bump react to 18.2.0 by [@dependabot](.) in [#89](.)
* Bump express to 4.18.2 by [@dependabot](.) in [#90](.)
* Bump axios to 5.10.0 by [@dependabot](.) in [#91](.)

#### ⚙️ Technical Enhancements

* Refactor authentication middleware for better readability by [@user](.) in [#92](.)
* Apply consistent code style with Prettier by [@user](.) in [#93](.)
* Improve unit test coverage for billing module by [@user](.) in [#94](.)
