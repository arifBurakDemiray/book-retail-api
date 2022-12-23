#!/usr/bin/env bash

CERTBOT_DOMAIN="@ssl.domain@"
CERTBOT_EMAIL="@ssl.email@"

if ! grep -q letsencrypt </etc/nginx/nginx.conf; then
  sudo certbot -n -d "$CERTBOT_DOMAIN" --nginx --agree-tos --email "$CERTBOT_EMAIL"
fi
