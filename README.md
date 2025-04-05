# micronaut-camunda-platform-7

[![Release](https://img.shields.io/github/v/release/camunda-community-hub/micronaut-camunda-platform-7.svg)](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/releases)
[![License](https://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Continuous Integration](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/workflows/Continuous%20Integration/badge.svg)](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/actions)
[![GitHub Discussions](https://img.shields.io/badge/Forum-GitHub_Discussions-blue)](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/discussions)

[![](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 7](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%207-26d07c)
[![](https://img.shields.io/badge/Lifecycle-Stable-brightgreen)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#stable-)

This open source project allows you to easily integrate [Camunda Platform 7](https://camunda.com/products/camunda-platform/) into [Micronaut](https://micronaut.io) projects.

The Micronaut Framework is known for its efficient use of resources. With this integration you embed the [BPMN 2.0](https://www.bpmn.org/) compliant and developer friendly Camunda process engine with minimal memory footprint into your application.

The integration is preconfigured with sensible defaults, so that you can get started with minimal configuration: simply add a dependency in your Micronaut project!

If you are interested in using Camunda's cloud native solution Zeebe on a Micronaut application instead, have a look at our open source project [micronaut-zeebe-client](https://github.com/camunda-community-hub/micronaut-zeebe-client).

---
_We're not aware of all installations of our Open Source project. However, we love_
* _listening to your feedback,_
* _discussing possible use cases with you,_
* _aligning the roadmap to your needs!_

📨 _Please [contact](#contact) us!_

---

Do you want to try it out? Please jump to the [Getting Started](#getting-started) section.

Do you want to contribute to our open source project? Please read the [Contribution Guidelines](CONTRIBUTING.md) and [contact us](#contact).

If you also want to run your External Task Client on a Micronaut application, have a look at the open source project [micronaut-camunda-external-client](https://github.com/camunda-community-hub/micronaut-camunda-external-client).

Micronaut Framework + Camunda = :heart:

# Table of Contents

* ✨ [Features](#features)
* 🚀 [Getting Started](#getting-started)
  * [Create Project with Micronaut Launch](#create-project-with-micronaut-launch)
  * [Deploying Models](#deploying-models)
  * [Camunda Integration](#camunda-integration)
  * [Configuration](#configuration)
  * [Examples](#examples)
  * [Supported JDKs](#supported-jdks)
* 🏆 [Advanced Topics](#advanced-topics)
  * [Dependency Management](#dependency-management)
  * [Camunda REST API and Webapps](#camunda-rest-api-and-webapps)
  * [Camunda Enterprise Edition (EE)](#camunda-enterprise-edition-ee)
  * [Process Engine Plugins](#process-engine-plugins)
  * [Custom Process Engine Configuration](#custom-process-engine-configuration)
  * [Custom Job Executor Configuration](#custom-job-executor-configuration)
  * [Transaction Management](#transaction-management)
  * [Performance](#performance)
  * [Architectural Design](#architectural-design)
  * [Keycloak](#keycloak)
  * [Eventing Bridge](#eventing-bridge)
  * [Process Tests](#process-tests)
  * [Docker](#docker)
  * [Updating Camunda](#updating-camunda)
  * [Pitfalls](#pitfalls)
* 📚 [Releases](#releases)
* 📆 [Publications](#publications)
* 📨 [Contact](#contact)

# ✨Features
* Camunda can be integrated as an embedded process engine into a Micronaut project by simply [adding a dependency](#dependency-management) in build.gradle (Gradle) or pom.xml (Maven).
* Using H2 as an in-memory database is as simple as [adding a dependency](#dependency-management). Other [data sources can be configured](#data-source) via properties.
* BPMN process models, DMN decision tables, and Camunda Forms are [automatically deployed](#deploying-models) for all configured locations.
* The Camunda process engine with its job executor is started automatically - but the job executor is disabled for tests by default.
* The process engine and related services, e.g. RuntimeService, RepositoryService, ..., are provided as lazy initialized beans and [can be injected](#camunda-integration).
* Micronaut beans are resolved from the application context if they are [referenced by expressions or Java class names](#java-delegates) within the process models.
* The process engine [integrates with Micronaut transaction manager](#transaction-management). Optionally, micronaut-data-jdbc or micronaut-data-jpa are supported.
* Eventing Bridge that maps Camunda Events to Micronaut ApplicationEvents.
* The process engine can be configured with [generic properties](#generic-properties).
* The [Camunda REST API and the Webapps](#camunda-rest-api-and-webapps) are supported (currently only for Jetty).
* The [Camunda Enterprise Edition (EE)](#camunda-enterprise-edition-ee) is supported. 
* [Process Engine Plugins](#process-engine-plugins) are automatically activated on start.
* The job executor uses the Micronaut IO Executor's [thread pools](https://docs.micronaut.io/latest/guide/index.html#threadPools).
* The [process engine configuration](#custom-process-engine-configuration) and the [job executor configuration](#custom-job-executor-configuration) can be customized programmatically.
* A Camunda admin user is created if configured by [properties](#properties) and not present yet (including admin group and authorizations).

# 🚀Getting Started

This section describes what needs to be done to use `micronaut-camunda-bpm-feature` in a Micronaut project.

## Create Project with Micronaut Launch

Create a new Micronaut project using [Micronaut Launch](https://micronaut.io/launch?name=micronaut-camunda&features=camunda-platform7) and check that the "camunda-platform7" feature is selected.

This will take care of the following:
* If you don't explicitly select any database then an in-memory H2 will be included by default.
* The configuration file `application.yml`
  * enables the Webapps and the REST-API
  * is configured to create an admin user with credentials `admin`/`admin` with which you can login to http://localhost:8080/camunda
* Jetty will be pre-configured (instead of Netty) to support the Webapps and REST-API by default

All you need to do is save a process model in the resources, see the following section.

##  Deploying Models
BPMN process models (`*.bpmn`), DMN decision tables (`*.dmn`), and Camunda Forms (`*.form`) should be created with the [Camunda Modeler](https://camunda.com/products/camunda-bpm/modeler) and saved in the resources.

By default, only the root of the resources will be scanned, but with the [property](#properties) `camunda.locations` you can configure the locations.

When starting the application you'll see the log output: `Deploying model: classpath:xxxxxxx.bpmn`

If you deploy Camunda [Forms](https://docs.camunda.org/manual/latest/reference/forms/camunda-forms/) then you can reference these from your user tasks by either
* defining the form as type "Camunda Form" and setting the "Form Ref", e.g. `ExampleForm` (Preferred solution supported by Camunda Modeller 4.11 and newer).
* defining the form as type "Embedded or External Task Forms" and setting the "Form Key", e.g. `camunda-forms:deployment:example.form` (Supported by Camunda Modeller 4.10 and newer).

## Camunda Integration

### Process Engine and Services

Inject the process engine or any of the Camunda services using constructor injection:
```java
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;

@Singleton
public class MyComponent {

    private final ProcessEngine processEngine;
    private final RuntimeService runtimeService;
    
    public MyComponent(ProcessEngine processEngine, RuntimeService runtimeService) {
        this.processEngine = processEngine;
        this.runtimeService = runtimeService;
    }

    // ...
}
```

Alternatively to constructor injection, you can also use field injection, Java bean property injection, or method parameter injection.

You can then for example use the `runtimeService` to start new processes instances or correlate existing process instances.

### Java Delegates

To invoke a Java delegate create a bean and reference it in your process model using an expression, e.g. `${loggerDelegate}`:

```java
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Singleton
public class LoggerDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(LoggerDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("Hello World: {}", delegateExecution);
    }
}
```

Internally, the bean will be resolved using `io.micronaut.inject.qualifiers.Qualifiers.byName(...)`.
Therefore, you can use the annotation `jakarta.inject.Named` to define an explicit bean name and use that name in your expression.

## Configuration

### Data Source

By default, an in-memory H2 data source is preconfigured. Remember to add the runtime dependency `com.h2database:h2` mentioned in [Dependency Management](#dependency-management).

However, you can configure any other database supported by Camunda, e.g. in `application.yml`:

```yaml
datasources:
  default:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: secret
    driver-class-name: org.postgresql.Driver
```

after adding the appropriate driver as a dependency:

```groovy
runtimeOnly("org.postgresql:postgresql:42.3.1")
```

### Connection Pool with HikariCP

This integration uses HikariCP as a database connection pool to optimize performance. By default, the following configuration is applied:
* `datasources.default.minimum-idle: 10`
* `datasources.default.maximum-pool-size: 50`

### Properties

You may use the following properties (typically in application.yml) to configure the Camunda integration.

| Prefix                |Property          | Default                                                                          | Description            |
|-----------------------|------------------|----------------------------------------------------------------------------------|------------------------|
| camunda               | .locations       | classpath:.                                                                      | List of locations to scan for model files (default is the resources's root only) |
| camunda.admin-user    | .id              |                                                                                  | If present, a Camunda admin account will be created by this id (including admin group and authorizations) |
|                       | .password        |                                                                                  | Admin's password (mandatory if the id is present)  |
|                       | .firstname       |                                                                                  | Admin's first name (optional, defaults to the capitalized id) |
|                       | .lastname        |                                                                                  | Admin's last name (optional, defaults to the capitalized id) |
|                       | .email           |                                                                                  | Admin's email address (optional, defaults to &lt;id&gt;@localhost) |
| camunda.rest          | .enabled         | false                                                                            | Enable the REST API |
|                       | .context-path    | /engine-rest                                                                     | Context path for the REST API |
|                       | .basic-auth-enabled | false                                                                            | Enables basic authentication for the REST API |
|                       | .authentication-provider | org.camunda.bpm.engine. rest.security.auth.impl. HttpBasicAuthenticationProvider | Authentication Provider to use for the REST API |
| camunda.webapps       | .enabled         | false                                                                            | Enable the Webapps (Cockpit, Task list, Admin) |
|                       | .context-path    | /camunda                                                                         | Context path for the Webapps |
|                       | .index-redirect-enabled | true                                                                             | Registers a redirect from / to the Webapps |
| camunda.filter        | .create          |                                                                                  | Name of a "show all" filter for the task list |
| camunda               | .license-file    |                                                                                  | Provide a URL to a license file; if no URL is present it will check your classpath for a file called "camunda-license.txt" |

### Generic Properties

The process engine can be configured using generic properties listed in Camunda's Documentation: [Configuration Properties](https://docs.camunda.org/manual/latest/reference/deployment-descriptors/tags/process-engine/#configuration-properties).

The properties can be set in kebab case (lowercase and hyphen separated) or camel case (indicating the separation of words with a single capitalized letter as written in Camunda's documentation). Kebab case is preferred when setting properties.

Some of the most relevant properties are:
* database-schema-update (databaseSchemaUpdate)
* history

Example:

```yaml
camunda:
  generic-properties:
    properties:
      history: audit
```

## Examples

Here are some example applications:
* [Onboarding Process](https://github.com/tobiasschaefer/micronaut-camunda-example-onboarding) with service tasks, user tasks, and message correlation.
* Simple [application with Java/Maven](https://github.com/tobiasschaefer/micronaut-camunda-example-java-maven)
* Simple [application with Kotlin/Gradle](https://github.com/tobiasschaefer/micronaut-camunda-example-kotlin-gradle)
* [Internal example application](/micronaut-camunda-bpm-example) used during development

## Supported JDKs

We officially support the following JDKs:
* JDK 8 (LTS) up to Camunda 7.19, i.e. with release [v2.15.0](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/releases/tag/v2.15.0)
* JDK 11 (LTS) up to Camunda 7.22, i.e. with release [v2.18.1](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/releases/tag/v2.18.1)
* JDK 17 (LTS)

# 🏆Advanced Topics

## Dependency Management

The Camunda integration works with both Gradle and Maven, but we recommend using Gradle because it has better Micronaut Support.

If you create a new project then simply use the feature `features=camunda-platform7` as described in [Getting Started](#getting-started) section. However, you can also manage the dependencies yourself as described here:

<details>
<summary>Click to show Gradle configuration</summary>

Add the dependency to the build.gradle file:
```groovy
implementation("info.novatec:micronaut-camunda-bpm-feature:2.19.0")
runtimeOnly("com.h2database:h2")
```
</details>

<details>
<summary>Click to show Maven configuration</summary>

Add the dependency to the pom.xml file:
```xml
<dependency>
  <groupId>info.novatec</groupId>
  <artifactId>micronaut-camunda-bpm-feature</artifactId>
  <version>2.19.o</version>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```
</details>

Note: The module `micronaut-camunda-bpm-feature` includes the dependency `org.camunda.bpm:camunda-engine` which will be resolved transitively.

## Camunda REST API and Webapps

Currently, the Camunda REST API and Webapps (Cockpit, Task list, and Admin) are only supported on the server runtime Jetty.

If you create your project with Micronaut Launch the `jetty` feature will be preselected for you.

However, if you have an existing project, you have to set the micronaut runtime of your project to `jetty`, e.g.

<details>
<summary>Click to show Gradle configuration</summary>

micronaut-gradle-plugin configuration in build.gradle:

```groovy
micronaut {
    runtime("jetty")
    [...]
}
```
</details>

<details>
<summary>Click to show Maven configuration</summary>

micronaut-maven-plugin configuration in pom.xml:

```xml
<properties>
  [...]
  <micronaut.runtime>jetty</micronaut.runtime>
</properties>
```

You have to remove this dependency in the pom.xml:
```xml
<dependency>
  <groupId>io.micronaut</groupId>
  <artifactId>micronaut-http-server-netty</artifactId>
  <scope>compile</scope>
</dependency>
```
and replace it with
```xml
<dependency>
  <groupId>io.micronaut.servlet</groupId>
  <artifactId>micronaut-http-server-jetty</artifactId>
</dependency>
```
</details>

### Configuration of REST API and Webapps
By default, REST API and the Webapps are not enabled. You have to configure them e.g. in the application.yaml as follows:

```yaml
camunda:
  webapps:
    enabled: true
  rest:
    enabled: true
```

Further Information:
* The Webapps are by default available at `/camunda`. By default, `/` will redirect you there.
* The REST API is by default available at `/engine-rest`, e.g. to get the engine name use `GET /engine-rest/engine`.
* See [Configuration Properties](#properties) on how to enable basic authentication for REST, create a default user, or disable the redirect.
* Enabling the REST API or the Webapps impacts the startup time. Depending on your hardware it increases by around 500-1000 milliseconds.

### Advanced Webapps Configuration
The security of the Webapps can be configured with the following properties:
<details>
  <summary>Click to show configuration options.</summary>

| Prefix                | Property          | Default                                      | Description            |
|-----------------------|------------------|----------------------------------------------|------------------------|
| camunda.webapps.header-security | .xss-protection-disabled | false | The header can be entirely disabled if set to true. |
|                       | .xss-protection-option| BLOCK | The allowed set of values: BLOCK - If the browser detects a cross-site scripting attack, the page is blocked completely; SANITIZE - If the browser detects a cross-site scripting attack, the page is sanitized from suspicious parts (value 0). Note: Is ignored when xss-protection-disabled is set to true and cannot be set in conjunction with xss-protection-value |
|                       | .xss-protection-value| 1; mode=block | A custom value for the header can be specified. Is ignored when xss-protection-disabled is set to true and cannot be set in conjunction with xss-protection-option. |
|                       | .content-security-policy-disabled | false| The header can be entirely disabled if set to true. |
|                       | .content-security-policy-value | base-uri 'self' | A custom value for the header can be specified. Note: Property is ignored when content-security-policy-disabled is set to true. |
|                       | .content-type-options-disabled | false | The header can be entirely disabled if set to true. |
|                       | .content-type-options-value | | A custom value for the header can be specified. Note: Property is ignored when content-security-policy-disabled is set to true. |
|                       | .hsts-disabled | true | Set to false to enable the header. |
|                       | .hsts-max-age | 31536000 | Amount of seconds, the browser should remember to access the webapp via HTTPS. Note: Is ignored when hstsDisabled is true, Cannot be set in conjunction with hstsValue, and allows a maximum value of 2^31-1. |
|                       | .hsts-include-subdomains-disabled | true | HSTS is additionally to the domain of the webapp enabled for all its subdomains. Note: Is ignored when hstsDisabled is true and cannot be set in conjunction with hstsValue. |
|                       | .hsts-value | max-age=31536000 | A custom value for the header can be specified. Note: Is ignored when hstsDisabled is true and cannot be set in conjunction with hstsMaxAge or hstsIncludeSubdomainsDisabled. |
| camunda.webapps.csrf  | .target-origin | | Sets the application expected deployment domain. |
|                       | .deny-status | | Sets the HTTP response status code used for a denied request. |
|                       | .random-class | | Sets the name of the class used to generate tokens. |
|                       | .entry-points | | Sets additional URLs that will not be tested for the presence of a valid token. |
|                       | .enable-secure-cookie | false | If true, the cookie flag Secure is enabled. |
|                       | .enable-same-site-cookie | true | If set to false, the cookie flag SameSite is disabled. The default value of the cookie is LAX and it can be changed via same-site-cookie-option configuration property. |
|                       | .same-site-cookie-option | | Can be configured either to STRICT or LAX. Note: Is ignored when enable-same-site-cookie is set to false and cannot be set in conjunction with same-site-cookie-value. |
|                       | .same-site-cookie-value | | A custom value for the cookie property. Note: Is ignored when enable-same-site-cookie is set to false and cannot be set in conjunction with same-site-cookie-option. |
|                       | .cookie-name | | A custom value to change the cookie name. Default ist 'XSRF-Token'. Note: Please make sure to additionally change the cookie name for each webapp (e.g. Cockpit) separately. |

</details>

## Camunda Enterprise Edition (EE)
### Add Maven Coordinates

To use the Camunda Enterprise Edition you have to add the Camunda Enterprise repository:

<details>
<summary>Click to show Gradle configuration</summary>

In `build.gradle`:
```groovy
repositories {
    maven {
        url "https://artifacts.camunda.com/artifactory/camunda-bpm-ee/"
        credentials(PasswordCredentials) {
            username "YOUR_USERNAME"
            password "YOUR_PASSWORD"
        }
    }
}
```
</details>
<details>
<summary>Click to show Maven configuration</summary>

In `pom.xml`:
```xml
<repositories>
  <repository>
    <id>camunda-bpm-nexus-ee</id>
    <name>camunda-bpm-nexus</name>
    <url>
      https://artifacts.camunda.com/artifactory/camunda-bpm-ee/
    </url>
  </repository>
</repositories>
```

Furthermore, you have to add your credentials in `~/.m2/settings.xml`:
```xml
<servers>
  <server>
    <id>camunda-bpm-nexus-ee</id>
    <username>YOUR_USERNAME</username>
    <password>YOUR_PASSWORD</password>
  </server>
</servers>
```
</details>

### Replace CE with EE Dependencies
Then remove the CE dependencies and replace them with the EE ones. Here are some example snippets 
on how to do that. Keep in mind using the correct version of the libraries.
<details>
<summary>Click to show Gradle configuration</summary>

In `build.gradle`:
```groovy
implementation("info.novatec:micronaut-camunda-bpm-feature:2.19.0") {
    exclude group: 'org.camunda.bpm.webapp', module: 'camunda-webapp-webjar'
    exclude group: 'org.camunda.bpm', module: 'camunda-engine'
}

implementation("org.camunda.bpm.webapp:camunda-webapp-webjar-ee:7.23.0-ee")
implementation("org.camunda.bpm:camunda-engine:7.23.0-ee")
```
</details>
<details>
<summary>Click to show Maven configuration</summary>

In `pom.xml`:
```xml
<dependency>
  <groupId>info.novatec</groupId>
  <artifactId>micronaut-camunda-bpm-feature</artifactId>
  <version>2.19.0</version>
  <exclusions>
    <exclusion>
      <groupId>org.camunda.bpm.webapp</groupId>
      <artifactId>camunda-webapp-webjar</artifactId>
    </exclusion>
    <exclusion>
      <groupId>org.camunda.bpm</groupId>
      <artifactId>camunda-engine</artifactId>
    </exclusion>
  </exclusions>
</dependency>
<dependency>
  <groupId>org.camunda.bpm.webapp</groupId>
  <artifactId>camunda-webapp-webjar-ee</artifactId>
  <version>7.23.0-ee</version>
</dependency>
<dependency>
  <groupId>org.camunda.bpm</groupId>
  <artifactId>camunda-engine</artifactId>
  <version>7.23.0-ee</version>
</dependency>
```
</details>

### Configure your EE license

Camunda will use the license configured in the Cockpit. 

If no license is registered, then the following locations will be checked during startup to register the license:

1. The URL referenced by the property `camunda.license-file`
2. The file `camunda-license.txt` in the resource's root if the property `camunda.license-file` has an empty value
3. The path `.camunda/license.txt` in the user's home directory

If you want to update your license key, use the Camunda Cockpit.

## Process Engine Plugins
Every bean that implements the interface `org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin` is automatically added to the process engine's configuration on start.

You can either
* implement a bean factory with `@io.micronaut.context.annotation.Factory` and add one or more methods returning `ProcessEnginePlugin` instances and annotate each with a bean scope annotation
* annotate your class with `@jakarta.inject.Singleton` and implement the `ProcessEnginePlugin` interface

Example with the LDAP plugin:

```groovy
implementation("org.camunda.bpm.identity:camunda-identity-ldap:7.23.0")
```

```java
import io.micronaut.context.annotation.Factory;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin;
import jakarta.inject.Singleton;

@Factory
public class PluginConfiguration {

    @Singleton
    public ProcessEnginePlugin ldap() {
        // Using a public online LDAP:
        // https://www.forumsys.com/tutorials/integration-how-to/ldap/online-ldap-test-server/
        LdapIdentityProviderPlugin ldap = new LdapIdentityProviderPlugin();
        ldap.setServerUrl("ldap://ldap.forumsys.com:389");
        ldap.setManagerDn("cn=read-only-admin,dc=example,dc=com");
        ldap.setManagerPassword("password");
        ldap.setBaseDn("dc=example,dc=com");
        return ldap;
    }
}
```

You can now log in with "einstein" / "password". Note: the automatic creation of the admin user with the property `camunda.admin-user` conflicts with a read-only LDAP and must not be set!

## Custom Process Engine Configuration
With the following bean it's possible to customize the process engine configuration:

```java
import info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration;
import info.novatec.micronaut.camunda.bpm.feature.ProcessEngineConfigurationCustomizer;
import io.micronaut.context.annotation.Replaces;
import jakarta.inject.Singleton;

@Singleton
@Replaces(ProcessEngineConfigurationCustomizer.class)
public class MyProcessEngineConfigurationCustomizer implements ProcessEngineConfigurationCustomizer {
    @Override
    public void customize(MnProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setProcessEngineName("CustomizedEngine");
    }
}
```

## Custom Job Executor Configuration
With the following bean it's possible to customize the job executor:

```java
import info.novatec.micronaut.camunda.bpm.feature.JobExecutorCustomizer;
import info.novatec.micronaut.camunda.bpm.feature.MnJobExecutor;
import io.micronaut.context.annotation.Replaces;
import jakarta.inject.Singleton;

@Singleton
@Replaces(JobExecutorCustomizer.class)
public class MyJobExecutorCustomizer implements JobExecutorCustomizer {
    @Override
    public void customize(MnJobExecutor jobExecutor) {
        jobExecutor.setWaitTimeInMillis(300);
    }
}
```

## Transaction management

By default the process engine integrates with Micronaut's transaction manager and uses a Hikari connection pool:
* When interacting with the process engine, e.g. starting or continuing a process, the existing transaction will be propagated.
* JavaDelegates and Listeners will have the surrounding Camunda transaction propagated to them allowing the atomic persistence of data.

Optionally, `micronaut-data-jdbc` or `micronaut-data-jpa` are supported.

### Using micronaut-data-jdbc

To enable embedded transactions management support **with micronaut-data-jdbc** please add the following dependencies to your project:

<details>
<summary>Click to show Gradle dependencies</summary>

```groovy
annotationProcessor("io.micronaut.data:micronaut-data-processor")
implementation("io.micronaut.data:micronaut-data-jdbc")
```
</details>

<details>
<summary>Click to show Maven dependencies</summary>

```xml
<dependency>
  <groupId>io.micronaut.data</groupId>
  <artifactId>micronaut-data-jdbc</artifactId>
</dependency>
```

And also add the annotation processor to every (!) `annotationProcessorPaths` element:

```xml
<path>
  <groupId>io.micronaut.data</groupId>
  <artifactId>micronaut-data-processor</artifactId>
  <version>${micronaut.data.version}</version>
</path>
```
</details>

and then configure the JDBC properties as described [micronaut-sql documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/#jdbc).

### Using micronaut-data-jpa

To enable embedded transactions management support **with micronaut-data-jpa** please add the following dependencies to your project:

<details>
<summary>Click to show Gradle dependencies</summary>

```groovy
annotationProcessor("io.micronaut.data:micronaut-data-processor")
implementation("io.micronaut.data:micronaut-hibernate-jpa")
```
</details>

<details>
<summary>Click to show Maven dependencies</summary>

```xml
<dependency>
  <groupId>io.micronaut.data</groupId>
  <artifactId>micronaut-data-hibernate-jpa</artifactId>
</dependency>
```

And also add the annotation processor to every (!) `annotationProcessorPaths` element:

```xml
<path>
  <groupId>io.micronaut.data</groupId>
  <artifactId>micronaut-data-processor</artifactId>
  <version>${micronaut.data.version}</version>
</path>
```
</details>

and then configure JPA as described in [micronaut-sql documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/#hibernate).

## Performance

The Time to First Response (TTFR) is mainly influenced by the (slow) bootstrapping of the process engine - the bottleneck is the sequential parsing of over 50 MyBatis mappings. But the REST api and WebApps also take time.

This Micronaut Camunda Integration includes some optimizations that come into play especially in multi-core environments.

Some hints:
* Newer JDKs (JDK 17) are about 20% faster than older ones (JDK 8)
* More CPU cores are better to take advantage of parallelization during startup (so don't limit Docker to one or two CPUs).
* The selected vendor for the JDK has a small influence (Java SE Development Kit from Oracle is one of the faster ones) 

Most discussions regarding startup time discuss the relevance during deployment or scaling up applications.

However, there is more: for a developer startup times are also relevant when test suites are executed - either locally or in a CI environment. If the application context is created often (e.g. @MockBean dirties the context in Spring Boot...) then integration tests run quite long.

If unit and integration tests are more of a conceptional difference (and not so much regarding performance) then the developer has more freedom of choice to decide if a unit or integration test is more appropriate for his scenario.

The documentation of [Micronaut Test](https://micronaut-projects.github.io/micronaut-test/latest/guide/) actually says: "One of the design goals of Micronaut was to eliminate the artificial separation imposed by traditional frameworks between function and unit tests due to slow startup times and memory consumption."

## Architectural Design

### Separating Process Engine from Webapps/REST

If you want to activate the Webapps and/or REST it might be an option to do this in a separate application. Both are connected via a common database.

Possible aspects:
* Your main process engine can run on Netty while the Webapps and/or REST run on Jetty (with a disabled Job Executor).
* You can scale these applications independently.

### Embedding External Workers

If you're intending to use the [External Task Pattern](https://docs.camunda.org/manual/latest/user-guide/process-engine/external-tasks/#the-external-task-pattern) it might be an option embedding them (at first) in your main application - and having them to communicate via REST on localhost.

Possible aspects:
* Having a lot of separate applications for each external worker from the beginning increases the complexity. You can put all external workers in one application - or even in the main application which provides the process engine.
* If you decide later to extract single modules to a separate microservice then this is straight forward

Here is an example application: https://github.com/tobiasschaefer/micronaut-embedded-worker

## Keycloak

You can enable the [Keycloak](https://www.keycloak.org/) integration to
* log into the Webapps (Tasklist, Cockpit, and Admin)
* get responses from the REST API with basic-auth enabled

1. Start Keycloak, e.g. `docker run -p 8080:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -e DB_VENDOR="h2" quay.io/keycloak/keycloak:12.0.4`
2. Configure Keycloak and add a test user.
3. Add the dependency `implementation("org.camunda.bpm.extension:camunda-bpm-identity-keycloak:2.2.3")` to your Micronaut project
4. Add the plugin:
```java
@Singleton
@ConfigurationProperties("plugin.identity.keycloak")
public class KeyCloakPlugin extends KeycloakIdentityProviderPlugin {
}
```
4. Configure the application.yml
```yaml
plugin.identity.keycloak:
  keycloakIssuerUrl: http://localhost:8080/auth/realms/master
  keycloakAdminUrl: http://localhost:8080/auth/admin/realms/master
  clientId: camunda-identity-service
  clientSecret: 42aa42bb-1234-4242-a24a-42a2b420cde1 # you get this from keycloak
  useEmailAsCamundaUserId: true
  administratorGroupName: camunda-admin
```
5. Start the application and log in with your created test user. Keep in mind that your user needs an e-mail address.

## Eventing Bridge

The Eventing Bridge maps Camunda Events to Micronaut [ApplicationEvents](https://docs.micronaut.io/latest/api/io/micronaut/context/event/ApplicationEvent.html). It's possible to configure three different
event streams:
* Task: All events depending on UserTasks (UserTasks are Created, Assigned, Completed)
* Execution: All execution events (Activities are Started, Ended and Transitions are being taken)
* History: All history events

### Configuration of Eventing Bridge
```yaml
camunda:
  eventing:
    task: true
    execution: true
    history: true
```

### Event Listener Implementation
To consume Micronaut ApplicationEvents you can implement the interface ApplicationEventListener or use the
@EventListener annotation.

<details>
<summary>Click to show example with ApplicationEventListener interface</summary>

```java
public class SampleEventListener implements ApplicationEventListener<TaskEvent> {
  private static final Logger log = LoggerFactory.getLogger(SampleEventListener.class);

  @Override
  public void onApplicationEvent(TaskEvent event) {
    log.info("new TaskEvent: EventName={}, Assignee={}", event.getEventName(), event.getAssignee());
  }
}
```
</details>

<details>
<summary>Click to show example with @EventListener</summary>

```java
@Singleton
public class SampleEventListener { 
  private static final Logger log = LoggerFactory.getLogger(SampleEventListener.class);

  @EventListener
  public void onExecutionEvent(ExecutionEvent event) {
    log.info("new ExecutionEvent: {}", event.getEventName());
  }

  @EventListener
  public void onTaskEvent(TaskEvent event) {
    log.info("new TaskEvent: {}", event.getEventName());
  }

  @EventListener
  public void onTaskEvent(HistoryEvent event) {
    log.info("new HistoryEvent: {}", event.getEventType());
  }
}
```
</details>

## Process Tests

Process tests can easily be implemented with JUnit 5 by adding the `camunda-bpm-assert` library as a dependency:

<details>
<summary>Click to show Gradle dependencies</summary>

```groovy
testImplementation("org.camunda.bpm:camunda-bpm-assert:7.23.0")
testImplementation("org.assertj:assertj-core")
```
</details>

<details>
<summary>Click to show Maven dependencies</summary>

```xml
<dependency>
  <groupId>org.camunda.bpm</groupId>
  <artifactId>camunda-bpm-assert</artifactId>
  <version>7.23.0</version>
  <scope>test</scope>
</dependency>
<dependency>
<groupId>org.assertj</groupId>
  <artifactId>assertj-core</artifactId>
  <version>3.21.0</version>
  <scope>test</scope>
</dependency>
```
</details>

and then implement the test using the usual `@MicronautTest` annotation:

```java
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@MicronautTest
class HelloWorldProcessTest {

    @Inject
    ProcessEngine processEngine;

    @Inject
    RuntimeService runtimeService;

    @BeforeEach
    void setUp() {
        init(processEngine);
    }

    @Test
    void happyPath() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWorld");
        assertThat(processInstance).isStarted();

        assertThat(processInstance).isWaitingAt("TimerEvent_Wait");
        execute(job());

        assertThat(processInstance).isEnded();
    }
}
```

See also a test in our example application: [HelloWorldProcessTest](/micronaut-camunda-bpm-example/src/test/java/info/novatec/micronaut/camunda/bpm/example/HelloWorldProcessTest.java)

## Docker

When using Gradle we recommend the [Micronaut Application Plugin](https://github.com/micronaut-projects/micronaut-gradle-plugin/blob/master/README.md#micronaut-application-plugin)'s `dockerBuild` task to create a layered Docker image.

Build the Docker image:

`./gradlew dockerBuild`

Run the Docker image:

`docker run -p 8080:8080 <IMAGE>`

## Updating Camunda

Generally, follow Camunda's instructions regarding [Update to the next Minor Version](https://docs.camunda.org/manual/latest/update/minor/).

If you want to automate the database schema migration you can use [Liquibase](https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/) or [Flyway](https://micronaut-projects.github.io/micronaut-flyway/latest/guide/) together with the [migration sql scripts](https://artifacts.camunda.com/artifactory/public/org/camunda/bpm/distro/camunda-sql-scripts/) provided by Camunda.

The following examples are based on Liquibase.

When starting on an empty database, e.g. when using H2 for tests:
```xml
<changeSet author="Tobias" id="1a" >
  <comment>Create common baseline Camunda 7.14 for H2 based on https://artifacts.camunda.com/artifactory/public/org/camunda/bpm/distro/camunda-sql-scripts/7.14.0/camunda-sql-scripts-7.14.0.zip in directory create</comment>
  <sqlFile path="camunda/h2_engine_7.14.0.sql" relativeToChangelogFile="true" dbms="h2" />
  <sqlFile path="camunda/h2_identity_7.14.0.sql" relativeToChangelogFile="true" dbms="h2" />
</changeSet>
```

If you already have a persistent database with the database schema of 7.14 which is not yet managed by Liquibase, e.g. PostgreSQL:
```xml
<changeSet author="Tobias" id="1b" >
  <comment>Create common baseline Camunda 7.14 for PostgreSQL (even if schema already exists) based on https://artifacts.camunda.com/artifactory/public/org/camunda/bpm/distro/camunda-sql-scripts/7.14.0/camunda-sql-scripts-7.14.0.zip in directory create</comment>
  <preConditions onFail="MARK_RAN">
    <not>
      <tableExists tableName="ACT_RU_JOB" />
    </not>
  </preConditions>
  <sqlFile path="camunda/postgres_engine_7.14.0.sql" relativeToChangelogFile="true" dbms="postgresql" />
  <sqlFile path="camunda/postgres_identity_7.14.0.sql" relativeToChangelogFile="true" dbms="postgresql" />
</changeSet>
```

When updating to a new Camunda version first apply all patch updates (if available) and then update to the next minor version:
```xml
<changeSet author="Tobias" id="2" >
  <comment>Update to Camunda 7.15 based on https://artifacts.camunda.com/artifactory/public/org/camunda/bpm/distro/camunda-sql-scripts/7.15.0/camunda-sql-scripts-7.15.0.zip in directory upgrade</comment>
  <!-- no patch files available for 7.14.x ... -->
  <sqlFile path="camunda/h2_engine_7.14_to_7.15.sql" relativeToChangelogFile="true" dbms="h2" />
  <sqlFile path="camunda/postgres_engine_7.14_to_7.15.sql" relativeToChangelogFile="true" dbms="postgresql" />
</changeSet>
```

## Pitfalls

### No version information in Fat/Uber/Shadow JAR

If you create a Fat/Uber/Shadow JAR and run that you will see a warning:

`WARN  i.n.m.c.b.f.MnProcessEngineConfiguration - The Camunda version cannot be determined. If you created a Fat/Uber/Shadow JAR then please consider using the Micronaut Application Plugin's 'dockerBuild' task to create a Docker image.`

This is because the repackaging of the jars implicitly removes the META-INF information.

Without version information the EE license cannot be configured.

Instead, of creating a Fat/Uber/Shadow JAR, please see instructions on creating a [Docker](#docker) image and use the resulting image to run a Docker container.

### Executing Blocking Operations on Netty's I/O Thread Pool
When using the default server implementation Netty, blocking operations must be performed on I/O instead of Netty threads to avoid possible deadlocks. Therefore, as soon as Camunda ["borrows a client thread"](https://docs.camunda.org/manual/current/user-guide/process-engine/transactions-in-processes/)  you have to make sure that the [event loop is not blocked](https://objectcomputing.com/resources/publications/sett/june-2020-micronaut-2-dont-let-event-loops-own-you).
A frequently occurring example is the implementation of a REST endpoint which interacts with the process engine. By default, Micronaut would use a Netty thread for this blocking operation. To prevent the use of a Netty thread it is recommended to use the annotation [`@ExecuteOn(TaskExecutors.IO)`](https://docs.micronaut.io/latest/guide/index.html#reactiveServer). This will make sure that an I/O thread is used.

```java
@Post("/hello-world-process")
@ExecuteOn(TaskExecutors.IO)
public String startHelloWorldProcess() {
    return runtimeService.startProcessInstanceByKey("HelloWorld").getId();
}
```

### Camunda Platform Assertions - Multiple process tests
If you create multiple process tests, you need to add the following initialisation code in each test:

```java
@Inject
ProcessEngine processEngine;

@BeforeEach
void setUp() {
    init(processEngine);
}
```

This makes the assertions aware of your process engine. Otherwise, it tries to reuse the engine of the test that got
executed first and that may already be shut down, see Camunda Platform 7's [Assert User Guide](https://github.com/camunda/camunda-bpm-assert/blob/master/docs/README.md#using-a-non-default-process-engine).

Here is a complete example: [HelloWorldProcessTest](/micronaut-camunda-bpm-example/src/test/java/info/novatec/micronaut/camunda/bpm/example/HelloWorldProcessTest.java).

# 📚Releases

The list of [releases](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/releases) contains a detailed changelog.

We use [Semantic Versioning](https://semver.org/).

The following compatibility matrix shows the officially supported Micronaut and Camunda versions for each release.
Other combinations might also work but have not been tested.

| Release | Micronaut | Camunda |
|---------|-----------|---------|
| 2.19.0  | 3.9.4     | 7.23.0  |

<details>
<summary>Click to see older releases</summary>

| Release | Micronaut | Camunda |
|--------|-----------|--------|
| 2.18.0  | 3.9.4     | 7.22.0  |
| 2.17.0  | 3.9.4     | 7.21.0  |
| 2.16.0  | 3.9.4     | 7.20.0  |
| 2.15.0  | 3.9.4     | 7.19.0  |
| 2.14.0  | 3.9.0     | 7.19.0  |
| 2.13.0  | 3.8.9     | 7.19.0  |
| 2.12.0  | 3.8.0     | 7.18.0  |
| 2.11.1  | 3.7.3     | 7.18.0  |
| 2.11.0  | 3.7.3     | 7.18.0  |
| 2.10.0  | 3.7.1     | 7.18.0  |
| 2.9.0   | 3.6.1     | 7.17.0  |
| 2.8.0   | 3.5.2     | 7.17.0  |
| 2.7.2   | 3.4.4     | 7.17.0  |
| 2.7.1   | 3.4.4     | 7.17.0  |
| 2.7.0   | 3.4.1     | 7.17.0  |
| 2.6.0   | 3.4.0     | 7.16.0 |
| 2.5.0   | 3.3.3     | 7.16.0 |
|  2.4.1 | 3.3.3     | 7.16.0 |
|  2.4.0 | 3.3.0     | 7.16.0 |
|  2.3.2 | 3.2.0     | 7.16.0 |
|  2.3.1 | 3.2.0     | 7.16.0 |
|  2.3.0 | 3.2.0     | 7.16.0 |
|  2.2.0 | 3.1.1     | 7.16.0 |
|  2.1.0 | 3.1.0     | 7.16.0 |
|  2.0.0 | 3.0.0     | 7.15.0 |
|  1.1.0 | 2.5.12    | 7.15.0 |
|  1.0.1 | 2.5.12    | 7.15.0 |
|  1.0.0 | 2.5.9     | 7.15.0 |
| 0.24.0 | 2.5.1     | 7.15.0 |
| 0.23.0 | 2.4.3     | 7.15.0 |
| 0.22.0 | 2.4.1     | 7.14.0 |
| 0.21.0 | 2.4.1     | 7.14.0 |
| 0.20.0 | 2.4.0     | 7.14.0 |
| 0.19.0 | 2.3.4     | 7.14.0 |
| 0.18.1 | 2.3.3     | 7.14.0 |
| 0.18.0 | 2.3.2     | 7.14.0 |
| 0.17.0 | 2.3.2     | 7.14.0 |
| 0.16.1 | 2.3.1     | 7.14.0 |
| 0.16.0 | 2.3.0     | 7.14.0 |
| 0.15.0 | 2.3.0     | 7.14.0 |
| 0.14.0 | 2.2.3     | 7.14.0 |
| 0.13.0 | 2.2.2     | 7.14.0 |
| 0.12.0 | 2.2.1     | 7.14.0 |
| 0.11.0 | 2.2.1     | 7.14.0 |
| 0.10.1 | 2.2.0     | 7.14.0 |
| 0.10.0 | 2.2.0     | 7.14.0 |
| 0.9.0 | 2.1.3     | 7.14.0 |
| 0.8.0 | 2.1.2     | 7.13.0 |
| 0.7.0 | 2.1.1     | 7.13.0 |
| 0.6.0 | 2.1.0     | 7.13.0 |
| 0.5.3 | 2.0.1     | 7.13.0 |
| 0.5.2 | 2.0.0     | 7.13.0 |
| 0.5.1 | 2.0.0     | 7.13.0 |
| 0.5.0 | 2.0.0     | 7.13.0 |
| 0.4.2 | 1.3.6     | 7.13.0 |
| 0.3.1 | 1.3.5     | 7.12.0 |
| 0.2.2 | 1.3.3     | 7.12.0 |
| 0.2.1 | 1.3.3     | 7.12.0 |
| 0.2.0 | 1.3.3     | 7.12.0 |
| 0.1.0 | 1.3.3     | 7.12.0 |
</details>



Download of Releases:
* [GitHub Artifacts](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/releases)
* [Maven Central Artifacts](https://search.maven.org/artifact/info.novatec/micronaut-camunda-bpm-feature)

# 📆Publications

* 2021-11: [Automating Processes with Camunda and Micronaut](https://www.youtube.com/watch?app=desktop&v=PwgrAb2z0YU&t=17m45s))  
  Recording of the Novatec Summit by Tobias Schäfer (45 Minutes)
* 2021-10: [Camunda Question Corner](https://www.youtube.com/watch?v=o2-sgtXGIls&t=320s)  
  Recording of the Community Contribution Special with Niall Deehan and Tobias Schäfer (10 minutes)
* 2021-07: [Automate any Process on Micronaut](https://camunda.com/blog/2021/07/automate-any-process-on-micronaut/)  
  Blogpost by Tobias Schäfer
* 2021-02: [Automating Processes with Microservices on Micronaut and Camunda](https://micronaut.io/2021/02/25/webinar-micronaut-and-camunda/)  
  Webinar by Tobias Schäfer, Bernd Rücker, and Sergio del Amo
* 2020-04: [Micronaut meets Camunda BPM](https://www.novatec-gmbh.de/en/blog/micronaut-meets-camunda-bpm/)  
  Blogpost by Tobias Schäfer

# 📨Contact

If you have any questions or ideas feel free to create an [issue](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/issues) or contact us via [GitHub Discussions](https://github.com/camunda-community-hub/micronaut-camunda-platform-7/discussions).

We love listening to your feedback, and of course also discussing the project roadmap and possible use cases with you!

This open source project is being developed by [envite consulting GmbH](https://envite.de/) and [Novatec Consulting GmbH](https://www.novatec-gmbh.de/en/) with the support of the open source community.

---
![envite consulting GmbH](envite-black.png#gh-light-mode-only)
![envite consulting GmbH](envite-white.png#gh-dark-mode-only)
---
![Novatec Consulting GmbH](novatec.jpeg)
---
