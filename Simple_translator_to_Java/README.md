## Simple_translator_Java:

compile: make\
\
Run: make execute (Change the input file in run command)\
\
Run and save the output to a new file: make execute_save

----------------------------------------------------------------------------------------------------------------------------------

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
John Doe\

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
JohnJohn\
Jane\

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
Probably Dynamic\

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
sey tcerroc input\

----------------------------------------------------------------------------------------------------------------------------------
blankfile:
```java
class myprog{

	public static void main(String[] argv){


	}
}
```
Result: 



