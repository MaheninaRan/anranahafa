#compile
cd "C:/apache-tomcat-10/webapps/Sprint8_jar/src"
javac -d C:/apache-tomcat-10/webapps/Sprint8_jar/classes annotation/Anno_Url.java  
javac -d C:/apache-tomcat-10/webapps/Sprint8_jar/classes etu1766/framework/*.java  
javac -d C:/apache-tomcat-10/webapps/Sprint8_jar/classes etu1766/framework/servlet/FrontServlet.java  

# se mettre dans la repertoire contenant les packages et les .class
cd "C:/apache-tomcat-10/webapps/Sprint8_jar/classes"

# construire le fichier jar
jar -cvf ../framework_etu1766.jar .

# copie du jar dans le projet de test
cd ../
copy framework_etu1766.jar "C:/apache-tomcat-10/webapps/Sprint8/WEB-INF/lib/"


cd "C:/apache-tomcat-10/webapps/Sprint8"

jar -cvf test_frame.war .

move /Y test_frame.war "C:/apache-tomcat-10/webapps"