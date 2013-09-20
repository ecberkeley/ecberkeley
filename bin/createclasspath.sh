D=`dirname $0`
echo  creating script :: $D/setclasspath.sh
echo -n "CP=.:" > $D/setclasspath.sh
 for jar in lib/* ; do  echo -n  "$jar:"; done >> $D/setclasspath.sh
echo "out" >> $D/setclasspath.sh
echo "echo \$CP" >> $D/setclasspath.sh
chmod a+x $D/setclasspath.sh

