# Reform API standards

[![Build Status](https://travis-ci.org/hmcts/reform-api-standards.svg?branch=master)](https://travis-ci.org/hmcts/reform-api-standards)
[![Download](https://api.bintray.com/packages/hmcts/hmcts-maven/reform-api-standards/images/download.svg)](https://bintray.com/hmcts/hmcts-maven/reform-api-standards/_latestVersion)

This library contains a set of Spring Boot components used across HMCTS APIs.

## SensitiveHeadersRequestTraceFilter
Removes sensitive headers before they are added to Spring trace.  
In order to use this filter declare the following Bean in your app:

```java
@Bean
public SensitiveHeadersRequestTraceFilter requestTraceFilter(Set<Include> includes) {
    return new SensitiveHeadersRequestTraceFilter(includes, <your_custom_headers_go_here>);
}
```

## @APIDeprecated
An annotation for controller classes and methods that adds warning headers to the responses generated by the API.

A typical usage would be to add the `@APIDeprecated` annotation to a method as in the following example:

```java
@APIDeprecated(
    name = "Deprecated Endpoint",
    expiryDate = "2018-06-30",
    docLink = "https://example.org/docs/foo",
    note = "Some note."
)
public Model someAction() {
    // request handling code ...
}
```
The annotation has the attributes that are used for forming a `Warning` header. 
- `name` for friendly name of the endpoint, 
- `expiryDate` for the date which the endpoint will cease to serve, 
- `docLink` for the details of documentation regarding the API updates
- `note` for optional notes to the clients

A sample of the Warning header is as follows:

>The UserProfileEndpoint is deprecated and will be removed by 2018-06-30. Please see https://example.org/docs/foo for details. Some note.
 
