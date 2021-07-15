DreamReading!!!

assuming you already have a functioning eclipse with java jdk, a few more steps
are required in order to install my application:

1. javafx:
1.1 Open Eclipse -> Help -> Eclipse Marketplace
1.2 Search for "javafx"
1.3 You'll see e(fx)eclipse, install it.
1.4 After installation, restart eclipse
1.5 download my project as zip file from git 
1.6 extract files 
1.7 in eclipse: file -> open projects from file system -> choose directory in which my project was saved
1.8 at first, You'll get "The import javafx cannot be resolved"
1.9 Download JavaFX SDK from here
	https://gluonhq.com/products/javafx/
1.10 Extract the folder
1.11 in eclipse right click the project -> properties -> Java Build Path -> Libraries tab (you'll see tabs on the top)
1.12 press "Add external JARs from the right."
1.13 Then from the download JavaFX, Choose all the .jars file in Downloads/javafx-sdk-11.0.2/lib/(all .jar(s) file).
1.14 click open
1.15 Then click Apply and Apply and close.

2. changing path of existing libraries
2.1 in eclipse right click the project -> properties -> Java Build Path -> Libraries tab (you'll see tabs on the top)
2.2 the 2 existing libraries are "json-20201115" and "sqlite-jdbc-3.7.2" - they'll appear in red
2.3 double click each of them and "find" them in the extracted project folder in your computer
	that way, you'll change their path 
2.4 Then click Apply and Apply and close.

3. library.sqlite
3.1 right click -> show in -> properties
3.2 copy location
3.3 in package mainScreen.DBConnection, paste new path accordingly

4. additional settings:
4.1 Then right click on project > Run as > Run configuration -> choose project's name on the left side
4.2 Choose the arguments tab and type this in VM arguments
	 --module-path "put_here_the_path" --add-modules=javafx.controls,javafx.fxml
	 "put_here_the_path" is where you extracted the javasdk files. For example,
	 C:\Users\IMOE001\Downloads\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib
4.3 Click Apply and Run.

then you could run the app regularly through "run", but anytime i need to be alerted / try to close the app with a confirm box, it doesn't work...
	 