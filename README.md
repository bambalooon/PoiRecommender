#To generate APK
1. ANDROID_HOME environment variable must define location of Android SDK. 
2. Download Android Support Repository, Android Support Library, Google Repository and Google Play Services in SDK Manager.
3. Download heartdroid-0.9.0.jar from https://bitbucket.org/sbobek/heartdroid/downloads and put it anywhere.
4. Open command line in directory where downloaded heartdroid-0.9.0.jar is located. Run: mvn install:install-file -Dfile=heartdroid-0.9.0.jar -DgroupId=heart -DartifactId=heartdroid -Dversion=0.9.0 -Dpackaging=jar
5. Import Gradle project to IDE
6. Run gradle task 'assemble' on project PoiRecommender:Application
7. Project apk will be generated in {project_root}\\Application\build\outputs\apk