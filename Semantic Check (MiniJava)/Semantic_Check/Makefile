FILES = ../minijava-examples-new/
FILES2 = ../minijava-examples-new/minijava-error-extra/

all: compile

compile:
	java -jar ../jtb132di.jar -te ../minijava.jj
	java -jar ../javacc5.jar ../minijava-jtb.jj
	javac Main.java

program:
	javac Main.java
	
run_examples:
	java Main $(FILES)BinaryTree.java $(FILES)BubbleSort.java $(FILES)Factorial.java \
	$(FILES)LinearSearch.java $(FILES)LinkedList.java $(FILES)MoreThan4.java \
	$(FILES)QuickSort.java $(FILES)TreeVisitor.java 

run_error:
	java Main $(FILES2)BadAssign.java $(FILES2)BadAssign2.java $(FILES2)Classes-error.java \
	$(FILES2)DoubleDeclaration1.java $(FILES2)DoubleDeclaration4.java \
	$(FILES2)DoubleDeclaration6.java $(FILES2)NoMatchingMethod.java $(FILES2)NoMethod.java \
	$(FILES2)Overload1.java $(FILES2)test18.java $(FILES2)test21.java $(FILES2)test35.java \
	$(FILES2)test52.java $(FILES2)test68.java $(FILES2)UseArgs.java 

clean: 
	rm -f *.class *~

