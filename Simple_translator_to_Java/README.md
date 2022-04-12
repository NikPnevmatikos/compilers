Όνομα: Νικόλαος 
Επίθετο: Πνευματικός
ΑΜ: 1115201900157

----------------------------------------------------------------------------------------------------------------------------------
Όσο αναφορά την γραμματική είχα πολλά θέματα με shift reduce conflict τα οποία πολλά λυνόντουσαν αν έγραφα τον ίδιο γραμματικό κανόνα η σύνολο γραμματικών κανόνών με άλλα ονόματα. Επίσης έτσι όπως άρχισα να γράφω την γραμματική πολλοί κανόνες χρειαστηκαν να γραφτούν το ίδιο με την μόνη διαφορά ο ένας από τους δύο να είχε ως τερματικό και IDENTIFIER που είναι αποδεκτό από την γλώσα μόνο στις συναρτήσεις(όπως args,command, comtype για τις συναρτήσεις και arg2, funargs, argtype για την main).

----------------------------------------------------------------------------------------------------------------------------------
Για την εκτέλεση του προγράματος στο makefile ανάλογα για ποιο αρχείο θέλουμε να το εκτελέσουμε το προσθέτουμε στο execute η execute_save. Με την χρήση execute save ο παραγμένος κώδικας java γράφεται στο αρχείο myprog.java (αν δεν υπάρχει δημιουεγείται).
Με την εντολή make run εκτελείται ο αντίστοιχος κώδικας java.

το προγράματα προς εκτέλεση στο φάκελο inputs είναι τα παραδείγματα από την εκφώνηση  της εργασίας + ένα παράδειγμα απο το piazza + ενα παράδειγμα για να δείξω την λειτουργία της if και στη μειν (input.txt) + ένα άδειο αρχείο για να φανεί το παραγόμενο πρόγραμμα στην java που έχει άδεια main.

----------------------------------------------------------------------------------------------------------------------------------

παρακάτω τα αποτελέσματα εκτέλεσης των προγραμμάτων στο φάκελο inputs:

example1:

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

Αποτέλεσμα: 

John
Doe
John Doe

----------------------------------------------------------------------------------------------------------------------------------
example2: 
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

Αποτέλεσμα: 
JohnJohn
Jane

----------------------------------------------------------------------------------------------------------------------------------
example3:
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

Αποτέλεσμα:
Static
Dynamic
Probably Dynamic

----------------------------------------------------------------------------------------------------------------------------------
input:

class myprog{

	public static void main(String[] argv){

		System.out.println((name().startsWith("nick") ? ("nick".startsWith(name()) ? "nick" : "unknown") : "unknown"));

	}
	public static String name(){
		return "nick";
	}
}

Αποτέλεσμα:
nick

----------------------------------------------------------------------------------------------------------------------------------
piazzaexample: 

class myprog{

	public static void main(String[] argv){

		System.out.println(("yes"+("y".startsWith("y") ? " correct" : (" incorrect"+" input"))));
		System.out.println(("yes"+("n".startsWith("y") ? " correct" : (" incorrect"+" input"))));
		System.out.println(("yes"+("y".startsWith("y") ? (" correct"+" input") : " incorrect")));
		System.out.println((((new StringBuffer("yes").reverse()).toString())+("y".startsWith("y") ? (((new StringBuffer("correct ").reverse()).toString())+" input") : " incorrect")));

	}
}


Αποτέλεσμα: 
yes correct
yes incorrect input
yes correct input
sey tcerroc input

----------------------------------------------------------------------------------------------------------------------------------
blankfile:

class myprog{

	public static void main(String[] argv){


	}
}

Αποτέλεσμα: 



