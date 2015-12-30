current_folder=`pwd`
echo "$current_folder"

#源码存放的路径
src_folder='/data/mvn/googlesearch'
cd "$src_folder"

git pull

mvn clean package

cd target
target_folder=`pwd`


#tomcat存放的路径
tomcat_folder='/home/ubuntu/data/tomcat/googlesearch.com'


#先关掉对应的tomcat应用进程
echo '关闭进程：\n'
#ps -aux|grep tomcat| grep googlesearch
#app_pid=$(ps -aux|grep tomcat| grep googlesearch| awk '{print $2}')
#kill $app_pid
shutdown_sh="$tomcat_folder/bin/shutdown.sh"

#执行tomcat的shutdown.sh
"$shutdown_sh"


#运行时文件存放 的路径
app_runtime_folder='/data/runapp/googlesearch.com'
#备份
mv app_runtime_folder_bakup="$app_runtime_folder-`date+%Y%m%d-%H%M%S`"

cp -R "$target_folder" "$app_runtime_folder"
cd "$app_runtime_folder"

rm -Rf classes lib generated-sources generated-test-sources maven-archiver maven-status surefire-reports test-classes


startup_sh="$tomcat_folder/bin/startup.sh"








