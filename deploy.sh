#!/bin/bash
echo "Starting run: `date`"

cd cel_loaders/
export DEPLOY_DIR='/mnt/loaders/loaders-app/'
export SCRIPTS_DIR='/mnt/loaders/scripts/'
export J2SDKDIR=/usr/lib/jvm/jdk1.8.0_131
export J2REDIR=/usr/lib/jvm/jdk1.8.0_131/jre
export JAVA_HOME=/usr/lib/jvm/jdk1.8.0_131
export DERBY_HOME=/usr/lib/jvm/jdk1.8.0_131/db
export PATH=$JAVA_HOME/bin:$PATH
mvn clean install
echo "Deploying to ${DEPLOY_DIR}"
cp target/*bundled*.jar ${DEPLOY_DIR}
echo "Deploying scripts to ${SCRIPTS_DIR}"
cp ../scripts/*.sh ${SCRIPTS_DIR} && chmod +x ${SCRIPTS_DIR}*.sh && ls -lat ${SCRIPTS_DIR}
echo "Installing crontab from file"
cd ..
crontab cron_file.txt
crontab -l
echo " <<<<<<<<<<<<<<< FINISH >>>>>>>>>>>>>>"