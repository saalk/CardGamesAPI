#!/bin/sh

SETTINGS_FILE=$HOME/.pcreditcardsapi/settings.properties

#default settings
SERVER_PORT=8011

if [ -f "$SETTINGS_FILE" ]
then
  echo Loading settings from "$SETTINGS_FILE"
  . "$SETTINGS_FILE"
fi

echo Using SERVER_PORT $SERVER_PORT

