#CURRENT_FOLDER=`pwd`
#echo "CURRENT_FOLDER=[$CURRENT_FOLDER]"

#源码存放的路径
SRC_FOLDER='/data/mvn/googlesearch'
cd "$SRC_FOLDER"

git pull

mvn clean package

TARGET_FOLDER="$SRC_FOLDER/target"


#编译结果存放目录
BUILD_FOLDER="$SRC_FOLDER/build";

#清理准备环境
if [ ! -d "$BUILD_FOLDER" ]; then
  	mkdir "$BUILD_FOLDER"
else  
  #备份
	BUILD_FOLDER_BACKUP="$BUILD_FOLDER-`date+%Y%m%d-%H%M%S`"
	mv "$BUILD_FOLDER" "$BUILD_FOLDER_BACKUP"
	mkdir "$BUILD_FOLDER"
fi


#编译结果拷贝过来
cp -R "$TARGET_FOLDER/." "$BUILD_FOLDER"
cd "$BUILD_FOLDER"

#去掉一些
rm -Rf classes lib generated-sources generated-test-sources maven-archiver maven-status surefire-reports test-classes .war


#回到原来的目录
#cd "$CURRENT_FOLDER"






