import java.util.*;

public class Functions {
    String name;
    String type;
    String owner;
    Map<String, Vars> vardec;
    Map<String, Vars> args;
    List<String> args_in_order;                                 //arguments in order
    int offset;

    public Functions(String type, String name, String owner){
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.vardec = new TreeMap<>();
        this.args = new TreeMap<>();
        this.args_in_order = new ArrayList<>();
        this.offset = 0;
    }

    //if a variable exist in arguments or declared
    boolean exist(String varname){
        if(vardec.get(varname) == null && args.get(varname) == null){
            return false;
        }
        return true;
    }

    //if argument exist
    boolean arg_exist(String argname){
        if(args.get(argname) == null){
            return false;
        }
        System.out.println(argname);
        return true;
    }

    //returns function type
    String get_type(){
        return type;
    }

    //returns variable with given name
    Vars get_var(String varname){
        if(vardec.get(varname) != null){
            return vardec.get(varname);
        }
        else{
            return args.get(varname);
        }
    }

    //add variable to map
    void add_vars(String type, String name){
        this.vardec.put(name, new Vars(type, name));
    }

    //add argument to map and list
    void add_args(String type, String name){
        this.args_in_order.add(type);
        this.args.put(name, new Vars(type, name));
    }

    //compare arguments between current function and function given
    boolean compare_args(Functions fun){
        if(this.args_in_order.size() != fun.args_in_order.size()){
            return false;
        }
        else{
            for(int i = 0; i < this.args_in_order.size(); i ++){
                if(this.args_in_order.get(i) != fun.args_in_order.get(i)){
                    return false;
                }
            }
        }
        return true;
    }

    //return arguments as list
    List<String> get_list_args(){
        return this.args_in_order;
    }

    //set offset with given number
    void set_offset(int offset){
        this.offset = offset;
    }
}
