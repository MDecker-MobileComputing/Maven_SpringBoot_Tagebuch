#!/bin/bash

./mvnw clean javadoc:javadoc -Dshop=private -DadditionalJOption=-Xdoclint:none

