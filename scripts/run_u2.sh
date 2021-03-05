#!/bin/bash
curl -s -L -X PUT 'https://9a0ce99e-185f-4cac-9b08-5058fd9e9f73-us-east1.apps.astra.datastax.com/api/rest/v2/keyspaces/retailer/assortment_price/cedar_park/021c511c960ef8f643a9367ae7c0091f/pp5005431230' \
-H "X-Cassandra-Token: $AUTH_TOKEN" \
-H 'Content-Type: application/json' \
-d '{
   "total_unit":10 
}' -v
