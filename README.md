# cassandra-monitor

## Get started

```
git clone https://github.com/starfisher/cassandra-monitor.git
```
```
mvn package -Dmaven.test.skip=true
```
use ```cd``` to change your directory to the jar path

copy the file ```mbean.cnf``` to the same folder with the target jar

run the jar
```
java -jar xxx.jar
```


## FAQ

### How to modify the ip and authentication of target cluster?

Find and modify the ```application.yml``` under the ```resource``` folder.
- host
- port
- username
- password

### How to modify the scan rate?
Find and modify the ```application.yml``` under the ```resource``` folder.
- fixedRate (ms)

### How to modify the log pattern?
Find and modify the ```logback-spring.xml``` under the ```resource``` folder.
