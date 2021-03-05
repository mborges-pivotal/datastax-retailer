#!/bin/bash
curl -s -L -X GET 'https://9a0ce99e-185f-4cac-9b08-5058fd9e9f73-us-east1.apps.astra.datastax.com/api/rest/v1/keyspaces/retailer/tables/product/rows' \
-H "X-Cassandra-Token: $AUTH_TOKEN" \
-H 'Content-Type: application/json' \
-v
