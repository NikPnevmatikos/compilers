import syntaxtree.*;
import visitor.*;
import java.util.*;

public class Typevisitor extends GJDepthFirst<String, String>{
    SymbolTable symboltable;
    String camefrom;                                            
    String classScope;
    String funScope;

    List<String> funargs;                                            //will be use in function message send

    public Typevisitor(SymbolTable symboltable){
        this.symboltable = symboltable;
        camefrom = null;
        classScope = null;
        funScope = null;
        funargs = new ArrayList<>();
    }

    @Override
    public String visit(MainClass n, String argu) throws Exception {
        String classname = n.f1.accept(this, argu);
        camefrom = "Class";
        classScope = classname;
        funScope = "main";
        super.visit(n, argu);

        return null;
 
    }
    @Override
    public String visit(ClassDeclaration n, String argu) throws Exception {             //declared only to update wich class came from
        String classname = n.f1.accept(this, null);
        classScope = classname;
        camefrom = "Class";

        super.visit(n, argu);

        return null;
    }

    @Override
    public String visit(ClassExtendsDeclaration n, String argu) throws Exception {      //declared only to update wich class came from
        String classname = n.f1.accept(this, null);
        camefrom = "Class";
        classScope = classname;

        super.visit(n, argu);
        return null;
    }

    @Override
    public String visit(MethodDeclaration n, String argu) throws Exception {    //declared to update wich functiom came from, and check if return type matches return value
        String type = n.f1.accept(this,argu);
        String myName = n.f2.accept(this, null);

        camefrom = "Function";
        funScope = myName;

        String exptype = n.f10.accept(this,argu);                               //if return type is expression accept will return the type
        Vars expvar = symboltable.is_declared(exptype, classScope, funScope);
        if(expvar != null){                                                     //if return type is variable accept will return its name so we need to update exptype to var type
            exptype = expvar.get_type();
        }
        if(exptype != type){                                                    //if returned type does not much returned value
            throw new RuntimeException("Function " + myName + " must return " + type + " type");
        }

        super.visit(n, argu);
        
        return null;
    }

    @Override
    public String visit(VarDeclaration n, String argu) throws Exception{        //declared only to check if types are permited by minijava
        String myType = n.f0.accept(this, null);

        if (myType != "int" && myType != "boolean" && myType != "int[]" && myType != "boolean[]" && symboltable.exist(myType) == false){
            throw new RuntimeException("Unknown declare type " + myType);
        }

        return null;
    }
    /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
    public String visit(AssignmentStatement n, String argu) throws Exception{
        
        String name = n.f0.accept(this, argu);
        Vars myvar = symboltable.is_declared(name,classScope,funScope);
        if(myvar == null){                                                  //if var does not exist in current class scope or function scope then its not declared
            throw new RuntimeException("Identifier " + name + " not declared in this scope");
        }
        else{
            String exptype = n.f2.accept(this, argu);                       //if type is expression accept will return the type
            Vars expvar = symboltable.is_declared(exptype ,classScope,funScope);
            if(expvar != null){                                             //if its variable accept will return the name so exptype needs to be updated
                exptype = expvar.get_type();
            }
            if(symboltable.exist(exptype) == true && symboltable.exist(myvar.get_type()) == true){       //if both ends of assintment are class type
                if(symboltable.checkinheritance(exptype,myvar.get_type()) == true){                     //if right value inherits from left types are compatable
                    exptype = myvar.get_type();                                                         //make type compatable
                }
            }
            if(myvar.get_type() != exptype){                                                            //if types does not much
                throw new RuntimeException("Expected type " + myvar.get_type() + " but expresion is type " + exptype);
            }
        }
        
        return null;
    }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
    public String visit(ArrayAssignmentStatement n, String argu) throws Exception {
        String arname = n.f0.accept(this,argu);
        Vars arvar = symboltable.is_declared(arname, classScope, funScope);

        if (arvar == null){
            throw new RuntimeException("Variable " + arname + " was not declared in this scope");
        }
        else if(arvar.get_type() != "int[]" && arvar.get_type() != "boolean[]"){
            throw new RuntimeException("Array " + arname + " must be type of int[] or boolean[]");
        }
        else {
            String idtype = n.f2.accept(this,argu);
            Vars expvar = symboltable.is_declared(idtype ,classScope,funScope);
            if(expvar != null){
                idtype = expvar.get_type();
            }
            if (idtype != "int"){                                               //index type is always int
                throw new RuntimeException("Index must be type of int");
            }
            String exptype = n.f5.accept(this,argu);
            expvar = symboltable.is_declared(exptype ,classScope,funScope);
            if(expvar != null){
                exptype = expvar.get_type();
            }

            if(arvar.get_type().startsWith(exptype) == false){                  //  -> {type}[] starts with {type}
                throw new RuntimeException("Tryed to assign " + exptype + " but array " + arname + " is type " + arvar.get_type());
            }
        }

        return null;
     }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
    public String visit(IfStatement n, String argu) throws Exception {
        String exptype = n.f2.accept(this,argu);
        Vars expvar = symboltable.is_declared(exptype ,classScope,funScope);
        if(expvar != null){
            exptype = expvar.get_type();
        }

        if (exptype != "boolean"){                                          //if expression is always boolean
            throw new RuntimeException("If statement must be type boolean");
        }
        else{
            super.visit(n, argu);
        }
        return null;
    }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
    public String visit(WhileStatement n, String argu) throws Exception {
        String exptype = n.f2.accept(this,argu);
        Vars expvar = symboltable.is_declared(exptype ,classScope,funScope);
        if(expvar != null){
            exptype = expvar.get_type();
        }

        if(exptype != "boolean"){                                               //while expression is always boolean
            throw new RuntimeException("while statement must be type boolean");
        }
        else{
            super.visit(n, argu);
        }
        
        return null;
    }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    public String visit(PrintStatement n, String argu) throws Exception {
        String exptype = n.f2.accept(this,argu);

        Vars expvar = symboltable.is_declared(exptype, classScope, funScope);
        if (expvar != null){
            exptype = expvar.get_type();
        }
        if(exptype != "int"){                                                   //print statment only prints ints in minijava
            throw new RuntimeException("Print statement must be type of int");
        }
        return null;
    }

   /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
    public String visit(AndExpression n, String argu) throws Exception {
        String left = n.f0.accept(this, argu);
        String right = n.f2.accept(this, argu);

        Vars leftvar = symboltable.is_declared(left, classScope, funScope);
        if (leftvar != null){
            left = leftvar.get_type();
        }

        Vars rightvar = symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null) {
            right = rightvar.get_type();
        }

        if(left != "boolean" || right != "boolean"){                                //and aplies to boolean types
            throw new RuntimeException("Error in Andexpression:Expression must be type of boolean");
        }
        return "boolean";
     }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public String visit(CompareExpression n, String argu) throws Exception {
        String left = n.f0.accept(this, argu);
        String right = n.f2.accept(this, argu);

        Vars leftvar = symboltable.is_declared(left, classScope, funScope);
        if (leftvar != null){
            left = leftvar.get_type();
        }

        Vars rightvar = symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null) {
            right = rightvar.get_type();
        }

        if(left != "int" || right != "int"){                                              // compare aplies to int types
            throw new RuntimeException("Error in Compareexpression:Expression must be type of int");
        }
        return "boolean";
    }
    
   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public String visit(PlusExpression n, String argu) throws Exception {
        String left = n.f0.accept(this, argu);
        String right = n.f2.accept(this, argu);

        Vars leftvar = symboltable.is_declared(left, classScope, funScope);
        if (leftvar != null){
            left = leftvar.get_type();
        }
        Vars rightvar = symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null) {
            right = rightvar.get_type();
        }


        if(left != "int" || right != "int"){                                //addition aplies to int types                     
            throw new RuntimeException("Error in Plusexpression:Expression must be type of int");
        }
        
        return "int";
    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public String visit(MinusExpression n, String argu) throws Exception {
        String left = n.f0.accept(this, argu);
        String right = n.f2.accept(this, argu);

        Vars leftvar = symboltable.is_declared(left, classScope, funScope);
        if (leftvar != null){
            left = leftvar.get_type();
        }

        Vars rightvar = symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null) {
            right = rightvar.get_type();
        }

        if(left != "int" || right != "int"){                                    //minus aplies to int types
            throw new RuntimeException("Error in Minusexpression:Expression must be type of int");
        }
        
        return "int";
    }


   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    public String visit(TimesExpression n, String argu) throws Exception {
        String left = n.f0.accept(this, argu);
        String right = n.f2.accept(this, argu);

        Vars leftvar = symboltable.is_declared(left, classScope, funScope);
        if (leftvar != null){
            left = leftvar.get_type();
        }

        Vars rightvar = symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null) {
            right = rightvar.get_type();
        }

        if(left != "int" || right != "int"){                                    //multiply aplies to int types
            throw new RuntimeException("Error in Timesexpression:Expression must be type of int");
        }
        
        return "int";
    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public String visit(ArrayLookup n, String argu) throws Exception {
        String arname = n.f0.accept(this,argu);
        Vars arvar = symboltable.is_declared(arname, classScope, funScope);
        if(arvar != null){
            arname = arvar.get_type();
        }

        if (arname != "int[]" && arname != "boolean[]"){                                //array types are int[] and boolean[] in minijava
            throw new RuntimeException("Array must be type of int[] or boolean[]");
        }
        else{
            String idname = n.f2.accept(this,argu);
            String idtype;
            Vars idvar = symboltable.is_declared(idname, classScope, funScope);
            if(idvar == null){
                idtype = idname;
            }
            else{
                idtype = idvar.get_type();
            }
            if (idtype != "int"){                                                   //index of array is always int
                throw new RuntimeException("Index must be type of int");
            }

        }
        if(arname == "int[]"){
            return "int";
        }
        else{
            return "boolean";
        }
     }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public String visit(ArrayLength n, String argu) throws Exception {
        String arname = n.f0.accept(this,argu);
        Vars arvar = symboltable.is_declared(arname, classScope, funScope);
        
        if(arvar != null){
            arname = arvar.get_type();
        }

        if (arname != "int[]" && arname != "boolean[]"){                                        //lenght function applies only to arrays
            throw new RuntimeException("In lenght, array must be type of int[] or boolean[]");
        }

        return "int";
        
    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
    public String visit(MessageSend n, String argu) throws Exception {
        String function_type;
        String prtype = n.f0.accept(this,argu);
        Vars prvar = symboltable.is_declared(prtype, classScope, funScope);
        if(prvar != null){
            prtype = prvar.get_type();
        }

        if(symboltable.exist(prtype) == false){                                     //only class type can call functions
            throw new RuntimeException("Function is not compatable with given type " + prtype);
        } 
        else{
            String funname = n.f2.accept(this,argu);
            Functions myfun = symboltable.fun_exist(prtype, funname);
            if(myfun == null){                                                      //if function name has not been declared in class or parents
                throw new RuntimeException("Function " + funname + " is not declared in this scope");
            }
            function_type = myfun.get_type();                                       //to return expression type
            
            n.f4.accept(this, argu);                                                //function call arguments will be stored in a List named funargs

            if(symboltable.checkfunarg(myfun.get_list_args(),funargs)== false){     //check if function arguments types are the same with the ones given
                throw new RuntimeException("Function call " + funname + " arguments does not much function declared arguments");
            }

            funargs.clear();                                                        //clear argument list for the next function call

        }
        return function_type;
    }

    /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
    public String visit(ExpressionList n, String argu) throws Exception {
        String exp = n.f0.accept(this,argu);

        Vars expvar = symboltable.is_declared(exp, classScope, funScope);
        if(expvar != null){
            exp = expvar.get_type();
        }

        funargs.add(exp);                                               //add argument to list
        n.f1.accept(this,argu);
        return null;
    }

    /**
     * f0 -> ","
    * f1 -> Expression()
    */
    public String visit(ExpressionTerm n, String argu) throws Exception {

        String exp = n.f1.accept(this,argu);
        Vars expvar = symboltable.is_declared(exp, classScope, funScope);
        if(expvar != null){
            exp = expvar.get_type();
        }
        funargs.add(exp);                                                 //add argument to list
        
        return null;
    }

   /**
    * f0 -> "this"
    */
    public String visit(ThisExpression n, String argu) throws Exception {
        return classScope;                                                    //this will be replace with the current class
    }

   /**
    * f0 -> "new"
    * f1 -> "boolean"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    public String visit(BooleanArrayAllocationExpression n, String argu) throws Exception {
        String exptype = n.f3.accept(this,argu);
        Vars expvar = symboltable.is_declared(exptype, classScope, funScope);
        if (expvar != null){
            exptype = expvar.get_type();
        }

        if (exptype != "int"){                                              //allocation index must be int
            throw new RuntimeException("Error in allocating boolean array, expression must be type of int " );
        }
        
        return "boolean[]";
    }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    public String visit(IntegerArrayAllocationExpression n, String argu) throws Exception {
        String exptype = n.f3.accept(this,argu);
        Vars expvar = symboltable.is_declared(exptype, classScope, funScope);
        if (expvar != null){
            exptype = expvar.get_type();
        }
        if (exptype != "int"){                                              //allocation index must be int
            throw new RuntimeException("Error in allocating int array, expression must be type of int " );
        }
        
        return "int[]";
    }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    public String visit(AllocationExpression n, String argu) throws Exception {
        String name = n.f1.accept(this,argu);
        return name;
    }

    /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    public String visit(NotExpression n, String argu) throws Exception {
        String exptype = n.f1.accept(this,argu);
        Vars expvar = symboltable.is_declared(exptype, classScope , funScope);
        if( expvar != null){
            exptype = expvar.get_type();
        }
        
        if( exptype != "boolean"){
            throw new RuntimeException("Statement in not expresion must be type of boolean");
        }
        return "boolean";
    }

    /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public String visit(BracketExpression n, String argu) throws Exception {
        
        String exptype = n.f1.accept(this,argu);
        Vars expvar = symboltable.is_declared(exptype, classScope , funScope);
        if( expvar != null){
            exptype = expvar.get_type();
        }

        return exptype;

    }
    @Override
    public String visit(IntegerArrayType n, String argu) {
        return "int[]";
    }

    public String visit(BooleanArrayType n, String argu) {
        return "boolean[]";
    }

    public String visit(BooleanType n, String argu) {
        return "boolean";
    }

    public String visit(IntegerType n, String argu) {
        return "int";
    }

    @Override
    public String visit(Identifier n, String argu) {
        return n.f0.toString();
    }

    /**
    * f0 -> <INTEGER_LITERAL>
    */
    public String visit(IntegerLiteral n, String argu) throws Exception {
        super.visit(n, argu);
        return "int";
    }

   /**
    * f0 -> "true"
    */
    public String visit(TrueLiteral n, String argu) throws Exception {
        super.visit(n, argu);
        return "boolean";
    }

    /**
     * f0 -> "false"
    */
    public String visit(FalseLiteral n, String argu) throws Exception {
        super.visit(n, argu);
        return "boolean";
    }
}
