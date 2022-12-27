# ReadingIsGood Backend in Java

It is written in Java 11 using Spring Boot framework. It has AWS and Docker deployment options
To deploy as a Docker run spring-boot build-image plugin

It simply designed as simple MVC pattern. Also, includes various patterns like Builder, Factory etc. 
Includes notification, email and websocket services. 

Also, includes nodejs to build custom email templates with mjml 


## Links
- Production:
  - API Documentation:
- Staging:
  - API Documentation:
- Development:
  - API Documentation Postman [Postman Collection](https://www.postman.com/arifburakdemiray/workspace/book-retail-api)
  - API Documentation Swagger [Swagger UI Local (Has swagger auth)](http://localhost:5000/documentation/index.html)
<hr />

## Branches

- `master` contains development code for now but

Linear flow must follow this: `dev` > `staging` > `master`.

## Development Environment

- **IDE:** [IntelliJ Idea](https://www.jetbrains.com/idea/)
- **Plugins:**
  - [Save Actions](https://plugins.jetbrains.com/plugin/7642-save-actions)
  - [Rainbow Brackets](https://plugins.jetbrains.com/plugin/10080-rainbow-brackets)
  - [Statistic](https://plugins.jetbrains.com/plugin/4509-statistic)
- **Java SDK:** Amazon Corretto 11
- **Build tool:** Apache Maven
- **Database:** MariaDB || MySQL
- **Migration Tool:** Flyway
- **Important Libraries:**
  - Spring Boot
  - Hibernate ORM
- **External Dependencies:**
  - NodeJS
  - Yarn
- **Recommendations:**
  - [DBeaver (Database Viewer)](https://dbeaver.io/)
  - Make your Intellij Idea connected to database.

## Installation

### Prerequisites

- Java 11
- NodeJS
- Yarn

### Instructions

- Clone repository.
- Put filters under resources/filters (Contact with me to take filters)
- Install external dependencies:
  - yarn install.
- Open project with Intellij Idea and let it install maven dependencies.
- Then you are ready to go

## Configuration and Profiles

### Application Configuration File

_application.yml_ is the only configuration file for application. All common configurations must be declared here.

Application specific configuration must be added to **application** namespace. For example:

    # src/main/resources/application.yaml

    application:
      some:
        thing: value

The newly added configurations must also be specified
in `src/main/resources/META-INF/additional-spring-configuration-metadata.json`.

### Profiles

`dev` is for development/staging branch of the application and `prod` is master branch of the application.

There are two primary profiles named `dev` and `prod`. Profile specific variables can be found in folder named after
profile name in _src/main/resources/filters/_. These are called as maven filters. Maven replaces their occurrences in
all resources with their values.

The secondary profile is `local`. It must be activated during development on your computer.
**If you don't activate it, the application assumes it is running on server.**

When starting a new project make sure that provide necessary parameters to start the project. Those are,

In `application.yml` file, provide parameters under `aws`,`onesignal`,and `application.aws` nodes.

In `app.properties` filter, provide missing parameters.
In `db.properties` filter, provide database parameters. You can change driver.
In `jwt.properties` filter, provide a secure secret and client ids. Please also select different private keys for dev and prod filters.
In `mail.properties` filter, provide missing parameters.

## Messages, Templates, Static files etc.

### Messages

All resource bundles located in _src/main/resources/messages_ folder. Try to follow

## Database

### Naming Convention

- Table:
  - Model: PascalCase (singular) (CompanyUser)
  - SQL: snake_case (plural) (example: company_users)
- Column:
  - Model: camelCase (refreshToken)
  - SQL: snake_case (example: refresh_token)

**Note:** Hibernate's naming convention maps camelCase to snake_case and vice versa.

### Migrations

Steps:

- Create new file named `Vx__Migration_name.sql` in _src/main/resources/db/migrations_. Replace `x` in `Vx__` with next
  number.
- Apply migration by clicking _maven tab_ > _book-retail-api_ > _plugins_ > _flyway_ > _migrate_ Intellij Idea. (You can
  use terminal.)
- If the migration fails, run repair, fix your migration file then migrate again.

**Note:** Full official flyway documentation is [here](https://flywaydb.org/documentation/concepts/migrations).

### Notes

- To be able to add a trigger to a database in Amazon RDS, (for this operation you need to have privilege to create a
  database and create a parameter group):

  - Login Amazon
  - Type `RDS` and click
  - Click `Parameter Groups` on the left in `Navigation Bar`
  - Create a parameter group, choose a database version (`MariaDB 10.5`)
  - Click newly created Parameter Group and click `Modify`
  - Search for `log_bin_trust_function_creators` and set it to `1`
  - Then apply changes
  - Go to database from `Databases` from Navigation Bar
  - Choose database and then click `Modify`
  - Go to `Additional configuration` section
  - Change default parameter group to newly created parameter group
  - Wait for changes to being applied
  - Then reboot database from `Databases -> Actions -> Reboot`
  - Now triggers are allowed in the database

- To support Turkish characters on database please make sure that database supports utf8 and collates with
  utf8_general_ci
