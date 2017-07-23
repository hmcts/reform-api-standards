# spk-version-support-in-accept-header
checking best option to add Spring support for version in HTTP Accept header

# run
mvn test

# Problem description
... coming soon

# Some conclusions/solutions:

1.	Get accept header in one controller and run the private function needed for the specific version

       This is the ugliest one
```java
public String version(@RequestHeader(value = "Accept", required = false) String acceptHeader,
                      NativeWebRequest request) throws HttpMediaTypeNotAcceptableException {
                      …
                      // use match and switch on results to methods representing needed version
                      match(acceptHeader, request.getNativeRequest(HttpServletRequest.class))   
                      …
}

private String match(String acceptHeader, HttpServletRequest request) {
      logger.info(request.getRequestURI()+ " called with Accept:" + acceptHeader);
      Matcher matcher = pattern.matcher(acceptHeader);
      if (matcher.find())
             return matchingVersion(matcher.group(1), new String[] { "1.0.1", "1.3.1", "1.3.4", "2.5.0" });
      return "no match";
}

private String matchingVersion(String version, String[] listOfVersionsToMatchTo) {
      List<Version> toMatch = Arrays.asList(listOfVersionsToMatchTo).stream()
                                    .map(x -> Version.valueOf(x)).collect(Collectors.toList());

      for (Iterator<Version> it = toMatch.iterator(); it.hasNext(); )
        if (!it.next().satisfies(version))
            it.remove();

      logger.info("Satisfying list: "+ toMatch);

      return toMatch.toString();
}
```

2.	Pull Spring repo and add version capability to ```MimeType```, that includes adding ```getVersion()``` and change ```isCompatibleWith()``` -> this one is triggered/used from the dispatcher. Probably few other comparison methods should be updated to regard version. Now only charset and weight are considered as parameters

       I reckon this is preferred, enhancing spring and skipping all kind of ugly hacks

3.	Add custom annotation for version based method running

       Looks good, haven’t tried it yet but still looks like a bit of a hack to build support for this as custom annotations when this is a standard thing
https://www-stackoverflow-info.blogspot.co.uk/2016/02/how-to-manage-rest-api-versioning-with.html
https://stackoverflow.com/questions/20655614/request-mapping-using-headers-in-spring-mvc
