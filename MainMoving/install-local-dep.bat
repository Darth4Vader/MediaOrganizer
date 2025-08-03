@ echo off

CALL mvn install:install-file  ^
    -DlocalRepositoryPath=repository ^
    -Dfile="Libaries/controlsfx/controlsfx-11.2.2-SNAPSHOT.jar" ^
    -Dsources="Libaries/controlsfx/controlsfx-11.2.2-SNAPSHOT-sources.jar" ^
    -DgroupId=org.controlsfx  ^
    -DartifactId=controlsfx  ^
    -Dversion=11.2.2-SNAPSHOT  ^
    -Dpackaging=jar

CALL mvn install:install-file  ^
    -DlocalRepositoryPath=repository ^
    -Dfile="Libaries/jiconextractreloaded-1.0.jar" ^
    -Dsources="Libaries/jiconextractreloaded-1.0-sources.jar" ^
    -DgroupId=JIconExtractReloaded  ^
    -DartifactId=JIconExtractReloaded  ^
    -Dversion=0.0.1-SNAPSHOT  ^
    -Dpackaging=jar

CALL mvn install:install-file  ^
    -DlocalRepositoryPath=repository ^
    -Dfile="Libaries/vorbis-java-tika-0.8.5.jar" ^
    -Dsources="Libaries/vorbis-java-tika-0.8.5-sources.jar" ^
    -DgroupId=org.gagravarr  ^
    -DartifactId=vorbis-java-tika  ^
    -Dversion=0.8.5  ^
    -Dpackaging=jar


CALL mvn install:install-file  ^
    -DlocalRepositoryPath=repository ^
    -Dfile="Libaries/vorbis-java-core-0.8.5.jar" ^
    -Dsources="Libaries/vorbis-java-core-0.8.5-sources.jar" ^
    -DgroupId=org.gagravarr  ^
    -DartifactId=vorbis-java-core  ^
    -Dversion=0.8.5  ^
    -Dpackaging=jar