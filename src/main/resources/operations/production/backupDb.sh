#!/bin/bash

readonly APP_NAME=preesence
readonly EMAIL="mainardi@prosystemingegneri.com"

readonly DATABASE="${APP_NAME}"
readonly DATE=$(date +"%Y-%m-%d")
readonly FILE="/tmp/pg_dump_"${DATABASE}"_"${DATE}".gz"

pg_dump -Fc "${DATABASE}" | gzip > "${FILE}"
mutt -s "Backup "${DATABASE}" "${DATE}"" -a "${FILE}" -- "${EMAIL}" < /dev/null
