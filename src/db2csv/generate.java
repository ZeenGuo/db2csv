package db2csv;

public class generate {
    public int a=init();
    public static int atom(int a){
        while(a<10){
            a+=1;
        }
        return a+100;
    }
    public static int bar(int a){
        if (a<10){
            a*=2;
        }
        do{
            a+=1;
        }while (a<10);
        return a+400;
    }
    public static int foo(int a, int b){
        return a+b;
    }

    public int init(){
        return 100;
    }
}

class  test1{
    int go(int a){
        return a;
    }
}