#!/bin/bash
JAVA=java
JAR=${JARDIR}loaders_dna-bundled-0.5.jar

STARTTIME=$(date +%s)
echo "Starting today daily run - API to BigQuery: `date`"


echo "Account:  `date`"
$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data true -concurrent false -configFileName "-config.properties" -runners "\
com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"

RESULT=$?
ENDTIME=$(date +%s)
echo "Result: $RESULT"
echo "Ending run: `date`"
echo "Processing time: $((ENDTIME - STARTTIME)) seconds"


