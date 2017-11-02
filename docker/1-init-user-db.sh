#!/bin/bash
set -e

#createuser --no-superuser --no-createdb --no-createrole --pwprompt igrow-service-dev

#createdb --owner igrow-service-dev igrow-service-dev

#psql -d igrow-service-dev -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
  CREATE EXTENSION IF NOT EXISTS postgis;
  CREATE EXTENSION IF NOT EXISTS postgis_topology;
  CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;
EOSQL
