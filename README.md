# event-store-commons
Defines a common event store Java interface and provides some adapters (like for Greg Young's [event store](https://www.geteventstore.com/)) and implementations (like in-memory or file-based).

[![Build Status](https://fuin-org.ci.cloudbees.com/job/event-store-commons/badge/icon)](https://fuin-org.ci.cloudbees.com/job/event-store-commons/)
[![Coverage Status](https://coveralls.io/repos/fuinorg/event-store-commons/badge.svg)](https://coveralls.io/r/fuinorg/event-store-commons)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.fuin.esc/esc-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.fuin.esc/esc-parent/)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 1.8](https://img.shields.io/badge/JDK-1.8-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

*Caution*: The code coverage value is not correct (it's actually higher than above value) as the 'test' module is not considered correctly (See [Issue #4](https://github.com//fuinorg/event-store-commons/issues/4))


## Status
![Warning](https://raw.githubusercontent.com/fuinorg/event-store-commons/master/doc/warning.gif) **This is work in progress** ![Warning](https://raw.githubusercontent.com/fuinorg/event-store-commons/master/doc/warning.gif)

| Module | Description | Status | Comment |
|:-------|:------------|--------|:--------|
| [esc-api](api) | Defines the event store commons API. | ![OK](https://raw.githubusercontent.com/fuinorg/event-store-commons/master/doc/ok.png) | Test coverage ~92% |
| [esc-http](eshttp) | HTTP adapter for Greg Young's [event store](https://www.geteventstore.com/)| ![OK](https://raw.githubusercontent.com/fuinorg/event-store-commons/master/doc/ok.png) | Test coverage ~66% |
| [esc-esjc](esjc) | [Event Store Java Client](https://github.com/msemys/esjc) adapter for Greg Young's [event store](https://www.geteventstore.com/)| ![OK](https://raw.githubusercontent.com/fuinorg/event-store-commons/master/doc/ok.png) | Test coverage ~80% |
| [esc-jpa](jpa) | JPA adapter | ![OK](https://raw.githubusercontent.com/fuinorg/event-store-commons/master/doc/ok.png) | Test coverage ~59% |
| [esc-mem](mem) | In-memory implementation | ![OK](https://raw.githubusercontent.com/fuinorg/event-store-commons/master/doc/ok.png) | Test coverage ~60% |
| [esc-spi](spi) | Helper classes for adapters and implementations | ![OK](https://raw.githubusercontent.com/fuinorg/event-store-commons/master/doc/ok.png) | Test coverage ~67% |
| [esc-test](test) | Cucumber tests for adapters and implementations | ![OK](https://raw.githubusercontent.com/fuinorg/event-store-commons/master/doc/ok.png) | Subscriptions not tested yet |

## Architecture
![Layers](https://raw.github.com/fuinorg/event-store-commons/master/doc/event-store-commons.png)

## Examples
- [Simple in-memory example](test/src/test/java/org/fuin/esc/test/examples/InMemoryExample.java)
- [Event store with HTTP interface and XML](test/src/test/java/org/fuin/esc/test/examples/EsHttpXmlExample.java)
- [Event store with HTTP interface and JSON](test/src/test/java/org/fuin/esc/test/examples/EsHttpJsonExample.java)
- [Event store with HTTP interface and mixed JSON/XML content](test/src/test/java/org/fuin/esc/test/examples/EsHttpMixedExample.java)

### Snapshots

Snapshots can be found on the [OSS Sonatype Snapshots Repository](https://oss.sonatype.org/content/repositories/snapshots/org/fuin/esc/ "Snapshot Repository"). 

Add the following to your [.m2/settings.xml](http://maven.apache.org/ref/3.2.1/maven-settings/settings.html "Reference configuration") to enable snapshots in your Maven build:

```xml
<repository>
    <id>sonatype.oss.snapshots</id>
    <name>Sonatype OSS Snapshot Repository</name>
    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
