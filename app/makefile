all: App

username = ENTER_USERNAME
password = ENTER_PASSWORD

App:
	mvn compile
	mvn exec:java -Dexec.mainClass="com.apt.App" -Dexec.args="$(username) $(password)"

clean:
	mvn clean