#!/bin/bash
JAVA=java
JAR=${JARDIR}loaders_dna-bundled-0.5.jar

STARTTIME=$(date +%s)
echo "Starting today daily run - API to BigQuery: `date`"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

echo "config.properties `date`"
$JAVA -jar $JAR -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline"

RESULT=$?
ENDTIME=$(date +%s)
echo "Result: $RESULT"
echo "Ending run: `date`"
echo "Processing time: $((ENDTIME - STARTTIME)) seconds"




