#!/usr/bin/env bash

if ! command -v "certbot" &>/dev/null; then
  echo "installing certbot..."
  sudo yum install -y certbot python2-certbot-nginx
fi

if ! grep -q "haveged" <(systemctl --all --type service); then
  echo "installing haveged..."
  sudo yum install -y haveged
  systemctl enable haveged
  systemctl start haveged
fi
