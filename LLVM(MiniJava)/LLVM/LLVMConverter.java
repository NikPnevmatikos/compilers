import syntaxtree.*;
import visitor.*;
import java.util.*;

public class LLVMConverter extends GJDepthFirst<String, String>{

    Vtable vtable;
    String camefrom;
    String classScope;
    String funScope;
    int regcount;                                                //registers id in each function
    List<String> args_regs;                                      //holds registers when message send is called
    String classtype;                                            //needed for case (new class()).function(...)
    String arraytype;                                            //needed for case (new array[size])[index]
    int labelif;                                                 //labels counter for conditions
    int labelwhile;                                              //labels conter for loops

    void close_file(){
        vtable.close_file();
    }

    public LLVMConverter(SymbolTable symboltable, String filename){
        vtable = new Vtable(symboltable, filename);
        vtable.create_vtables();                                //writes vtables at start of .ll file
        camefrom = null;
        classScope = null;
        funScope = null;
        regcount = 0;
        args_regs = new ArrayList<>();
        classtype = null;
        arraytype = null;
        labelif = 0;
        labelwhile = 0;
    }

    @Override
    public String visit(MainClass n, String argu) throws Exception {
        String classname = n.f1.accept(this, argu);
        vtable.write("define i32 @main() {\n\n");
        
        classScope = classname;
        camefrom = "Function";
        funScope = "main";

        n.f14.accept(this,null);
        vtable.write("\n");
        n.f15.accept(this,null);

        vtable.write("\n\tret i32 0\n}\n\n");

        return null;
 
    }

    @Override
    public String visit(ClassDeclaration n, String argu) throws Exception {//needed to update classScope        
        String classname = n.f1.accept(this, null);
        classScope = classname;
        camefrom = "Class";

        super.visit(n, argu);

        return null;
    }

    @Override
    public String visit(ClassExtendsDeclaration n, String argu) throws Exception {//needed to update classScope 
        String classname = n.f1.accept(this, null);
        camefrom = "Class";
        classScope = classname;

        super.visit(n, argu);
        return null;
    }

    @Override
    public String visit(MethodDeclaration n, String argu) throws Exception {    
        String type = n.f1.accept(this,argu);
        String myName = n.f2.accept(this, null);

        camefrom = "Function";
        funScope = myName;

        String buf = "define ";
        buf = buf + vtable.convert_type(type) + " ";
        buf = buf + "@" + classScope + "." + myName + "(i8* %this";
        vtable.write(buf);

        n.f4.accept(this,null);
        vtable.write(") {\n");
        
        vtable.alocate_args(classScope, funScope);                  //each argument allocat a register and store argument value

        regcount = 0;                                               //we can use same registers in different functions so reset register id each time
        n.f7.accept(this,null);
        vtable.write("\n");
        n.f8.accept(this,null);
        String returnexp = n.f10.accept(this,null);                 //either register of expression or a varible name

        Vars retvar = vtable.symboltable.is_declared(returnexp, classScope, funScope);
        if(retvar != null){
            if(vtable.is_var_local(classScope, funScope, retvar) == true){          //if variable is in function, just load the value
                returnexp = "%_" + regcount;
                regcount = vtable.load_local_var(retvar, regcount);

            }
            else{                                                                  //if variable is from a class access it throw its offset
                returnexp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(retvar, regcount, true);
            }
        }

        vtable.write("\n\tret " + vtable.convert_type(type) + " " + returnexp);
        vtable.write("\n\n}\n\n");
        
        return null;
    }
  
     /**
      * f0 -> Type()
      * f1 -> Identifier()
      */
     public String visit(FormalParameter n, String argu) throws Exception {
        String type = n.f0.accept(this, argu);
        String name = n.f1.accept(this, argu);
        String buf = ", ";
        buf = buf + vtable.convert_type(type) + " %." + name;
        vtable.write(buf);
        return null;
     }
  
    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    @Override
    public String visit(VarDeclaration n, String argu) throws Exception{        
        String type = n.f0.accept(this,null);
        String name = n.f1.accept(this,null);
        
        if(camefrom == "Function"){                                                         //only local variables need allocation
                                                                                            //variables from classes are allocating when the class is initialised
            vtable.write("\t%" + name + " = alloca " + vtable.convert_type(type) + "\n");
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
        String identifier = n.f0.accept(this,null);
        String exp = n.f2.accept(this,null);  
        
        Vars myvar = vtable.symboltable.is_declared(identifier, classScope, funScope);               //is declared function is use because it returns variable found and not to see if var exist(Done in typechecking)
        String type = vtable.convert_type(myvar.get_type());

        if(vtable.is_var_local(classScope, funScope, myvar) == false){                              //access variable only if it is from class otherwise store the value only
            identifier = "_" + (regcount +1);
            regcount = vtable.load_class_var(myvar, regcount, false);
        }

        Vars expvar = vtable.symboltable.is_declared(exp, classScope, funScope);

        if(expvar != null){

            if(vtable.is_var_local(classScope, funScope, expvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(expvar, regcount);

            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(expvar, regcount, true);
            }
        }
        vtable.write("\tstore " + type + " " + exp + ", " + type + "* %" + identifier + "\n\n");

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
        vtable.write("\t; Array Assignment\n");
        String name = n.f0.accept(this,null);

        Vars myvar = vtable.symboltable.is_declared(name, classScope, funScope);
        if(vtable.is_var_local(classScope, funScope, myvar) == true){
            name = "%_" + regcount;
            regcount = vtable.load_local_var(myvar, regcount);
        }
        else{
            name = "%_" + (regcount + 2);
            regcount = vtable.load_class_var(myvar, regcount, true);                            //in array assignment the array needs to be loaded not just access to it
        }

        
        String exp = n.f2.accept(this,null);

        Vars expvar = vtable.symboltable.is_declared(exp, classScope, funScope);

        if(expvar != null){
            if(vtable.is_var_local(classScope, funScope, expvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(expvar, regcount);
            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(expvar, regcount, true);
            }
        }

        vtable.write("\t%_" + regcount + " = load i32, i32* " + name + "\n");
        regcount++;
        
        //store labels to variables because in case a program has nested contitions and labels get mixed up
        int label1 = labelif;
        int label2 = labelif+1;
        int label3 = labelif+2;
        labelif = labelif + 3;

        //icmp unsigned to check if index is out of bounds
        vtable.write("\t%_" + regcount + " = icmp ult i32 " + exp + ", %_" + (regcount-1) + "\n");
        regcount++;
        vtable.write("\tbr i1 %_" + (regcount-1) + ", label %oob" + label1 +", label %oob" + label2 + "\n\n");
        vtable.write("oob" + label1 + ":\n");
        vtable.write("\t%_" + regcount + " = add i32 " + exp + ", 1\n");                                            //because size is in first position add 1 to get the element
        regcount++;
        vtable.write("\t%_" + regcount + " = getelementptr i32, i32* " + name + ", i32 %_" + (regcount-1) + "\n");     //get position to store the value
        
        //labelif = labelif +2;
        int element = regcount;
        regcount++;

        String exp2 = n.f5.accept(this,null);

        Vars exp2var = vtable.symboltable.is_declared(exp2, classScope, funScope);

        if(exp2var != null){
            if(vtable.is_var_local(classScope, funScope, exp2var) == true){
                exp2 = "%_" + regcount;
                regcount = vtable.load_local_var(exp2var, regcount);
            }
            else{
                exp2 = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(exp2var, regcount, true);
            }
        }
        if(myvar.get_type() == "boolean[]"){
            vtable.write("\t%_" + regcount + " = zext i1 " + exp2 + " to i32\n");
            exp2 = "%_" + regcount;
            regcount++;   
        }
        vtable.write("\tstore i32 " + exp2 + ", i32* %_" + element + "\n");                                     //store value to element
        vtable.write("\tbr label %oob" + label3 + "\n\n");
        vtable.write("oob" + label2 + ":\n");
        vtable.write("\tcall void @throw_oob()\n");                                                            //prints out of bounds and program stops
        vtable.write("\tbr label %oob" + label3 + "\n\n");
        vtable.write("oob" + label3 + ":\n");

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

        vtable.write("\t; If Statment\n");

        String exp = n.f2.accept(this,null);
        Vars myvar = vtable.symboltable.is_declared(exp, classScope, funScope);

        if(myvar != null){
            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);
            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, true);   
            }
        }

        int label1 = labelif;
        int label2 = labelif + 1;
        int label3 = labelif + 2;
        labelif = labelif + 3;

        vtable.write("\tbr i1 " + exp + ", label %iflabel" + label1 + ", label %ellabel" + label2 + "\n\n");
        vtable.write("iflabel" + label1 + ":\n");

        n.f4.accept(this,null);                                                                     //true statments
        vtable.write("\tbr label %exitif" + label3 + "\n\n");
        vtable.write("ellabel" + label2 + ":\n");
        n.f6.accept(this,null);                                                                     //false statments
        vtable.write("\tbr label %exitif" + label3 + "\n\n");
        vtable.write("exitif" + label3 + ":\n\n");
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

        vtable.write("\t; While Statment\n");

        int label1 = labelwhile;
        int label2 = labelwhile + 1;
        int label3 = labelwhile + 2;
        labelwhile = labelwhile + 3;  

        vtable.write("\tbr label %loop" + label1 + "\n");                                       //needed to jump to loop label for program to work
        vtable.write("loop" + label1 + ":\n");
        String exp = n.f2.accept(this,null);

        Vars myvar = vtable.symboltable.is_declared(exp, classScope, funScope);

        if(myvar != null){
            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);
            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, true);
            }
        }


        vtable.write("\tbr i1 " + exp + ", label %statments" + label2 + ", label %exitwhile" + label3 + "\n");              //while condition
        vtable.write("statments" + label2 + ":\n");
        n.f4.accept(this,null);
        vtable.write("\tbr label %loop" + label1 + "\n");                                                                   //jump to begining of loop
        vtable.write("exitwhile" + label3 + ":\n");
    
        
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
        String exp = n.f2.accept(this,null);
        Vars myvar = vtable.symboltable.is_declared(exp, classScope, funScope);

        if(myvar != null){

            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);

            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, true);
            }
        }
        
        vtable.write("\n\tcall void (i32) @print_int(i32 " + exp +")\n");

        return null;
    }

   /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */

    //could use llvm and operator, examples had implemented and with phi so i did it like this too
    public String visit(AndExpression n, String argu) throws Exception {

        String left = n.f0.accept(this,null);
        
        Vars leftvar = vtable.symboltable.is_declared(left, classScope, funScope);
        if(leftvar != null){
            if(vtable.is_var_local(classScope, funScope, leftvar) == true){
                left = "%_" + regcount;
                regcount = vtable.load_local_var(leftvar, regcount);

            }
            else{
                left = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(leftvar, regcount, true);
            }
        }

        int label1 = labelif;
        int label2 = labelif + 1;
        int label3 = labelif + 2;
        int label4 = labelif + 3;
        labelif = labelif + 4;

        vtable.write("\tbr label %andStart" + label1 + "\n\n");                                                     //needed to run program
        vtable.write("andStart" + label1 + ":\n");
        vtable.write("\tbr i1 " + left + ", label %truelabel" + label2 + ", label %exitlabel" + label4 + "\n\n");   
        vtable.write("truelabel" + label2 + ":\n");
        
        String right = n.f2.accept(this,null);

        Vars rightvar = vtable.symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null){
            if(vtable.is_var_local(classScope, funScope, rightvar) == true){
                right = "%_" + regcount;
                regcount = vtable.load_local_var(rightvar, regcount);
            }
            else{
                right = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(rightvar, regcount, true);
            }
        }

        vtable.write("\tbr label %rightexp" + label3 + "\n\n");
        vtable.write("rightexp" + label3 + ":\n");
        vtable.write("\tbr label %exitlabel" + label4 + "\n\n");
        vtable.write("exitlabel" + label4 + ":\n");

        //if expression is false program will not pass the block rightexp label so phi will return 0
        //if expression is true phi will return 1 because program passes rightexpr label block 
        vtable.write("\t%_" + regcount + " = phi i1 [0, %andStart" + label1 + "], [" + right + ", %rightexp" + label3 +"]\n\n");

        regcount++;
        return "%_" + (regcount-1);



    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public String visit(CompareExpression n, String argu) throws Exception {

        String left = n.f0.accept(this,null);
        String right = n.f2.accept(this,null);
        
        Vars leftvar = vtable.symboltable.is_declared(left, classScope, funScope);
        if(leftvar != null){
            if(vtable.is_var_local(classScope, funScope, leftvar) == true){
                left = "%_" + regcount;
                regcount = vtable.load_local_var(leftvar, regcount);

            }
            else{
                left = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(leftvar, regcount, true);
            }
        }

        Vars rightvar = vtable.symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null){
            if(vtable.is_var_local(classScope, funScope, rightvar) == true){
                right = "%_" + regcount;
                regcount = vtable.load_local_var(rightvar, regcount);
            }
            else{
                right = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(rightvar, regcount, true);
            }
        }
        //slt = signed less than
        vtable.write("\t%_" + regcount + " = icmp slt i32 " + left + ", " + right + "\n");
        regcount++;
        return "%_" + (regcount - 1);
    }
    
   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public String visit(PlusExpression n, String argu) throws Exception {

        String left = n.f0.accept(this,null);
        String right = n.f2.accept(this,null);
        
        Vars leftvar = vtable.symboltable.is_declared(left, classScope, funScope);
        if(leftvar != null){
            if(vtable.is_var_local(classScope, funScope, leftvar) == true){
                left = "%_" + regcount;
                regcount = vtable.load_local_var(leftvar, regcount);

            }
            else{
                left = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(leftvar, regcount, true);
            }
        }

        Vars rightvar = vtable.symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null){
            if(vtable.is_var_local(classScope, funScope, rightvar) == true){
                right = "%_" + regcount;
                regcount = vtable.load_local_var(rightvar, regcount);

            }
            else{
                right = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(rightvar, regcount, true);
            }
        }
        
        vtable.write("\t%_" + regcount + " = add i32 " + left + ", "+ right + "\n");
        regcount++;

        return "%_" + (regcount - 1);
    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public String visit(MinusExpression n, String argu) throws Exception {

        String left = n.f0.accept(this,null);
        String right = n.f2.accept(this,null);
        
        Vars leftvar = vtable.symboltable.is_declared(left, classScope, funScope);
        if(leftvar != null){
            if(vtable.is_var_local(classScope, funScope, leftvar) == true){
                left = "%_" + regcount;
                regcount = vtable.load_local_var(leftvar, regcount);

            }
            else{
                left = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(leftvar, regcount, true);
            }
        }

        Vars rightvar = vtable.symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null){
            if(vtable.is_var_local(classScope, funScope, rightvar) == true){
                right = "%_" + regcount;
                regcount = vtable.load_local_var(rightvar, regcount);

            }
            else{
                right = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(rightvar, regcount, true);
            }
        }
        
        vtable.write("\t%_" + regcount + " = sub i32 " + left + ", "+ right + "\n");
        regcount++;

        return "%_" + (regcount - 1);
    }


   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    public String visit(TimesExpression n, String argu) throws Exception {

        String left = n.f0.accept(this,null);
        String right = n.f2.accept(this,null);
        
        Vars leftvar = vtable.symboltable.is_declared(left, classScope, funScope);
        if(leftvar != null){
            if(vtable.is_var_local(classScope, funScope, leftvar) == true){
                left = "%_" + regcount;
                regcount = vtable.load_local_var(leftvar, regcount);

            }
            else{
                left = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(leftvar, regcount, true);
            }
        }

        Vars rightvar = vtable.symboltable.is_declared(right, classScope, funScope);
        if(rightvar != null){
            if(vtable.is_var_local(classScope, funScope, rightvar) == true){
                right = "%_" + regcount;
                regcount = vtable.load_local_var(rightvar, regcount);

            }
            else{
                right = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(rightvar, regcount, true);
            }
        }
        
        vtable.write("\t%_" + regcount + " = mul i32 " + left + ", "+ right + "\n");
        regcount++;

        return "%_" + (regcount - 1);
    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public String visit(ArrayLookup n, String argu) throws Exception {

        vtable.write("\t; Array Lookup\n");
        String name = n.f0.accept(this,null);
 
        String artype = arraytype;                                                  //needed for case (new array[size])[index]
        Vars myvar = vtable.symboltable.is_declared(name, classScope, funScope);
        if(myvar != null){
            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                artype = myvar.get_type();
                name = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);
            }
            else{
                artype = myvar.get_type();
                name = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, true);
            }
        }
        String exp = n.f2.accept(this,null);

        Vars expvar = vtable.symboltable.is_declared(exp, classScope, funScope);
        
        if(expvar != null){
            if(vtable.is_var_local(classScope, funScope, expvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(expvar, regcount);

            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(expvar, regcount, true);
            }
        }

        int label1 = labelif;
        int label2 = labelif + 1;
        int label3 = labelif + 2;
        labelif = labelif + 3;

        vtable.write("\t%_" + regcount + " = load i32, i32* " + name + "\n");
        regcount++;
    
        vtable.write("\t%_" + regcount + " = icmp ult i32 " + exp + ", %_" + (regcount-1) + "\n");
        regcount++;
        vtable.write("\tbr i1 %_" + (regcount-1) + ", label %oob" + label1 +", label %oob" + label2 + "\n\n");
        vtable.write("oob" + label1 + ":\n");
        vtable.write("\t%_" + regcount + " = add i32 " + exp + ", 1\n");
        regcount++;
        vtable.write("\t%_" + regcount + " = getelementptr i32, i32* " + name + ", i32 %_" + (regcount-1) + "\n");
        regcount++;

        vtable.write("\t%_" + regcount + " = load i32, i32* %_" + (regcount-1) + "\n") ;
        regcount++;

        vtable.write("\tbr label %oob" + label3 + "\n\n");
        vtable.write("oob" + label2 + ":\n");
        vtable.write("\tcall void @throw_oob()\n");
        vtable.write("\tbr label %oob" + label3 + "\n\n");
        vtable.write("oob" + label3 + ":\n");

        if(artype == "boolean[]"){                                                              //since boolean arrays are i32 type need to convert value from i1 to i32 before returning it
            vtable.write("\t%_" + regcount + " = trunc i32 %_" + (regcount-1) + " to i1\n");
            regcount++;   
        }
        return "%_" + (regcount-1);
     }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public String visit(ArrayLength n, String argu) throws Exception {
        String exp = n.f0.accept(this,null);

        Vars myvar = vtable.symboltable.is_declared(exp, classScope, funScope);
        
        if(myvar != null){
            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);

            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, true);
            }
        }

        vtable.write("\t%_" + regcount + " = load i32, i32* " + exp +"\n");             //size in an array is in first position 
        regcount++;

        return "%_" + (regcount-1);
        
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
        String name = n.f0.accept(this,null);
        Vars myvar = vtable.symboltable.is_declared(name, classScope, funScope);
        String classname = classtype;                                               //needed for case (new class()).function()
        
        if(myvar != null){
            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                classname = myvar.get_type();
                name = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);

            }
            else{
                classname = myvar.get_type();
                name = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, true);
            }
        }

        String funname = n.f2.accept(this,null);
        n.f4.accept(this,null);

        regcount = vtable.function_call(classname, funname, name, regcount,args_regs,classScope,funScope);
        args_regs.clear();//clear list for next time message send is called

        return "%_" + (regcount - 1);
    }

    /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
    public String visit(ExpressionList n, String argu) throws Exception {
        String exp = n.f0.accept(this,null);
        args_regs.add(exp);                         //just add exp, it doesnt matter if its variable or register this will be delt with in vtable.function_call function
        n.f1.accept(this,null);

        return null;
    }

    /**
     * f0 -> ","
    * f1 -> Expression()
    */
    public String visit(ExpressionTerm n, String argu) throws Exception {
        String exp = n.f1.accept(this,null);
        args_regs.add(exp);

        return null;
    }

   /**
    * f0 -> "this"
    */
    public String visit(ThisExpression n, String argu) throws Exception {
        classtype = classScope;
        return "%this";
    }

   /**
    * f0 -> "new"
    * f1 -> "boolean"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    public String visit(BooleanArrayAllocationExpression n, String argu) throws Exception {//same in int array allocation
        arraytype = "boolean[]";
        vtable.write("\t; Boolean Array Allocation\n");
        String exp = n.f3.accept(this,null);
        Vars myvar = vtable.symboltable.is_declared(exp, classScope, funScope);
        
        if(myvar != null){
            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);

            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, false);
            }
        }

        int label1 = labelif;
        int label2 = labelif + 1;
        labelif = labelif + 2;

        vtable.write("\t%_" + regcount + " = icmp slt i32 " + exp +  ", 0\n");                  //check if size is >0
        regcount++;
    
        vtable.write("\tbr i1 %_" + (regcount-1) + ", label %arr_alloc" + label1 + ", label %arr_alloc" + label2 + "\n\n");
        vtable.write("arr_alloc" + label1 + ":\n");
        vtable.write("\tcall void @throw_oob()\n");
        vtable.write("\tbr label %arr_alloc" + label2 + "\n\n");
        vtable.write("arr_alloc" + label2 + ":\n");
        vtable.write("\t%_" + regcount + " = add i32 " + exp + ", 1\n");
        regcount++;
        vtable.write("\t%_" + regcount + " = call i8* @calloc(i32 4, i32 %_" + (regcount-1) + ")\n");           //allocate size + 1 posotion because in 0 position size will be stored
        regcount++;
        vtable.write("\t%_" + regcount + " = bitcast i8* %_" + (regcount-1) + " to i32*\n");
        regcount++;
        vtable.write("\tstore i32 " + exp + ", i32* %_" + (regcount-1) + "\n\n");

        return "%_" + (regcount-1);

    }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    public String visit(IntegerArrayAllocationExpression n, String argu) throws Exception {//same as boolean array allocation

        arraytype = "int[]";//maybe delete
        vtable.write("\t; Int Array Allocation\n");
        String exp = n.f3.accept(this,null);
        Vars myvar = vtable.symboltable.is_declared(exp, classScope, funScope);
        
        if(myvar != null){
            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);

            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, true);
            }
        }

        int label1 = labelif;
        int label2 = labelif + 1;
        labelif = labelif + 2;

        vtable.write("\t%_" + regcount + " = icmp slt i32 " + exp +  ", 0\n");
        regcount++;
    
        vtable.write("\tbr i1 %_" + (regcount-1) + ", label %arr_alloc" + label1 + ", label %arr_alloc" + label2 + "\n\n");
        vtable.write("arr_alloc" + label1 + ":\n");
        vtable.write("\tcall void @throw_oob()\n");
        vtable.write("\tbr label %arr_alloc" + label2 + "\n\n");
        vtable.write("arr_alloc" + label2 + ":\n");
        vtable.write("\t%_" + regcount + " = add i32 " + exp + ", 1\n");
        regcount++;
        vtable.write("\t%_" + regcount + " = call i8* @calloc(i32 4, i32 %_" + (regcount-1) + ")\n");
        regcount++;
        vtable.write("\t%_" + regcount + " = bitcast i8* %_" + (regcount-1) + " to i32*\n");
        regcount++;
        vtable.write("\tstore i32 " + exp + ", i32* %_" + (regcount-1) + "\n\n");
 
        return "%_" + (regcount-1);
    }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    public String visit(AllocationExpression n, String argu) throws Exception {
        String name = n.f1.accept(this,argu);
        classtype = name;
        vtable.allocate_class(name, regcount);
        regcount = regcount + 3;                    //allocation_class uses 3 register so update counter
        return "%_" + (regcount - 3);               //class will be stored in register given to allocate_class 
    }

    /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    public String visit(NotExpression n, String argu) throws Exception {
        String exp = n.f1.accept(this,null);

        Vars myvar = vtable.symboltable.is_declared(exp, classScope, funScope);
        
        if(myvar != null){
            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);

            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, true);
            }
        }

        vtable.write("\t%_" + regcount + " = xor i1 1, " + exp + "\n");             //not expression = xor 1, expression
        regcount++;

        return "%_" + (regcount - 1);
    }

    /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public String visit(BracketExpression n, String argu) throws Exception {
        String exp = n.f1.accept(this,null);
        Vars myvar = vtable.symboltable.is_declared(exp, classScope, funScope);
        
        if(myvar != null){
            if(vtable.is_var_local(classScope, funScope, myvar) == true){
                exp = "%_" + regcount;
                regcount = vtable.load_local_var(myvar, regcount);

            }
            else{
                exp = "%_" + (regcount + 2);
                regcount = vtable.load_class_var(myvar, regcount, true);
            }
        }

        return exp;

    }

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
        String number = n.f0.toString();
        return number;
    }

   /**
    * f0 -> "true"
    */
    public String visit(TrueLiteral n, String argu) throws Exception {
        super.visit(n, argu);
        return "1";
    }

    /**
     * f0 -> "false"
    */
    public String visit(FalseLiteral n, String argu) throws Exception {
        super.visit(n, argu);
        return "0";
    }
}
