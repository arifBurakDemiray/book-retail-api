#!/usr/bin/env sh

if [ -d tmp ]; then
  echo "creating tmp folder..."
  mkdir tmp
  sudo chown webapp:webapp tmp
fi

if [ -d logs ]; then
  echo "creating logs folder..."
  mkdir logs
  sudo chown webapp:webapp logs
fi
