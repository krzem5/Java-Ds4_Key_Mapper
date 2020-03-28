echo off
echo NUL>_.class&&del /s /f /q *.class
cls
javac -cp com/krzem/ds4_key_mapper/modules/jna-4.0.0.jar;com/krzem/ds4_key_mapper/modules/purejavahidapi.jar; com/krzem/ds4_key_mapper/Main.java&&java -cp com/krzem/ds4_key_mapper/modules/jna-4.0.0.jar;com/krzem/ds4_key_mapper/modules/purejavahidapi.jar; com/krzem/ds4_key_mapper/Main key.xml
start /min cmd /c "echo NUL>_.class&&del /s /f /q *.class"