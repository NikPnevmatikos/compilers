import java.io.InputStream;
import java.io.IOException;


class Parser{
    private int lookaheadToken;
    private InputStream in;

    public Parser(InputStream in) throws IOException{
        this.in = in;
        lookaheadToken = in.read();
    }

    public int eval() throws IOException, ParseError{
        int value = exp1();
        if(lookaheadToken != '\n' && lookaheadToken != -1){             //if given expresion is not over
            throw new ParseError();
        }
        return value;
    }

    //same as in parse example
    private void consume(int symbol) throws IOException, ParseError{
        if (lookaheadToken != symbol)
            throw new ParseError();
        lookaheadToken = in.read();
    }

    //exp1 -> term1 exp2
    private int exp1() throws IOException, ParseError{
        //the expresion must start with a number or "(" because first(term1) = {num, (}
        if ((lookaheadToken >= '0' && lookaheadToken <= '9') || lookaheadToken == '('){
            return exp2(term1());
        }
        else{
            throw new ParseError();
        }
    }

    //exp2 -> ^ term1 exp2 | ε
    private int exp2(int value) throws IOException, ParseError{

        if (lookaheadToken == '^'){
            consume(lookaheadToken);
            return exp2(value^term1());                                //previus value given ^ value return from term1 function
        }
        else if((lookaheadToken >= '0' && lookaheadToken <= '9') || lookaheadToken == '('){
            throw new ParseError();
        }

        return value;
    }

    //term1 -> factor term2
    private int term1() throws IOException, ParseError{
        if ((lookaheadToken >= '0' && lookaheadToken <= '9') || lookaheadToken == '('){
            return term2(factor());
        }
        else{
            throw new ParseError();
        }
    }

    //term2 -> & factor term2 | ε
    private int term2(int value) throws IOException, ParseError{
        
        if (lookaheadToken == '&'){
            consume(lookaheadToken);
            return term2(value&factor());                           //calculates value given & 
        }
        else if((lookaheadToken >= '0' && lookaheadToken <= '9') || lookaheadToken == '('){
            throw new ParseError();
        }

        return value;
    }

    //factor -> num | (exp1)
    //num -> 0|1|..|9
    private int factor() throws IOException, ParseError{
        int value = -1;
        if (lookaheadToken >= '0' && lookaheadToken <= '9'){
            value = lookaheadToken - '0';                       
            consume(lookaheadToken);
        }
        else if(lookaheadToken == '('){
            consume(lookaheadToken);
            value = exp1();
            consume(')');                       //if next to exp1 is not ")" consume fuction will throw parse error
        }
        else{
            throw new ParseError();
        }

        return value;
    }


}
