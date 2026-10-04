import java.lang.reflect.Method;

class Student {

    private static void displayInfo(String[] args) {
        System.out.println("Student Information");
    }

    public static void runPrivateMain() throws Exception {
        Method m = Student.class.getDeclaredMethod("displayInfo", String[].class);
        m.setAccessible(true);
        m.invoke(null, (Object) new String[]{});
    }

    public static void main(String[] args) throws Exception {
        runPrivateMain();
    }
}


    