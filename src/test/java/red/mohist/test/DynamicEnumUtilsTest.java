package red.mohist.test;

import red.mohist.util.DynamicEnumUtils;

import java.util.Arrays;

public class DynamicEnumUtilsTest {

    private enum TestEnum{
        a,b,c;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(TestEnum.values()));
        String str = "d";
        DynamicEnumUtils.addEnum(TestEnum.class, str, new Class[] { }, new Object[] { });;
        System.out.println("_____________________________");
        System.out.println(Arrays.deepToString(TestEnum.values()));
    }
}
