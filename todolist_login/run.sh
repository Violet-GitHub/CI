#!/bin/sh
echo "start to build APP and test app"
cd todolist_login
ant clean debug
cd ..
echo "start to install source apk"
adb install -r todolist/bin/todolist-debug.apk
echo "start to install test apk"
adb install -r todolist_login/bin/todolistTest-debug.apk
echo "start to run test"
adb shell am instrument -w -e reportDir /mnt/sdcard/testauto/junit/ -e reportFile junit-reportFile.xml com.example.todolist.test/com.example.todolist.test.runner.Runner1
echo "pull junit report"
adb pull /mnt/sdcard/testauto/junit/junit-reportFile.xml $WORKSPACE/junit-reportFile.xml
echo "pull screenshots"
adb pull /sdcard/Robotium-Screenshots/ $WORKSPACE/