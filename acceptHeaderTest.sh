#!/bin/bash

# you can also run 'mvn test', should have all test suite there 

#Accept: application/vnd.uk.gov.hmcts.<micro-service-name>.<domain-object-name>.<...?>+json; version="^1.0.1"

curl -v -H "Content-Type:text/html" -H "Accept:application/vnd.uk.gov.hmcts.test+json;version=\"1.3.3\"" -i http://localhost:8080/versions;
printf "\n\n*****\n\n";

curl -v -H "Accept:application/vnd.uk.gov.hmcts.test+json;version=\"1.0.1\"" -H "Content-Type: text/html" http://localhost:8080/versions;
printf "\n\n*****\n\n";
