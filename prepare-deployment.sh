#!/usr/bin/env bash

write_version_file() {
  echo "VERSION=$(date +%Y%m%d%H%M%S)" > resources/version.properties
  echo "COMMIT=$(git rev-parse HEAD)" >> resources/version.properties
}

write_version_file