## Simple_translator_Java:

compile: make\
\
Run: make execute (Change the input file in run command)\
\
Run and save the output to a new file: make execute_save

----------------------------------------------------------------------------------------------------------------------------------

In the second part of this homework you will implement a parser and translator for a language supporting string operations. The language supports concatenation (+) and "reverse" operators over strings, function definitions and calls, conditionals (if-else i.e, every "if" must be followed by an "else"), and the following logical expression:

* *is-prefix-of (string1 prefix string2): Whether string1 is a prefix of string2.*
All values in the language are strings.

The precedence of the operator expressions is defined as: precedence(if) < precedence(concat) < precedence(reverse).

Your parser, based on a context-free grammar, will translate the input language into Java. You will use JavaCUP for the generation of the parser combined either with a hand-written lexer or a generated-one (e.g., using JFlex, which is encouraged).

You will infer the desired syntax of the input and output languages from the examples below. The output language is a subset of Java so it can be compiled using javac and executed using Java or online Java compilers like this, if you want to test your output.

There is no need to perform type checking for the argument types or a check for the number of function arguments. You can assume that the program input will always be semantically correct.

As with the first part of this assignment, you should accept input programs from stdin and print output Java programs to stdout. For your own convenience you can name the public class "Main", expecting the files that will be created by redirecting stdout output to be named "Main.java".. In order to compile a file named Main.java you need to execute the command: javac Main.java. In order to execute the produced Main.class file you need to execute: java Main.

To execute the program successfully, the "Main" class of your Java program must have a method with the following signature: public static void main(String[] args), which will be the main method of your program, containing all the translated statements of the input program. Moreover, for each function declaration of the input program, the translated Java program must contain an equivalent static method of the same name. Finally, keep in mind that in the input language the function declarations must precede all statements.

---------------------------------------------------------------------------------------------------------------------------------------------------------

##### Examples:

example1:
```java
class myprog{

	public static void main(String[] argv){

		System.out.println(name());
		System.out.println(surname());
		System.out.println(fullname(name(), " ", surname()));

	}
	public static String name(){
		return "John";
	}
	public static String surname(){
		return "Doe";
	}
	public static String fullname(String first_name, String sep, String last_name){
		return first_name + sep + last_name;
	}
}
```
Result: 
\
John\
Doe\
John Doe

----------------------------------------------------------------------------------------------------------------------------------
example2: 
```java
class myprog{

	public static void main(String[] argv){

		System.out.println(cond_repeat("yes", name()));
		System.out.println(cond_repeat("no", "Jane"));

	}
	public static String name(){
		return "John";
	}
	public static String repeat(String x){
		return x + x;
	}
	public static String cond_repeat(String c, String x){
		return ("yes".startsWith(c) ? (c.startsWith("yes") ? repeat(x) : x) : x);
	}
}
```
Result: 
\
JohnJohn\
Jane

----------------------------------------------------------------------------------------------------------------------------------
example3:
```java
class myprog{

	public static void main(String[] argv){

		System.out.println(findLangType("Java"));
		System.out.println(findLangType("Javascript"));
		System.out.println(findLangType("Typescript"));

	}
	public static String findLangType(String langName){
		return (langName.startsWith("Java") ? ("Java".startsWith(langName) ? "Static" : (((new StringBuffer(langName).reverse()).toString()).startsWith(((new StringBuffer("script").reverse()).toString())) ? "Dynamic" : "Unknown")) : (((new StringBuffer(langName).reverse()).toString()).startsWith(((new StringBuffer("script").reverse()).toString())) ? "Probably Dynamic" : "Unknown"));
	}
}
```
Result:
Static\
Dynamic\
Probably Dynamic

----------------------------------------------------------------------------------------------------------------------------------
input:
```java
class myprog{

	public static void main(String[] argv){

		System.out.println((name().startsWith("nick") ? ("nick".startsWith(name()) ? "nick" : "unknown") : "unknown"));

	}
	public static String name(){
		return "nick";
	}
}
```
Result:
nick

----------------------------------------------------------------------------------------------------------------------------------
piazzaexample: 
```java
class myprog{

	public static void main(String[] argv){

		System.out.println(("yes"+("y".startsWith("y") ? " correct" : (" incorrect"+" input"))));
		System.out.println(("yes"+("n".startsWith("y") ? " correct" : (" incorrect"+" input"))));
		System.out.println(("yes"+("y".startsWith("y") ? (" correct"+" input") : " incorrect")));
		System.out.println((((new StringBuffer("yes").reverse()).toString())+("y".startsWith("y") ? (((new StringBuffer("correct ").reverse()).toString())+" input") : " incorrect")));

	}
}
```
Result: 
yes correct\
yes incorrect input\
yes correct input\
sey tcerroc input

----------------------------------------------------------------------------------------------------------------------------------
blankfile:
```java
class myprog{

	public static void main(String[] argv){


	}
}
```
Result: 



