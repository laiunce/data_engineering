#!/bin/bash
JAVA=java
JAR=${JARDIR}loaders_dna-bundled-0.5.jar

STARTTIME=$(date +%s)
echo "Starting today daily run - API to BigQuery: `date`"


echo "Account Id: 1963702086992984  La_Roche `date`"
$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data true -concurrent false -facebookAccountId "1963702086992984" -runners "\
com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1964752940221232  Biotherm `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1964752940221232" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962439643785895  Cacharel `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962439643785895" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962445833785276  Garnier `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962445833785276" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962481307115062  Institucional `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962481307115062" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962460073783852  Institucional `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962460073783852" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1964778276885365  Kiehls `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1964778276885365" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 395491640935587  Beauty for All `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "395491640935587" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962469703782889  LOreal_Paris `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962469703782889" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962432447119948  LOreal_Professionnel `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962432447119948" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962434280453098  Lancome `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962434280453098" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962449157118277  Armani `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962449157118277" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962453643784495  Matrix `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962453643784495" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962484277114765  Maybelline `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962484277114765" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 160760571286751  RalphLauren `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "160760571286751" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1963699923659867  Vichy `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1963699923659867" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1962443490452177  Vogue `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1962443490452177" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
#echo "Account Id: 1964784753551384  Yves_Saint_Lauren `date`"
#$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data false -concurrent false -facebookAccountId "1964784753551384" -runners "\
#com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
#com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"


RESULT=$?
ENDTIME=$(date +%s)
echo "Result: $RESULT"
echo "Ending run: `date`"
echo "Processing time: $((ENDTIME - STARTTIME)) seconds"


