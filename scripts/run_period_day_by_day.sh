#!/bin/bash
JAVA=java
JAR=${JARDIR}loaders_dna-bundled-0.5.jar
#JAR=/Users/crilaiun/NetBeansProjects/loaders/cel_loaders/target/loaders_dna-bundled-0.5.jar

FECHAINI=$1
FECHAFIN=$2
PROPERTIES=$3
LOADER=$4

# INGRESAR DOS DIAS MINIMO!!!

#ejecucion 
#cd /mnt/loaders/loaders-app/
#sh run_period_day_by_day.sh "2019-01-18" "2019-01-19" "ccu_amstel-config.properties,ccu_budweiser-config.properties,ccu_cervezamiller-config.properties,ccu_cervezapalermo-config.properties,ccu_cervezasalta-config.properties,ccu_cervezasantafe-config.properties,ccu_cervezasol-config.properties,ccu_grolsch-config.properties,ccu_growlerbar-config.properties,ccu_heineken-config.properties,ccu_imperial-config.properties,ccu_isenbeck-config.properties,ccu_kunstmann-config.properties,ccu_milochoochentayocho-config.properties,ccu_mistral-config.properties,ccu_sidrareal-config.properties,ccu_tribusur-config.properties,ccu_warsteiner-config.properties,mcdonalds-config.properties,uip-config.properties" "FbAdStatsVideoToBigQueryPipeline"
#server side
#cd /mnt/loaders/loaders-app/
#nohup /mnt/loaders/scripts/run_period_day_by_day.sh "2019-01-01" "2019-01-17" "ccu_grolsch-config.properties,ccu_kunstmann-config.properties,ccu_mistral-config.properties,ccu_sidrareal-config.properties,ccu_milochoochentayocho-config.properties,mcdonalds-config.properties" "FbAdStatsVideoToBigQueryPipeline" > /var/www/html/logs/loaders/run_period_day_by_day_logGeneral_`date "+\%Y-\%m-\%d_\%H:\%M:\%S"`.log &


echo $FECHAINI
echo $FECHAFIN
echo $PROPERTIES
echo $LOADER


startdate=$(date -I -d "$FECHAINI") || exit -1
enddate=$(date -I -d "$FECHAFIN")     || exit -1
enddate2=$(date -I -d "$enddate + 1 day")


arr_properties=$(echo $PROPERTIES | tr "," "\n")
arr_loaders=$(echo $LOADER | tr "," "\n")


d="$startdate"
while [ "$d" != "$enddate2" ]; do
	for prop in $arr_properties
		do
			for load in $arr_loaders
				do
					echo "________________________________________________________________________________________________________________________________________________________"
				    echo "Processing" $d $prop $load 
				    $JAVA -jar $JAR -from "$d" -to "$d" -overwrite-data false -replace-data true -concurrent false -configFileName "$prop" -runners "com.dna.app.etl.apitobigquery.facebook.$load"  > /var/www/html/logs/loaders/run_period_day_by_day_"$d"_"$prop"_"$load"_`date "+\%Y-\%m-\%d_\%H:\%M:\%S"`.log
				done		    
		done

	  d=$(date -I -d "$d + 1 day")
done

