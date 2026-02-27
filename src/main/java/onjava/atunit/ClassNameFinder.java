package onjava.atunit;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author runningpig66
 * @date 2月27日 周五
 * @time 5:13
 * P.198 §4.4 基于注解的单元测试 §4.4.2 实现 @Unit
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\onjava}
 * <p>
 * AtUnit.java 在寻找类文件时有个必须解决的问题：从类文件名无法确切地得知限定的类名（包括包名）。
 * 要获取这个信息，就必须分析类文件。这并非易事，但也并非做不到。当找到一个 .class 文件时，程序会打开该文件，读取它的二进制数据，
 * 并传给 ClassNameFinder.thisClass()。此处，我们将进入“字节码工程”的领域，因为我们实际上已经在分析类文件的内容了。
 */
public class ClassNameFinder {
    public static String thisClass(byte[] classBytes) {
        // 字典1：记录类信息引用：Key 为常量池索引，Value 为指向具体字符串项的常量池索引
        Map<Integer, Integer> offsetTable = new HashMap<>();
        // 字典2：记录具体的字符串数据：Key 为常量池索引，Value 为解析出的 UTF-8 字符串
        // Map<Integer, String> utf8Table = new HashMap<>();
        Map<Integer, String> utf8Table = new HashMap<>();
        try {
            // 将字节数组包装为 DataInputStream，以便按 Java 基础数据类型规范读取结构化流
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(classBytes));
            // 1. 读取魔术数字 (Magic Number)：占 4 个字节。
            int magic = data.readInt(); // 0xcafebabe
            // 2. 读取版本号：各占 2 个字节。
            int minorVersion = data.readShort(); // 次版本号
            int majorVersion = data.readShort(); // 主版本号 (例如 Java 21 是 65)
            // 3. 读取常量池容量：占 2 个字节。
            int constantPoolCount = data.readShort();
            int[] constantPool = new int[constantPoolCount];
            // 4. 遍历常量池。注：JVM 规范约定常量池的有效索引从 1 开始
            for (int i = 1; i < constantPoolCount; i++) {
                // 读取当前常量项的标志位 (Tag)，用于决定后续数据的结构与长度：占 1 个字节。
                int tag = data.read();
                // int tableSize;
                switch (tag) {
                    case 1: // UTF 解析并存储实际的字符串文本
                        int length = data.readShort(); // 读取字符串长度
                        char[] bytes = new char[length];
                        for (int k = 0; k < bytes.length; k++) {
                            bytes[k] = (char) data.read(); // 读取单个字符
                        }
                        // String className = new String(bytes);
                        String utf8Text = new String(bytes);
                        // 存入字典2：常量池编号 i 对应的文本（i 的 tag 是 1 字符串）
                        utf8Table.put(i, utf8Text);
                        break;
                    case 5: // LONG
                    case 6: // DOUBLE
                        data.readLong(); // discard 8 bytes
                        // 根据 JVM 规范 (JVMS 4.4.5)，Long 和 Double 类型的常量将占用常量池中的两个逻辑索引。
                        // 因此此处需要执行 i++，以正确跳过下一个保留索引。
                        i++; // Special skip necessary
                        break;
                    case 7: // CLASS 存放指向字符串常量的索引
                        int offset = data.readShort();
                        // 存入字典1：编号 i 指向 offset
                        offsetTable.put(i, offset);
                        break;
                    case 8: // STRING 存放指向字符串常量的索引
                        data.readShort(); // discard 2 bytes
                        break;
                    // 以下 Case 为本逻辑无需关注的常量类型。直接读取并丢弃相应的字节数，以确保文件指针正确推进到下一个 Tag。
                    case 3:  // INTEGER
                    case 4:  // FLOAT
                    case 9:  // FIELD_REF
                    case 10: // METHOD_REF
                    case 11: // INTERFACE_METHOD_REF
                    case 12: // NAME_AND_TYPE
                    case 18: // Invoke Dynamic
                        data.readInt(); // discard 4 bytes
                        break;
                    case 15: // Method Handle
                        data.readByte();
                        data.readShort();
                        break;
                    case 16: // Method Type
                        data.readShort();
                        break;
                    default:
                        // 遇到未知的 Tag 时抛出异常，通常意味着类文件已损坏或版本不兼容
                        throw new RuntimeException("Bad tag " + tag);
                }
            }
            // 5. 读取当前类的访问标志
            short accessFlags = data.readShort();
            // 掩码 0x0001 对应 ACC_PUBLIC 标志
            String access = (accessFlags & 0x0001) == 0 ? "nonpublic:" : "public:";
            // 6. 读取当前类索引与父类索引
            int thisClass = data.readShort();
            int superClass = data.readShort();
            // 7. 通过索引链解析类的全限定名：thisClass 索引 -> offsetTable 获取字符串常量索引 -> utf8Table 获取内部形式的类名
            // return access + classNameTable.get(offsetTable.get(thisClass)).replace('/', '.');
            return access + utf8Table.get(offsetTable.get(thisClass)).replace('/', '.');
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // Demonstration:
    public static void main(String[] args) throws Exception {
        PathMatcher matcher = FileSystems.getDefault()
                .getPathMatcher("glob:**/*.class");
        // Walk the entire tree:
        Files.walk(Paths.get("build/classes/java/main/onjava"))
                .filter(matcher::matches)
                .map(p -> {
                    try {
                        return thisClass(Files.readAllBytes(p));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(s -> s.startsWith("public:"))
                // .filter(s -> s.indexOf('$') >= 0)
                .map(s -> s.split(":")[1])
                .filter(s -> !s.startsWith("enums."))
                .filter(s -> s.contains("."))
                .forEach(System.out::println);
    }
}
/* Output:
onjava.ArrayShow
onjava.atunit.AtUnit$TestMethods
onjava.atunit.AtUnit
onjava.atunit.ClassNameFinder
onjava.atunit.Test
onjava.atunit.TestObjectCleanup
onjava.atunit.TestObjectCreate
onjava.atunit.TestProperty
onjava.BasicSupplier
onjava.CollectionMethodDifferences
onjava.ConvertTo
onjava.Count$Boolean
onjava.Count$Byte
onjava.Count$Character
onjava.Count$Double
onjava.Count$Float
onjava.Count$Integer
onjava.Count$Long
onjava.Count$Pboolean
onjava.Count$Pbyte
onjava.Count$Pchar
onjava.Count$Pdouble
onjava.Count$Pfloat
onjava.Count$Pint
onjava.Count$Plong
onjava.Count$Pshort
onjava.Count$Short
onjava.Count
onjava.CountingIntegerList
onjava.CountMap
onjava.Countries
onjava.Enums
onjava.FillMap
onjava.HTMLColors
onjava.MouseClick
onjava.Nap
onjava.Null
onjava.Operation
onjava.OSExecute
onjava.OSExecuteException
onjava.Pair
onjava.ProcessFiles$Strategy
onjava.ProcessFiles
onjava.Rand$Boolean
onjava.Rand$Byte
onjava.Rand$Character
onjava.Rand$Double
onjava.Rand$Float
onjava.Rand$Integer
onjava.Rand$Long
onjava.Rand$Pboolean
onjava.Rand$Pbyte
onjava.Rand$Pchar
onjava.Rand$Pdouble
onjava.Rand$Pfloat
onjava.Rand$Pint
onjava.Rand$Plong
onjava.Rand$Pshort
onjava.Rand$Short
onjava.Rand$String
onjava.Rand
onjava.Range
onjava.Repeat
onjava.RmDir
onjava.Sets
onjava.Stack
onjava.Suppliers
onjava.TestRange
onjava.TimedAbort
onjava.Timer
onjava.Tuple
onjava.Tuple2
onjava.Tuple3
onjava.Tuple4
onjava.Tuple5
onjava.TypeCounter
 */
