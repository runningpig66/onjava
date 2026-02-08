package ch22_enums;

import onjava.Enums;
import org.jspecify.annotations.NonNull;

import java.util.Iterator;

/**
 * @author runningpig66
 * @date 2月4日 周三
 * @time 18:08
 * P.023 §1.10 常量特定方法 §1.10.1 用枚举实现职责链模式 [IMP]
 * Modeling a post office
 */
class Mail {
    // The NO's reduce probability of random selection:
    enum GeneralDelivery {YES, NO1, NO2, NO3, NO4, NO5}

    enum Scannability {UNSCANNABLE, YES1, YES2, YES3, YES4}

    enum Readability {ILLEGIBLE, YES1, YES2, YES3, YES4}

    enum Address {INCORRECT, OK1, OK2, OK3, OK4, OK5, OK6}

    enum ReturnAddress {MISSING, OK1, OK2, OK3, OK4, OK5}

    GeneralDelivery generalDelivery;
    Scannability scannability;
    Readability readability;
    Address address;
    ReturnAddress returnAddress;
    static long counter = 0;
    long id = counter++;

    @Override
    public String toString() {
        return "Mail " + id;
    }

    public String details() {
        return this +
                ",\nGeneral Delivery: " + generalDelivery +
                ",\nAddress Scannability: " + scannability +
                ",\nAddress Readability: " + readability +
                ",\nAddress Address: " + address +
                ",\nReturn address: " + returnAddress;
    }

    // Generate test Mail:
    public static Mail randomMail() {
        Mail m = new Mail();
        m.generalDelivery = Enums.random(GeneralDelivery.class);
        m.scannability = Enums.random(Scannability.class);
        m.readability = Enums.random(Readability.class);
        m.address = Enums.random(Address.class);
        m.returnAddress = Enums.random(ReturnAddress.class);
        return m;
    }

    public static Iterable<Mail> generator(final int count) {
        return new Iterable<>() {
            int n = count;

            @Override
            public @NonNull Iterator<Mail> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return n-- > 0;
                    }

                    @Override
                    public Mail next() {
                        return randomMail();
                    }

                    @Override
                    public void remove() { // Not implemented
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}

public class PostOffice {
    enum MailHandler {
        // 第一关：检查是否为“存局候领”（自取），若是则直接拦截成功，否则放行给下一关。
        GENERAL_DELIVERY {
            @Override
            boolean handle(Mail m) {
                switch (m.generalDelivery) {
                    case YES:
                        System.out.println("Using general delivery for " + m);
                        return true;
                    default:
                        return false;
                }
            }
        },
        // 第二关：尝试“机器自动投递”，硬性要求必须【可扫描】且【地址正确】，否则搞不定，放行。
        MACHINE_SCAN {
            @Override
            boolean handle(Mail m) {
                switch (m.scannability) {
                    case UNSCANNABLE:
                        return false;
                    default:
                        switch (m.address) {
                            case INCORRECT:
                                return false;
                            default:
                                System.out.println("Delivering " + m + " automatically");
                                return true;
                        }
                }
            }
        },
        // 第三关：尝试“人工肉眼投递”（处理烂信件），硬性要求必须【字迹可辨】且【地址正确】，否则看不懂，放行。
        VISUAL_INSPECTION {
            @Override
            boolean handle(Mail m) {
                switch (m.readability) {
                    case ILLEGIBLE:
                        return false;
                    default:
                        switch (m.address) {
                            case INCORRECT:
                                return false;
                            default:
                                System.out.println("Delivering " + m + " normally");
                                return true;
                        }
                }
            }
        },
        // 第四关：最后兜底尝试“退件”，要求必须【有寄件人地址】，若连这个都没有，就彻底没救了（死信）。
        RETURN_TO_SENDER {
            @Override
            boolean handle(Mail m) {
                switch (m.returnAddress) {
                    case MISSING:
                        return false;
                    default:
                        System.out.println("Returning " + m + " to sender");
                        return true;
                }
            }
        };

        abstract boolean handle(Mail m);
    }

    static void handle(Mail m) {
        for (MailHandler handler : MailHandler.values()) {
            if (handler.handle(m)) {
                return;
            }
        }
        System.out.println(m + " is a dead letter");
    }

    public static void main(String[] args) {
        for (Mail mail : Mail.generator(10)) {
            System.out.println(mail.details());
            handle(mail);
            System.out.println("*****");
        }
    }
}
/* Output:
Mail 0,
General Delivery: NO2,
Address Scannability: UNSCANNABLE,
Address Readability: YES3,
Address Address: OK1,
Return address: OK1
Delivering Mail 0 normally
*****
Mail 1,
General Delivery: NO5,
Address Scannability: YES3,
Address Readability: ILLEGIBLE,
Address Address: OK5,
Return address: OK1
Delivering Mail 1 automatically
*****
Mail 2,
General Delivery: YES,
Address Scannability: YES3,
Address Readability: YES1,
Address Address: OK1,
Return address: OK5
Using general delivery for Mail 2
*****
Mail 3,
General Delivery: NO4,
Address Scannability: YES3,
Address Readability: YES1,
Address Address: INCORRECT,
Return address: OK4
Returning Mail 3 to sender
*****
Mail 4,
General Delivery: NO4,
Address Scannability: UNSCANNABLE,
Address Readability: YES1,
Address Address: INCORRECT,
Return address: OK2
Returning Mail 4 to sender
*****
Mail 5,
General Delivery: NO3,
Address Scannability: YES1,
Address Readability: ILLEGIBLE,
Address Address: OK4,
Return address: OK2
Delivering Mail 5 automatically
*****
Mail 6,
General Delivery: YES,
Address Scannability: YES4,
Address Readability: ILLEGIBLE,
Address Address: OK4,
Return address: OK4
Using general delivery for Mail 6
*****
Mail 7,
General Delivery: YES,
Address Scannability: YES3,
Address Readability: YES4,
Address Address: OK2,
Return address: MISSING
Using general delivery for Mail 7
*****
Mail 8,
General Delivery: NO3,
Address Scannability: YES1,
Address Readability: YES3,
Address Address: INCORRECT,
Return address: MISSING
Mail 8 is a dead letter
*****
Mail 9,
General Delivery: NO1,
Address Scannability: UNSCANNABLE,
Address Readability: YES2,
Address Address: OK1,
Return address: OK4
Delivering Mail 9 normally
*****
 */
