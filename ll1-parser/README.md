# Compilers Project
## LL-parser:

For the first part of this homework you should implement a simple calculator. The calculator should accept expressions with the bitwise AND(&) and XOR(^) operators, as well as parentheses. The grammar (for single-digit numbers) is summarized in:

exp -> num | exp op exp | (exp)

op -> ^ | &

num -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

You need to change this grammar to support priority between the two operators, to remove the left recursion for LL parsing, etc.

This part of the homework is divided in two parts:

1.For practice, you can write the FIRST+ & FOLLOW sets for the LL(1) version of the above grammar. In the end you will summarize them in a single lookahead table (include a row for every derivation in your final grammar). This part will not be graded.

2.You have to write a recursive descent parser in Java that reads expressions and computes the values or prints "parse error" if there is a syntax error. You don't need to identify blank space or multi-digit numbers. You can read the symbols one-by-one (as in the C getchar() function). The expression must end with a newline or EOF.


---------------------------------------------------------------------------------------------------------

Αρχική Γραμματική\

exp -> num | exp op exp | (exp)

op -> ^ | &

num -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9


Η αρχική γραμματική δεν είναι ll(1) καθώς το first(exp op exp) και first((exp)) είναι ίδια.
Επίσης δεν υπάρχει καμία προταιραιότητα ανάμεσα στους τελεστές ^,&. Με την βοήθεια αντίστοιχου παραδείγματος που είδα στο φροντηστήριο της εργασίας (ίδια γραμματικη απλά με τελεστές +,-,*,/) Η παραπάνω γραμματική μπορεί να μετασχηματιστεί στην εξής μορφή:

exp -> term exp2

exp2 -> ^ term exp2
     | e

term -> factor term2

term2 -> & factor term2
      | e

factor -> num
       | (exp)

num -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

\
Υπολογίζοντας τα first έχουμε:
\
first(exp) = {num,(}\
first(exp2) = {^}\
first(e) = {e}\
first(term) = {num,(}\
first(term2) = {&}\
first(e) = {e}\
first(factor) = {num, (}\
first((exp)) = {(}\
\
Υπολογίζοντας τα follow εχουμε:
\
follow(exp) = {$}\
follow(exp2) = {$}\
follow(term) = {^ , $}\
follow(term2) = {^ , $}\
follow(factor) = {& , ^ , $}\
\
καταλήγουμε στον πίνακα lookahead:
\

|  | 0...9 | ^ | & | ( | ) | $ |
|:------:|:-----:|:--:|:--:|:--:|:--:|:---:|
| exp1 | term exp2 | error | error | term exp2 | error | error |
| exp2 | error |^ term exp2 | error | error | e | e |
| term1 |factor term2 | error | error | factor term2 | error | error |
| term2 | error | e | & factor term2 | error | e | e |
| factor | - | error | error | (exp) | error | error |

