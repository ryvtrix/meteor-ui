#!/bin/sh
# Needs JDK 21. Result: build/libs/1.0.0/prism-ui-1.0.0+mc1.21.11.jar
chmod +x ./gradlew
./gradlew buildActive
