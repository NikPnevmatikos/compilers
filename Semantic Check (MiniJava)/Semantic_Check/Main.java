import syntaxtree.*;
import visitor.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        
        if(args.length < 1){
            System.err.println("Usage: java Main <inputFile>");
            System.exit(1);
        }

        for (int i = 0; i < args.length; i++){

            FileInputStream fis = null;
            try{
                fis = new FileInputStream(args[i]);
                MiniJavaParser parser = new MiniJavaParser(fis);

                Goal root = parser.Goal();

                System.err.println("Program parsed successfully.");

                SymbolTable symboltable = new SymbolTable();

                MyVisitor eval = new MyVisitor(symboltable);
                root.accept(eval, null);

                Typevisitor typecheck = new Typevisitor(symboltable);
                root.accept(typecheck, null);

                System.err.println("Program passed Type Cheking.\n\n");

                symboltable.set_and_print_offsets();

            }
            catch(ParseException ex){
                System.out.println(ex.getMessage());
            }
            catch(RuntimeException ex){
                System.err.println(ex.getMessage());
            }
            catch(FileNotFoundException ex){
                System.err.println(ex.getMessage());
            }
            finally{
                try{
                    if(fis != null) fis.close();
                }
                catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
            }
            if(i < args.length - 1){
                System.out.println("\n\nNext Program\n\n");
            }        
        }
    }
}


class MyVisitor extends GJDepthFirst<String, String>{
    
    SymbolTable symboltable;
    String camefrom;                                    //usefull in vardeclarations 
                                                        //to know if variable is declared in class or function

    public MyVisitor(SymbolTable st){
        symboltable = st;
        camefrom = null;
    }
    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    @Override
    public String visit(MainClass n, String argu) throws Exception {
        String classname = n.f1.accept(this, argu);

        if(symboltable.exist(classname) == true){                                   //Class cannot have same name
            throw new RuntimeException("Redeclaration of class " + classname);
        }
        else{
            symboltable.add_class(classname, null);
            symboltable.add_fun("void", "main", classname);
            camefrom = "Function";
            n.f14.accept(this,argu);
        }


        return null;
 
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    @Override
    public String visit(ClassDeclaration n, String argu) throws Exception {
        String classname = n.f1.accept(this, null);

        if(symboltable.exist(classname) == true){                                          //class cannot have same name
            throw new RuntimeException("Redeclaration of class name " + classname);
        }
        else{
            camefrom = "Class";                                                            //when we visit vardeclaration we know variable declared in class
            symboltable.add_class(classname, null);
            n.f3.accept(this, argu);
            n.f4.accept(this, argu);   
        }

        return null;
     }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    @Override
    public String visit(ClassExtendsDeclaration n, String argu) throws Exception {
        String classname = n.f1.accept(this, null);
        String parentname = n.f3.accept(this, null);

        if(symboltable.exist(classname) == true){
            throw new RuntimeException("Redeclaration of class name " + classname);
        }
        else if(symboltable.exist(parentname) == false){                                                //cannot extent for class that does not exist
            throw new RuntimeException("class name " + parentname + " not declared in this scope");
        }
        else{
            camefrom = "Class";
            symboltable.add_class(classname, parentname);
            n.f5.accept(this, argu);
            n.f6.accept(this, argu);
        }
        return null;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    @Override
    public String visit(MethodDeclaration n, String argu) throws Exception {

        String myType = n.f1.accept(this, null);
        String myName = n.f2.accept(this, null);

        Classes myclass = symboltable.get_class(symboltable.get_classScope());

        if(myclass.exist(myName) == true){                                            //if function name have been declared again in same class
            throw new RuntimeException("Redeclaration of function name " + myName);
        }
        else{
            symboltable.add_fun(myType, myName, symboltable.get_classScope());          //first we add the function to sympol table
            camefrom = "Function";
            n.f4.accept(this, argu);                                                    //so we can add the arguments too
            
            if(symboltable.checkfun(symboltable.get_classScope(), symboltable.get_funScope()) != true){     //and check if its overloaded or not
                throw new RuntimeException("Redeclaration of function " + myName);
            }
            n.f7.accept(this, argu);
        }

        return null;
    }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    @Override
    public String visit(VarDeclaration n, String argu) throws Exception{
        String myType = n.f0.accept(this, null);
        String myName = n.f1.accept(this, null);

        String fun = null;
        if(camefrom == "Function"){                     //if variable has been declared in function checkvar and add var functions
                                                        //will have different behavior
            fun = symboltable.get_funScope();
        }
        if(symboltable.checkvar(myType,myName, symboltable.get_classScope(),fun) != true){          //check if variable has been redeclared
            throw new RuntimeException("Redeclaration of variable " + myName);
        }
        else{
            symboltable.add_var(myType, myName, symboltable.get_classScope(), fun , true);
        }
        return null;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public String visit(FormalParameter n, String argu) throws Exception{
        String type = n.f0.accept(this, null);
        String name = n.f1.accept(this, null);
        if(name != null){                                                               //if argument exist
            Classes myclass = symboltable.get_class(symboltable.get_classScope());      //get the class that has the functiom
            if(myclass.arg_exist(name,symboltable.get_funScope()) == true){             //check if argument name has been redeclared in the same function arguments
                throw new RuntimeException("Redeclaration of argument variable " + name + " in function " + symboltable.get_funScope());
            }
            else{
                symboltable.add_var(type,name,symboltable.get_classScope(),symboltable.get_funScope(),false);
            }
        }
        return null;
    }

    @Override
    public String visit(IntegerArrayType n, String argu) {
        return "int[]";
    }

    @Override
    public String visit(BooleanArrayType n, String argu){ 
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

}
