
CURRENT_FOLDER=`pwd`
echo "$CURRENT_FOLDER"

#源码存放的路径
SRC_FOLDER='/data/mvn/googlesearch'
cd "$SRC_FOLDER"

git pull

mvn clean package

cd target
TARGET_FOLDER=`pwd`


#tomcat存放的路径
TOMCAT_FOLDER='/home/ubuntu/data/tomcat/googlesearch.com'


#先关掉对应的tomcat应用进程
echo '关闭进程：\n'
#ps -aux|grep tomcat| grep googlesearch
#app_pid=$(ps -aux|grep tomcat| grep googlesearch| awk '{print $2}')
#kill $app_pid
SHUTDOWN_SH="$tomcat_folder/bin/shutdown.sh"

#执行tomcat的shutdown.sh
$SHUTDOWN_SH


#运行时文件存放 的路径
APP_RUNTIME_FOLDER='/data/runapp/googlesearch.com'
#备份
APP_RUNTIME_FOLDER_BACKUP="$APP_RUNTIME_FOLDER-`date+%Y%m%d-%H%M%S`"
mv "$APP_RUNTIME_FOLDER" "$APP_RUNTIME_FOLDER_BACKUP"

cp -R "$TARGET_FOLDER" "$APP_RUNTIME_FOLDER"
cd "$APP_RUNTIME_FOLDER"

rm -Rf classes lib generated-sources generated-test-sources maven-archiver maven-status surefire-reports test-classes


STARTUP_SH="$tomcat_folder/bin/startup.sh"
$STARTUP_SH








