all: App

username = Tom
password = Hanks

App:
	mvn compile
	mvn exec:java -Dexec.mainClass="com.apt.App" -Dexec.args="$(username) $(password)"

clean:
	mvn clean