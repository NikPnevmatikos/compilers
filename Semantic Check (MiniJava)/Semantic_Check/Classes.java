import java.util.*;

public class Classes{
    String name;
    String parent;
    Map<String, Vars> vars;
    List<String> vars_in_order;                                 //to hold variables in right order
    int childstartoffset;                                       //variable offset past to child if exist
    Map<String, Functions> functions;
    List<String> functions_in_order;                            //to hold functions in right order
    int childfunoffset;                                         //function offset past to child class if exist

    public Classes(String name, String parent){
        this.name = name;
        this.parent = parent;
        this.vars = new TreeMap<>();
        vars_in_order = new ArrayList<>();
        this.functions = new TreeMap<>();
        functions_in_order = new ArrayList<>();
        childstartoffset = 0;
        childfunoffset = 0;
    }

    //returns parent name
    String get_parent(){
        return this.parent;
    }

    //check if function name exist 
    boolean exist(String funname){
        if (functions.get(funname) == null){
            return false;
        }
        return true;
    }

    //add function to map and list
    void add_fun(String type, String name, String owner){
        functions_in_order.add(name);
        this.functions.put(name, new Functions(type, name, owner));
    }

    //returns function with name given
    Functions get_fun(String funname){
        return functions.get(funname);
    }

    //check if variable exist inside class
    boolean var_exist(String varname){
        if(vars.get(varname) == null){
            return false;
        }
        return true;
    }

    //check if function name has given argument name
    boolean arg_exist(String argname , String funname){
        return functions.get(funname).arg_exist(argname);
    }

    //add variable to map and list
    void add_var(String type, String name){
        vars_in_order.add(name);
        this.vars.put(name, new Vars(type, name));
    }

    //add function argument
    void add_funarg(String type, String name, String funname){
        this.functions.get(funname).add_args(type, name);
    }

    //add function variable 
    void add_funvar(String type, String name, String funname){
        this.functions.get(funname).add_vars(type, name);
    }

    //check if variable exist
    Vars is_declared(String varname, String funname){
        if(functions.get(funname).exist(varname) == false){
            return null;
        }
        return functions.get(funname).get_var(varname);
    }

    //set and print variables offsets
    void set_var_offset(int startingoffset){
        System.out.println("---Variables---");
        if(vars_in_order.size() > 0){                               //if class has declared variables
            Vars myvar = vars.get(vars_in_order.get(0));
            
            myvar.set_offset(startingoffset);                       //if class has a parent starting offset is set by last variable of parent
            System.out.println(this.name + "." + myvar.name + " : " + startingoffset);
            int nextoff = startingoffset + myvar.next_offset();
            if(vars_in_order.size() == 1){
                childstartoffset = nextoff;
            }

            for(int i = 1; i < vars_in_order.size(); i++){
                myvar = vars.get(vars_in_order.get(i));
                myvar.set_offset(nextoff);
                System.out.println(this.name + "." + myvar.name + " : " + nextoff);
                nextoff = nextoff + myvar.next_offset();
                if(i == vars_in_order.size() - 1){
                    childstartoffset = nextoff;
                }
            }
        }
    }

    //set abd print function offsets
    void set_fun_offset(int startingoffset, List<Map<String,Functions>> parfunctions){
        System.out.println("---Methods---");
        if(functions_in_order.size() > 0){
            Boolean found = false;
            int nextoff = startingoffset;
            Functions myfun = functions.get(functions_in_order.get(0));
            for(int i = 0; i < parfunctions.size(); i++){
                if(parfunctions.get(i).get(myfun.name) != null){                            //circle throw parents function to see if function already has offset
                    found = true;                                                           //if found skip this function                             
                    break;
                }
            }
            if(found == false){                                                             //if not found set and print offsets
                myfun.set_offset(startingoffset);
                System.out.println(this.name + "." + myfun.name + " : " + startingoffset);
                nextoff = nextoff + 8;
            }

            if(functions_in_order.size() == 1){
                childfunoffset = nextoff;
            }

            for(int i = 1; i < functions_in_order.size(); i++){
                myfun = functions.get(functions_in_order.get(i));
                found = false;
                for(int j = 0; j < parfunctions.size(); j++){
                    if(parfunctions.get(j).get(myfun.name) != null){
                        found = true;
                        break;
                    }
                }
                if(found == false){
                    myfun.set_offset(nextoff);
                    System.out.println(this.name + "." + myfun.name + " : " + nextoff);
                    nextoff = nextoff + 8;
                }
                if(i == functions_in_order.size() - 1){
                    childfunoffset = nextoff;
                }
            }
        }
    }
}
