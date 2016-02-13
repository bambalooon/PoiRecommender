#To generate APK
1. ```ANDROID_HOME``` environment variable or ```sdk.dir``` property in ```local.properties``` must define location of Android SDK. 
2. Download ```Android Support Repository```, ```Android Support Library```, ```Google Repository``` and ```Google Play Services``` in ```SDK Manager```.
3. *Optional:* import Gradle project to IDE
4. Run gradle task 'assemble' on project PoiRecommender:Application
5. Project apk will be generated in {project_root}\\Application\build\outputs\apk