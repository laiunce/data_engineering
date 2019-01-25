


NO carga data --> fb_canvas_component_avg_pct_view |  java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbCanvasComponentToBigQueryPipeline"
NO carga data --> fb_website_ctr | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-10" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbWebsiteCtrToBigQueryPipeline"

fb_actions  |  java -jar loaders_dna-bundled-0.5.jar -from "2018-12-30" -to "2018-12-30" -overwrite-data false -replace-data true -concurrent true -facebookAccountId "1962484277114765" -runners "com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline"
fb_ad_set   |  java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline"
fb_ad_stats |  java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline"
fb_cost_per_action_type |  java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline"
fb_unique_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"
fb_video_10_sec_watched_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbVideo10SecToBigQueryPipeline"
fb_video_30_sec_watched_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbVideo30SecToBigQueryPipeline"
fb_video_avg_percent_watched_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbVideoAvgPercentToBigQueryPipeline"
fb_video_avg_time_watched_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbVideoAvgTimeToBigQueryPipeline"
fb_video_p100_watched_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbVideoP100ToBigQueryPipeline"
fb_video_p25_watched_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbVideoP25ToBigQueryPipeline"
fb_video_p50_watched_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbVideoP50ToBigQueryPipeline"
fb_video_p75_watched_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbVideoP75ToBigQueryPipeline"
fb_video_p95_watched_actions | java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-02" -overwrite-data false -replace-data true -concurrent true -runners "com.dna.app.etl.apitobigquery.facebook.FbVideoP95ToBigQueryPipeline"


____________________________________________________________________________________________________________________________________________________________________________

ps -ef
kill 11769

____________________________________________________________________________________________________________________________________________________________________________

/mnt/loaders/loaders-app/

nohup java -jar loaders_dna-bundled-0.5.jar -from "2018-12-15" -to "2018-12-31" -overwrite-data false -replace-data true -concurrent false -facebookAccountId "1789338334623523" -runners "\
com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline" > /var/www/html/logs/loaders/runweekly_facebook_crm_falabella_`date "+\%Y-\%m-\%d_\%H:\%M:\%S"`.log &



nohup java -jar loaders_dna-bundled-0.5.jar -from "2018-12-15" -to "2018-12-31" -overwrite-data false -replace-data true -concurrent false -facebookAccountId "1789338334623523" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline" > /var/www/html/logs/loaders/runweekly_facebook_crm_falabella_`date "+\%Y-\%m-\%d_\%H:\%M:\%S"`.log &

cd /mnt/loaders/loaders-app/

nohup /mnt/loaders/scripts/runhistory_ccu.sh >> /var/www/html/logs/loaders/ccu_runhistory_ccu_20190101_20190101_`date "+\%Y-\%m-\%d_\%H:\%M:\%S"`.log &


nohup java -jar loaders_dna-bundled-0.5.jar -from "2019-01-12" -to "2019-01-13" -overwrite-data false -replace-data true -concurrent false -facebookAccountId "1789338334623523" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline" > /var/www/html/logs/loaders/run12_13days_facebook_crm_falabella_`date "+\%Y-\%m-\%d_\%H:\%M:\%S"`.log &

____________________________________________________________________________________________________________________________________________________________________________

historico con nohup


cd /mnt/loaders/loaders-app/

nohup /mnt/loaders/scripts/run_history_by_dates.sh >> /var/www/html/logs/loaders/run_history_by_dates_`date "+\%Y-\%m-\%d_\%H:\%M:\%S"`.log &





cd /mnt/loaders/loaders-app/

nohup java -jar loaders_dna-bundled-0.5.jar -from "2019-01-01" -to "2019-01-15" -overwrite-data false -replace-data true -concurrent false -configFileName "UIP-config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsVideoToBigQueryPipeline" > /var/www/html/logs/loaders/UIP_20190101_20190115_`date "+\%Y-\%m-\%d_\%H:\%M:\%S"`.log &


____________________________________________________________________________________________________________________________________________________________________________

java -jar loaders_dna-bundled-0.5.jar -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -configFileName "falabella-config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline"


java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-01" -overwrite-data false -replace-data true -concurrent false -configFileName "ccu-config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline"

java -jar loaders_dna-bundled-0.5.jar -from "2018-10-01" -to "2018-10-01" -overwrite-data false -replace-data true -concurrent false -configFileName "dna-config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline"

java -jar loaders_dna-bundled-0.5.jar -from "2018-10-01" -to "2018-10-01" -overwrite-data false -replace-data true -concurrent false -configFileName "ccu-config.properties" -facebookAccountId "647636758768461" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline"

java -jar loaders_dna-bundled-0.5.jar -from "2019-01-01" -to "2019-01-16" -overwrite-data false -replace-data true -concurrent false -configFileName "ccu_heineken-config.properties" -runners "com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline"


java -jar loaders_dna-bundled-0.5.jar -configFileName "ccu-config.properties"




#se corre a las 4 hs del server porque esta adelantado 3 hs respecto a ARG
cd /mnt/loaders/loaders-app/
java -jar loaders_dna-bundled-0.5.jar -from "2018-12-01" -to "2018-12-01" -overwrite-data false -replace-data true -concurrent false -facebookAccountId "1963702086992984" -runners "
com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"

cd /mnt/loaders/loaders-app/
java -jar loaders_dna-bundled-0.5.jar -from "2019-01-01" -to "2019-01-01" -overwrite-data false -replace-data true -concurrent false -facebookAccountId "1963702086992984" -runners "
com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"



$JAVA -jar $JAR -from "yesterday" -to "yesterday" -overwrite-data false -replace-data true -concurrent false -facebookAccountId "1963702086992984" -runners "\
com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdSetToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbAdStatsToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbCostPerActionTypeToBigQueryPipeline,\
com.dna.app.etl.apitobigquery.facebook.FbUniqueActionsToBigQueryPipeline"

