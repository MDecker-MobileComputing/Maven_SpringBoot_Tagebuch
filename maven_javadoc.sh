#!/bin/bash

./mvnw clean javadoc:javadoc -Dshow=private -DadditionalJOption=-Xdoclint:none

