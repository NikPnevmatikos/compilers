public class Vars{
    String name;
    String type;
    int offset;

    public Vars(String type, String name){
        this.name = name;
        this.type = type;
        this.offset = 0;
    }

    //return type
    String get_type(){
        return type;
    }

    //set offset with given number
    void set_offset(int offset){
        this.offset = offset;
    }

    //find next offset
    int next_offset(){
        if(type == "int"){
            return 4;               //size of int
        }
        else if(type == "boolean"){ 
            return 1;               //size of boolean
        }
        else{
            return 8;               //size of pointers
        }
    }
}