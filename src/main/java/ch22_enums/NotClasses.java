package ch22_enums;

/**
 * @author runningpig66
 * @date 2月4日 周三
 * @time 15:32
 * P.019 §1.10 常量特定方法
 * {ExcludeFromGradle}
 * E:\IdeaProjects\onjava> ./gradlew clean compilejava
 * javap -c -cp ./build/classes/java/main ch22_enums.LikeClasses
 */
enum LikeClasses {
    WINKEN {
        @Override
        void behavior() {
            System.out.println(aaa);
            System.out.println("Behavior1");
        }
    },
    BLINKEN {
        @Override
        void behavior() {
            System.out.println("Behavior2");
        }
    },
    NOD {
        @Override
        void behavior() {
            System.out.println("Behavior3");
        }
    };

    abstract void behavior();

    public String aaa = "123";
}

public class NotClasses {
    // Error: Unknown class: 'LikeClasses.WINKEN'
    // void f1(LikeClasses.WINKEN instance) {} // Nope
}
/* Output:
Compiled from "NotClasses.java"
abstract class ch22_enums.LikeClasses extends java.lang.Enum<ch22_enums.LikeClasses> {
  public static final ch22_enums.LikeClasses WINKEN;

  public static final ch22_enums.LikeClasses BLINKEN;

  public static final ch22_enums.LikeClasses NOD;

  public static ch22_enums.LikeClasses[] values();
    Code:
       0: getstatic     #13                 // Field $VALUES:[Lch22_enums/LikeClasses;
       3: invokevirtual #17                 // Method "[Lch22_enums/LikeClasses;".clone:()Ljava/lang/Object;
       6: checkcast     #18                 // class "[Lch22_enums/LikeClasses;"
       9: areturn

  public static ch22_enums.LikeClasses valueOf(java.lang.String);
    Code:
       0: ldc           #1                  // class ch22_enums/LikeClasses
       2: aload_0
       3: invokestatic  #22                 // Method java/lang/Enum.valueOf:(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
       6: checkcast     #1                  // class ch22_enums/LikeClasses
       9: areturn

  abstract void behavior();

  static {};
    Code:
       0: new           #32                 // class ch22_enums/LikeClasses$1
       3: dup
       4: ldc           #34                 // String WINKEN
       6: iconst_0
       7: invokespecial #35                 // Method ch22_enums/LikeClasses$1."<init>":(Ljava/lang/String;I)V
      10: putstatic     #3                  // Field WINKEN:Lch22_enums/LikeClasses;
      13: new           #36                 // class ch22_enums/LikeClasses$2
      16: dup
      17: ldc           #38                 // String BLINKEN
      19: iconst_1
      20: invokespecial #39                 // Method ch22_enums/LikeClasses$2."<init>":(Ljava/lang/String;I)V
      23: putstatic     #7                  // Field BLINKEN:Lch22_enums/LikeClasses;
      26: new           #40                 // class ch22_enums/LikeClasses$3
      29: dup
      30: ldc           #42                 // String NOD
      32: iconst_2
      33: invokespecial #43                 // Method ch22_enums/LikeClasses$3."<init>":(Ljava/lang/String;I)V
      36: putstatic     #10                 // Field NOD:Lch22_enums/LikeClasses;
      39: invokestatic  #44                 // Method $values:()[Lch22_enums/LikeClasses;
      42: putstatic     #13                 // Field $VALUES:[Lch22_enums/LikeClasses;
      45: return
}
 */
