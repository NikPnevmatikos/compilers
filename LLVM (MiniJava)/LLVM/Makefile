FILES = ../llvm-examples/dot_java/

all: compile

compile:
	java -jar ../jtb132di.jar -te ../minijava.jj
	java -jar ../javacc5.jar ../minijava-jtb.jj
	javac Main.java
	javac Vtable.java
	javac SymbolTable.java
	javac LLVMConverter.java


program:
	javac Main.java
	javac Vtable.java
	javac SymbolTable.java
	javac LLVMConverter.java

	
run_examples:
	java Main $(FILES)BinaryTree.java $(FILES)BubbleSort.java $(FILES)Factorial.java \
	$(FILES)LinearSearch.java $(FILES)LinkedList.java $(FILES)MoreThan4.java \
	$(FILES)QuickSort.java $(FILES)TreeVisitor.java 


clean: 
	rm -f *.class *~

