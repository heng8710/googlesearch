#store current shell pwd folder
CURRENT_FOLDER=`pwd`
echo "CURRENT_FOLDER=[$CURRENT_FOLDER]"

#the source code folder place
SRC_FOLDER='/data/mvn/googlesearch'
cd "$SRC_FOLDER"

git pull

mvn clean package

TARGET_FOLDER="$SRC_FOLDER/target"


#compiled code folder
BUILD_FOLDER="$SRC_FOLDER/build";
 
#clean builde folder
if [ ! -d "$BUILD_FOLDER" ]; then
  	mkdir "$BUILD_FOLDER"
else  
  #back up
	BUILD_FOLDER_BACKUP="$BUILD_FOLDER-$(date +%Y%m%d-%H%M%S)"
	mv "$BUILD_FOLDER" "$BUILD_FOLDER_BACKUP"
	mkdir "$BUILD_FOLDER"
fi


#copy the compiled result
cp -R "$TARGET_FOLDER/." "$BUILD_FOLDER"
cd "$BUILD_FOLDER"

#remove useless file from maven compiled result
rm -Rf classes lib generated-sources generated-test-sources maven-archiver maven-status surefire-reports test-classes .war


#return to stored current pwd folder 
cd "$CURRENT_FOLDER"






