#!/bin/bash

psql -U postgres -d tennistime_test -h localhost -f src/main/resources/schema.sql


psql -U postgres -d tennistime_test -h localhost -f src/main/resources/data.sql

