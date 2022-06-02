import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class Vtable {
    
    FileWriter myWriter;
    SymbolTable symboltable;
    Map<String,List<Functions>> classmethods;

    public Vtable(SymbolTable symboltable, String filename){
        filename = filename.replace(".java", ".ll");

        try {
            myWriter = new FileWriter(filename);                    //if file already exist it will overide
        } 
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        classmethods = new TreeMap<>();
        this.symboltable = symboltable;
    }

    public void close_file(){                            
        try{
            myWriter.close();
        }
        catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();           
        }
    }

    public void write(String buf){
        try{
            myWriter.write(buf);
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public String convert_type(String type){
        if(type == "int"){
            return "i32";
        }
        else if(type == "boolean"){
            return "i1";
        }
        else if(type == "int[]" || type == "boolean[]"){                                    //boolean arrays are also implemated as i32* type
            return "i32*";
        }
        else{
            return "i8*";
        }
    }

    void create_vtables(){
        for (int i = 1; i < symboltable.classes_in_order.size(); i++){//for each class
            Classes cur = symboltable.classes.get(symboltable.classes_in_order.get(i));
            classmethods.put(cur.name, new ArrayList<>());
            int insertpos = 0;
            String classname = cur.name;

            while(cur != null){
                for(int j = 0; j < cur.functions_in_order.size(); j++){             //insert all function that the class has access
                    boolean found = false;
                    for(int k = 0; k < classmethods.get(classname).size(); k++){
                        Functions myfun = classmethods.get(classname).get(k);
                        if(myfun.name == cur.functions_in_order.get(j)){                //if a function exist in parent too
                                                                                        //need to add child function to the position that the parent function would be stored
                            classmethods.get(classname).remove(myfun);
                            classmethods.get(classname).add(insertpos, myfun);
                            insertpos++;
                            found = true;
                            break;
                        }
                    }
                    if(found == false){
                        classmethods.get(classname).add(insertpos, cur.get_fun(cur.functions_in_order.get(j)));

                        insertpos++;
                    }
                }
                if(cur.get_parent() != null){                                       
                    insertpos = 0;                                              //parents functions are before child 
                    cur = symboltable.classes.get(cur.get_parent());
                }
                else{
                    break;
                }

            }
        }
        String buf;
        //i = 0 is main 
        for (int i = 1; i < symboltable.classes_in_order.size(); i++){              //for each class print in file the vtables

            List<Functions> mylist = classmethods.get(symboltable.classes_in_order.get(i));
            buf = "@."+ symboltable.classes_in_order.get(i) + "_vtable = global ["+ mylist.size() + " x i8*] [\n";
            this.write(buf);

            for (int j = 0; j < mylist.size(); j++){
                Functions myfun = mylist.get(j);
                buf = "\ti8* bitcast (";
                buf = buf + this.convert_type(myfun.get_type()) + " (i8*";

                for(int k = 0; k < myfun.args_in_order.size(); k++){

                    buf = buf + ", " + this.convert_type(myfun.args_in_order.get(k));
                }
                Classes cur = symboltable.classes.get(symboltable.classes_in_order.get(i));
                while(cur != null){                                                             
                    if(cur.exist(myfun.name)){                                                    //if current class does not own the function class parent will be put before function name
                        break;
                    }
                    if(cur.get_parent() != null){
                        cur = symboltable.classes.get(cur.get_parent());
                    }
                    else{
                        break;
                    }
                }
                buf = buf + ")* @" + cur.name + "." + myfun.name + " to i8*)";
                if(j != mylist.size() - 1){
                    buf = buf + ",\n";
                }
                this.write(buf);
            }
            this.write("\n]\n\n");
        
        }


        //all programs have this 
        this.write("declare i8* @calloc(i32, i32)\n");
        this.write("declare i32 @printf(i8*, ...)\n");
        this.write("declare void @exit(i32)\n\n");
        
        this.write("@_cint = constant [4 x i8] c\"%d\\0a\\00\" \n");
        this.write("@_cOOB = constant [15 x i8] c\"Out of bounds\\0a\\00\"\n\n");
        
        this.write("define void @print_int(i32 %i) {\n");
        this.write("\t%_str = bitcast [4 x i8]* @_cint to i8*\n");
        this.write("\tcall i32 (i8*, ...) @printf(i8* %_str, i32 %i)\n");
        this.write("\tret void\n}\n\n");

        this.write("define void @throw_oob() {\n");
        this.write("\t%_str = bitcast [15 x i8]* @_cOOB to i8*\n");
        this.write("\tcall i32 (i8*, ...) @printf(i8* %_str)\n");
        this.write("\tcall void @exit(i32 1)\n");
        this.write("\tret void\n}\n\n");
    }


    //in a function arguments are allocatin in register and values are store in this registers
    void alocate_args(String classname, String funname){
        Classes cur = symboltable.get_class(classname);
        Functions myfun = cur.get_fun(funname);
        String buf;
        this.write("\n");
        for (int i = 0; i < myfun.args_names.size(); i++){
            Vars myvar = myfun.get_var(myfun.args_names.get(i));

            buf = "\t%" + myvar.name + " = alloca " + this.convert_type(myvar.get_type()) + "\n";
            buf = buf + "\tstore " + this.convert_type(myvar.get_type()) + " %." + myvar.name + ", " + this.convert_type(myvar.get_type()) + "* %" + myvar.name + "\n\n";
            this.write(buf);

        }

    }

    void allocate_class(String classname, int regcount){

        this.write("\t; Allocating object type " + classname + "\n");
        int size = symboltable.classes.get(classname).childstartoffset + 8;                         //in previus exersise childstartoffset was the offset of the first variable
                                                                                                    //a posible child would have, in this case it acts as the size of each class
                                                                                                    // + 8 for the vtable
        this.write("\t%_" + regcount + " = call i8* @calloc(i32 1, i32 " + size + ")\n");
        this.write("\t%_" + (regcount+1) + " = bitcast i8* %_" + regcount + " to i8***\n");
        size = classmethods.get(classname).size();
        this.write("\t%_" + (regcount+2) + " = getelementptr [" + size + " x i8*], [" + size + " x i8*]* @." + classname +"_vtable, i32 0, i32 0\n");
        this.write("\tstore i8** %_"+ (regcount+2) +", i8*** %_"+ (regcount+1) +"\n");

    }

    int function_call(String classname, String funname, String classreg, int regcount, List<String> arg_regs, String classScope, String FunScope){
        
        Functions myfun = symboltable.get_class(classname).get_fun(funname);

        if(myfun == null){                                                      //if function is not defined in current class get function from parents
            for (int i = 0; i < classmethods.get(classname).size(); i++){
                myfun = classmethods.get(classname).get(i);
                if(myfun.name == funname){
                    break;
                }

            }
        }

        int offset = classmethods.get(classname).indexOf(myfun);

        this.write("\t; " + classname + "." + funname + "(): "+ offset +"\n");

        this.write("\t%_" + regcount + " = bitcast i8* " + classreg + " to i8***\n");
        this.write("\t%_" + (regcount+1) + " = load i8**, i8*** %_" + regcount + "\n");
        this.write("\t%_" + (regcount+2) + " = getelementptr i8*, i8** %_" + (regcount+1) + ", i32 " + offset + "\n");
        this.write("\t%_" + (regcount+3) + " = load i8*, i8** %_" + (regcount+2) + "\n");                               //load function address

        
        String type = this.convert_type(myfun.get_type());
        String buf = "\t%_" + (regcount+4) + " = bitcast i8* %_" + (regcount+3) + " to " + type + " (i8*";


        for(int i = 0; i < myfun.args_in_order.size(); i++){
            buf = buf + ", ";
            buf = buf + this.convert_type(myfun.args_in_order.get(i));
        }
        buf = buf + ")*\n";
        this.write(buf);

        int funreg = regcount + 4;
        regcount = regcount + 5;
        for(int i = 0; i < arg_regs.size(); i++){                                           //for each argument check if its a register or variable
            Vars reg = symboltable.is_declared(arg_regs.get(i), classScope, FunScope);
            if(reg != null){
                if(this.is_var_local(classScope, FunScope, reg) == true){
                    arg_regs.set(i, ("%_" + regcount));
                    regcount = this.load_local_var(reg, regcount);
                }
                else{
                    arg_regs.set(i, ("%_" + (regcount + 2)));
                    regcount = this.load_class_var(reg, regcount, true);
                }
            }
        }

        buf  = "\t%_" + regcount + " = call " + type + " %_" + funreg + "(i8* " + classreg;
        for(int i = 0; i < arg_regs.size(); i++){
            buf = buf + ",";
            buf = buf + this.convert_type(myfun.args_in_order.get(i)) + " ";
            buf = buf + arg_regs.get(i);
        }
        buf = buf + ")\n";
        regcount++;

        this.write(buf);

        return regcount;

    }

    //returns true if variable is declared inside a function else false
    boolean is_var_local(String classname, String funname, Vars myvar){

        Functions myfun = symboltable.classes.get(classname).get_fun(funname);
        if(myfun.exist(myvar.name) == true){
            return true;
        }
        return false;
    
    }

    //each time we use a variable we need to load its value to a register (exept assigment to this variable)
    int load_local_var(Vars myvar, int regcount){
        String type = this.convert_type(myvar.get_type());
        this.write("\t%_" + regcount + " = load " + type + ", " + type + "* %" + myvar.name + "\n");
        regcount++;

        return regcount;
    }

    int load_class_var(Vars myvar, int regcount, boolean loadvalue){
        String type = this.convert_type(myvar.get_type());

        //access variable address
        this.write("\t%_" + regcount + " = getelementptr i8, i8* %this, i32 " + (myvar.offset+8) + "\n");
        regcount++;
        this.write("\t%_" + regcount + " = bitcast i8* %_" + (regcount-1) + " to "+ type +"*\n");
        regcount++;
        //variable dont always needs to be loaded (case assigment for example)
        if(loadvalue == true){                                  
            this.write("\t%_" + regcount + " = load " + type + ", " + type + "* %_" + (regcount-1) + "\n");
            regcount++;
        }

        return regcount;
    }
}
