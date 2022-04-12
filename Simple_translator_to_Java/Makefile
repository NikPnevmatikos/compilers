INPUTS = inputs/

all: compile

compile:
	jflex scanner.flex
	java -jar java-cup-11b.jar -interface -parser Parser parser.cup
	javac -cp java-cup-11b-runtime.jar *.java

execute:
	java -cp java-cup-11b-runtime.jar:. Main <$(INPUTS)example3.txt

execute_save:
	java -cp java-cup-11b-runtime.jar:. Main <$(INPUTS)example2.txt > myprog.java

run:
	javac myprog.java
	java myprog

clean:
	rm -f *.class *~
	rm Scanner.java
	rm Parser.java
	rm sym.java
	rm myprog.java
