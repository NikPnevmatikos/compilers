import java.util.*;

public class SymbolTable {
    Map<String, Classes> classes;                   //class name -> class info
    List<String> classes_in_order;                  //map does not hold classes the way they enter the map so this list will hold class names in write order
    String classScope;
    String funScope;
    
    public SymbolTable(){
        classes = new TreeMap<>();
        classes_in_order = new ArrayList<>();
        classScope = null;
        funScope = null;
    }

    //if class exist
    boolean exist(String classname){                        
        if(classes.get(classname) == null){
            return false;
        }
        return true;
    }

    //returns class with given name
    Classes get_class(String name){
        return this.classes.get(name);
    }

    //return current class
    String get_classScope(){
        return classScope;
    }

    //return current function
    String get_funScope(){
        return funScope;
    }

    //Add class to map and list
    void add_class(String name, String parent){
        classes_in_order.add(name);
        classes.put(name,new Classes(name, parent));
        classScope = name;                                  //update current class
    }

    //add function 
    void add_fun(String type, String name, String classowner){
        classes.get(classowner).add_fun(type, name, classowner);
        funScope = name;                                     //update current function
    }

    //Check if a function is overloaded
    boolean checkfun(String classScope, String funScope){
        Classes current = classes.get(classScope);
        if (current.get_parent() != null){                              //if current class has a parent function might be overload
            Classes parent = classes.get(current.get_parent());
            while(parent != null){                                      //circle all parent until one or none is found with some function name
                
                if (parent.exist(funScope) == true){                    //if function name exist to parent
                    Functions cur = current.get_fun(funScope);
                    Functions par = parent.get_fun(funScope);
                    if(par.get_type() != cur.get_type()){               //if functions have different return type it is not overload
                        return false;
                    }
                    else{
                        if(cur.compare_args(par) == false){             //if functions have different arguments type it is not overload
                            return false;
                        }
                    }
                }
                if(parent.get_parent() != null){
                    parent = classes.get(parent.get_parent());
                }
                else{
                    break;
                }
            }
        }
        return true;                                                    //if code reach hear that mean fuction is ok to be declared
    }

    //Check if variable has been redeclared
    boolean checkvar(String type, String name, String classname, String funname){
        if(funname != null){                                                //if variable is declared inside function
            Functions myfun = classes.get(classname).get_fun(funname);
            if(myfun.exist(name) == true){                                  //if variable exist in function variables or arguments 
                return false;
            }
        }
        else{
            if(classes.get(classname).var_exist(name) == true){             //if variable exist in class variables
                return false;
            }
        }
        return true;
    }

    //add variable to symbol table
    void add_var(String type, String name, String classname, String funname, boolean isvardec){

        if (funname != null){                                                   //if variable is declared inside function
            if(isvardec == true){                                               //if variable is argument or var declared
                classes.get(classname).add_funvar(type, name, funname);         //add to function arguments
            }
            else{
                classes.get(classname).add_funarg(type, name, funname);         //add to function variables
            }
        }
        else{
            classes.get(classname).add_var(type, name);                         //add to class variables
        }
    }

    //check if variable has already been declared
    Vars is_declared(String varname, String classname, String Funname){
        
        Classes current = classes.get(classname);
        Vars myvar = current.is_declared(varname,Funname);                        //if variable is declared in current function
        
        if(myvar == null){                                                        //if not
            if(current.var_exist(varname) == false){                              //if variable isnt declared in current class                            
                if(current.get_parent() != null){                                 //circle throw each posible parent
                    Classes parent = classes.get(current.get_parent());
                    while(parent != null){
                        if(parent.var_exist(varname) == true){                     //check if variable exist
                            return parent.vars.get(varname);
                        }
                        if(parent.get_parent() != null){
                            parent = classes.get(parent.get_parent());
                        }
                        else{
                            break;
                        }
                    }   
                }
                return null;
            }
            return current.vars.get(varname);
        }
        return myvar;                                                       //if code reach hear variable has been found
    }

    //check if function exist in symbol table
    Functions fun_exist(String classname, String funname){
        Classes current = classes.get(classname);
        if(current.exist(funname) == false){                                //if function name does not exist in current class

            if(current.get_parent() != null){                   
                Classes parent = classes.get(current.get_parent());
                while(parent != null){                                      //circle throw parents
                    if(parent.exist(funname) == true){                      
                        return parent.get_fun(funname);
                    }
                    if(parent.get_parent() != null){
                        parent = classes.get(parent.get_parent());
                    }
                    else{
                        break;
                    }
                }   
            }
        }
        else{
            return current.get_fun(funname);
        }
        return null;
    }

    //check if a class is inherited by the other class 
    boolean checkinheritance(String child,String parent){
        Classes cur = classes.get(child);
        if(cur.get_parent() != null){                                   //if it has a parent check for possible inheritance
            Classes curpar = classes.get(cur.get_parent());
            while(curpar != null){
                if(curpar.name == parent){
                    return true;
                }
                if(curpar.get_parent() != null){
                    curpar = classes.get(curpar.get_parent());
                }
                else{
                    break;
                }
            }
        }
        return false;
    }

    //check if function arguments are the same with arguments on list
    boolean checkfunarg(List<String> funargs, List<String> argsgiven){
        if(funargs.size() != argsgiven.size()){                             //if size is different arguments are not the same
            return false;
        }
        for(int i = 0; i < funargs.size(); i++){                            //itterate list and check for each element
            if(funargs.get(i) != argsgiven.get(i)){                         //if type is different it might be because its a child of class type
                if(classes.get(funargs.get(i)) != null && classes.get(argsgiven.get(i)) != null){       //check if both are class types
                    if(checkinheritance(argsgiven.get(i), funargs.get(i)) == false){                    //if one inherits from the other
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //Final function call to set offsets in each variable and function and print them
    void set_and_print_offsets(){

        //starting from 1 because in 0 position is main class
        for(int i = 1; i < classes_in_order.size(); i++){                                       //for each class
            System.out.println("-----------Class " + classes_in_order.get(i) + "-----------");
            int startingoffset = 0;                                                             //starting value of first variable offset of current class
            int startfunoffset = 0;                                                             //starting value of first function offset of current class
            List<Map<String,Functions>> parfunctions = new ArrayList<>();                       //helping list, it has all function names of parent class

            Classes cur = classes.get(classes_in_order.get(i));
            if (cur.get_parent() != null){                                                      //if it has parent offsets must start after parent last variable and function 
                Classes par = classes.get(cur.get_parent());
                startingoffset = par.childstartoffset;
                startfunoffset = par.childfunoffset;
                while(par != null){
                    parfunctions.add(par.functions);
                    if (par.get_parent() != null){
                        par = classes.get(par.get_parent());
                    }
                    else{
                        break;
                    }
                }
            }
            cur.set_var_offset(startingoffset);                                                 //set variable offsets
            cur.set_fun_offset(startfunoffset,parfunctions);                                    //set function offsets
        }
    }
}
