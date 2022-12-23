#!/usr/bin/env bash

if ! grep -q enabled <(amazon-linux-extras list | grep epel); then
  echo "installing epel repository..."
  sudo amazon-linux-extras install epel -y
fi
