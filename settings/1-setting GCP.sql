________________________________________________________________________________________________________________________________________________________________##################################################################################################################################
##################################################################################################################################
##################################################################################################################################
##################################################################################################################################
##################################################################################################################################

CONFIGURACION DEL PROYECTO EN GOOGLE CLOUD PLATOFM

##################################################################################################################################
##################################################################################################################################
##################################################################################################################################
##################################################################################################################################
________________________________________________________________________________________________________________________________________________________________




1- Crear proyecto en Google Cloud Platform
2- Habilitar APIS
3- Crear un conjunto de datos en BigQuery que se llame "loaders"
4- Crear un bucket para archivos y staging "loaders-data"
5- Dentro del bucket crear dos carpetas "loaders" y "loaders-staging"

		gs://loaders-data/loaders/
		gs://loaders-data/loaders-staging/


6- Agregar una regla firewall en la red que aplique a todas las intancias del proyecto

		Direccion del trafico: ingreso
		Accion: allow
		Target: todas las intancias
		IP: 0.0.0.0/0
		Protocolos y puertos: allow all

7- Crear una Instancia VM debian 9

8- Seguir los pasos de instalacion del servidor (instalaciones.sql)

9- loguease a GCP con auth login y auth application-default

	gcloud auth login --no-launch-browser
	gcloud auth application-default login

10- agregar IP statica a la virtual 

	https://towardsdatascience.com/running-jupyter-notebook-in-google-cloud-platform-in-15-min-61e16da34d52

	En GCP ir a VCP Network - external IP addresses


11- Crear una cuenta de servicio en el cloud

	En api y services ir a credentials
	Crear oauth cliend id
	Selecciona "other"
	Asignar un nobre y crear
	Copiar client id y client secret

12- Crear un refresh token	
		Tener en cuenta que este programa sirve para generar accesos de todas las apis, por lo tanto tener en cuenta de agregar en la url el auth para "bigquery".
		Por lo tanto despues de esta linea tiene que decir bigquery "scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2F"

		Ejecutar el codigo "3-config/dsApi.py" corriendo "python2 dsApi.py --login"
		En teoria via web se podria hacer pero esta presetnando fallas. seguir tutorial: #https://lavastorm.zendesk.com/hc/en-us/articles/213174365-Connecting-to-the-Google-Cloud-Platform-via-OAuth-2-0



13- Modificar config file para agregar parametros del proyecto

		(tener en cuenta el project id que puede asignase unos numeros aparte del nombre)

		project.id=loaders-227115
		project.appName=loaders
		gcs.dna.bucket.location=gs://loaders-data/loaders/
		gcs.dna.staging.location=gs://loaders-data/loaders-staging/

		gc.clientId=690903469871-ps7np75bgbrt6dmtuvpbr34irl01662f.apps.googleusercontent.com
		gc.clientSecret=HbJ8JQ-JtjYTty5OxIGADQBo
		gc.refreshToken=1/SiP87gV6ssCTqDYOiq-VFBhT56Gt0Hwi-4UdmQdgUHs


14- Hacer un commit para enviar modificaciones del proyecto al repo

15- Entrar a jenkins para configurarlo


	Ir a la IP :8080, en este caso http://35.227.89.40:8080
	Desbloquear entrandoe al server y viendo pass -> cat /var/lib/jenkins/secrets/initialAdminPassword
	instalar plugins
	ingresar datos del usuario:

		laiunce
		root
		crilaiun@publicisgroupe.net


16- Configurar tarea para deploy linkeando el repo:
	crear nueva tarea (multibranch pipeline)
	add source "git"
	add credential y repo
	elegir un branch o dejarlo en blanco para todos los branchs
	build by jenkinsfile

	guardar y esperar a que se setee el proyecto


17- Obtener un codigo sin vencimiento de facebook


		Usar la cuenta de Facebook con la cual se tiene acceso al admin de la cuenta

		1-primero generar el appSecret siguiento el tutorial punto 3:
		https://developers.facebook.com/docs/business-sdk/getting-started/

		2-luego generar un token de usuario que no tenga vencimiento
		https://sujipthapa.co/blog/generating-never-expiring-facebook-page-access-token

		3-el accountId es el id de la cuenta que se quiere linkear.


		si hay que renovar ir a 

		https://developers.facebook.com/tools/debug/accesstoken/

		abajo de todo click en "ampliar token de acceso"



________________________________________________________________________________________________________________________________________________________________
##################################################################################################################################
##################################################################################################################################
##################################################################################################################################
##################################################################################################################################

APARTADO

##################################################################################################################################
##################################################################################################################################
##################################################################################################################################
##################################################################################################################################
________________________________________________________________________________________________________________________________________________________________




________________________________________________________________________________________________________________________________________________________________

CLONAR REPO 

cd /opt/
sudo git clone https://laiunce@bitbucket.org/laiunce/loaders.git
git branch master

________________________________________________________________________________________________________________________________________________________________

INICIAR CREDENCIALES GCP

gcloud init
gcloud auth application-default login

________________________________________________________________________________________________________________________________________________________________


EJECUTAR EL CODIGO MANUAL


cd /mnt/loaders/loaders-app/
java -jar loaders_dna-bundled-0.5.jar -from "yesterday" -to "yesterday" -overwrite-data false -replace-data true -concurrent false -facebookAccountId "1963702086992984" -runners "com.dna.app.etl.apitobigquery.facebook.FbActionsToBigQueryPipeline"


________________________________________________________________________________________________________________________________________________________________

TO DO

LINKEAR REPO, CREAR PIPELINE, ARREGLAR VARIABLES GLOBALES

<<<<< falta >>>>>>>

el pipeline de jenkins hay que arreglarlo creo que ay problema con las variables globalaes.

ver si con el cambio de jenkins a root se soluciona lo de que no encunetra maven y esas cosas, adaptar el pipeline a la copia de VS

hacer diagrama y subir pasos de credenciales de facebook por ejemplo

ver tema de los tests en el codigo pruebas de integracion.y esas cosas

en dataflow si es 0 la cantidad de registros que no se carguen

________________________________________________________________________________________________________________________________________________________________











