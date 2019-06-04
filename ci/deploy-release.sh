#!/bin/bash
set -e
set -uxo pipefail

# Decrypt and import signing key
openssl aes-256-cbc -K $encrypted_57a5d69ebeaa_key -iv $encrypted_57a5d69ebeaa_iv -in ci/dropwizard.asc.enc -out ci/dropwizard.asc -d
gpg --armor --import ci/dropwizard.asc

./mvnw -B deploy --settings 'ci/settings.xml' -DperformRelease=true -Dmaven.test.skip=true
