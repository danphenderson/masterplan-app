# Masterplan Desktop Application 

----
 
### Notes to Developers:
**Technologies:**
- `Java11`
- `Openjfx16`
- `JUnit4`
- `Maven3`

**Execution:**

Ensure your local environment has the needed dependencies by executing:

`%` java --version
  
The output will return the configured JDK, it must be 11 or greater. 

 `%` mvn -v 

To support mavan projects using java modules (introduced in java version 9), you must have mvn 3.7.X
or greater installed on your system.

Lastly, construct the Jar file in the root directory of the project:

`%` mvn compile package

Then to run the shade constructed uber-jar:

`%` java -jar ui/shade/ui.jar


  
