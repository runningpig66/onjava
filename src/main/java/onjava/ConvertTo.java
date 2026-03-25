package onjava;

/**
 * @author runningpig66
 * @date 2026-03-23
 * @time 20:39
 * P.784 §21.10 泛型和基本类型数组
 * <p>
 * 下面是一个对所有类型都有效的、可双向操作的转换器示例：每个版本的 primitive() 都创建了一个正确长度的基本类型数组，
 * 然后从包装类数组 in 中复制了所有的元素。如果包装类数组中的任何一个元素是 null，就会抛出一个异常
 * （这是合理的——你会用什么有意义的值来替换？）。请留意整个分配过程中，自动装箱机制是怎么发生的。
 */
public interface ConvertTo {
    static boolean[] primitive(Boolean[] in) {
        boolean[] result = new boolean[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i]; // Autounboxing
        }
        return result;
    }

    static char[] primitive(Character[] in) {
        char[] result = new char[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static byte[] primitive(Byte[] in) {
        byte[] result = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static short[] primitive(Short[] in) {
        short[] result = new short[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static int[] primitive(Integer[] in) {
        int[] result = new int[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static long[] primitive(Long[] in) {
        long[] result = new long[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static float[] primitive(Float[] in) {
        float[] result = new float[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static double[] primitive(Double[] in) {
        double[] result = new double[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    // Convert from primitive array to wrapped array:
    static Boolean[] boxed(boolean[] in) {
        Boolean[] result = new Boolean[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i]; // Autoboxing
        }
        return result;
    }

    static Character[] boxed(char[] in) {
        Character[] result = new Character[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static Byte[] boxed(byte[] in) {
        Byte[] result = new Byte[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static Short[] boxed(short[] in) {
        Short[] result = new Short[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static Integer[] boxed(int[] in) {
        Integer[] result = new Integer[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static Long[] boxed(long[] in) {
        Long[] result = new Long[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static Float[] boxed(float[] in) {
        Float[] result = new Float[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }

    static Double[] boxed(double[] in) {
        Double[] result = new Double[in.length];
        for (int i = 0; i < in.length; i++) {
            result[i] = in[i];
        }
        return result;
    }
}
