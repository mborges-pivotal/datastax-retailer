#!/bin/bash
#curl -s -L -X GET 'https://9a0ce99e-185f-4cac-9b08-5058fd9e9f73-us-east1.apps.astra.datastax.com/api/rest/v2/keyspaces/retailer/product/021c511c960ef8f643a9367ae7c0091f/pp5005431230' \
curl -s -L -X GET 'https://9a0ce99e-185f-4cac-9b08-5058fd9e9f73-us-east1.apps.astra.datastax.com/api/rest/v2/keyspaces/retailer/product/61f6f799f90061e0169adec4ef68ac51/pp5006332724' \
-H "X-Cassandra-Token: $AUTH_TOKEN" \
-H 'Content-Type: application/json' \
-v
