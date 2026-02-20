package onjava;

import org.jspecify.annotations.NonNull;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月18日 周三
 * @time 0:04
 * P.116 §3.7 使用享元自定义 Collection 和 Map
 * "Flyweight" Maps and Lists of sample data
 * {java onjava.Countries}
 * <p>
 * 现在我们来创建一个更复杂的享元。在这个示例中，数据集是一个由国家及其首都组成的 Map。capitals() 方法可以生成这样的 Map。
 * names() 方法则会生成包含国家名的 List。这两个方法都可以通过一个指定了所需大小的 int 参数来获得由部分元素组成的列表：
 */
public class Countries {
    public static final String[][] DATA = {
            // Africa
            {"ALGERIA", "Algiers"},
            {"ANGOLA", "Luanda"},
            {"BENIN", "Porto-Novo"},
            {"BOTSWANA", "Gaberone"},
            {"BURKINA FASO", "Ouagadougou"},
            {"BURUNDI", "Bujumbura"},
            {"CAMEROON", "Yaounde"},
            {"CAPE VERDE", "Praia"},
            {"CENTRAL AFRICAN REPUBLIC", "Bangui"},
            {"CHAD", "N'djamena"},
            {"COMOROS", "Moroni"},
            {"CONGO", "Brazzaville"},
            {"DJIBOUTI", "Dijibouti"},
            {"EGYPT", "Cairo"},
            {"EQUATORIAL GUINEA", "Malabo"},
            {"ERITREA", "Asmara"},
            {"ETHIOPIA", "Addis Ababa"},
            {"GABON", "Libreville"},
            {"THE GAMBIA", "Banjul"},
            {"GHANA", "Accra"},
            {"GUINEA", "Conakry"},
            {"BISSAU", "Bissau"},
            {"COTE D'IVOIR (IVORY COAST)", "Yamoussoukro"},
            {"KENYA", "Nairobi"},
            {"LESOTHO", "Maseru"},
            {"LIBERIA", "Monrovia"},
            {"LIBYA", "Tripoli"},
            {"MADAGASCAR", "Antananarivo"},
            {"MALAWI", "Lilongwe"},
            {"MALI", "Bamako"},
            {"MAURITANIA", "Nouakchott"},
            {"MAURITIUS", "Port Louis"},
            {"MOROCCO", "Rabat"},
            {"MOZAMBIQUE", "Maputo"},
            {"NAMIBIA", "Windhoek"},
            {"NIGER", "Niamey"},
            {"NIGERIA", "Abuja"},
            {"RWANDA", "Kigali"},
            {"SAO TOME E PRINCIPE", "Sao Tome"},
            {"SENEGAL", "Dakar"},
            {"SEYCHELLES", "Victoria"},
            {"SIERRA LEONE", "Freetown"},
            {"SOMALIA", "Mogadishu"},
            {"SOUTH AFRICA", "Pretoria/Cape Town"},
            {"SUDAN", "Khartoum"},
            {"SWAZILAND", "Mbabane"},
            {"TANZANIA", "Dodoma"},
            {"TOGO", "Lome"},
            {"TUNISIA", "Tunis"},
            {"UGANDA", "Kampala"},
            {"DEMOCRATIC REPUBLIC OF THE CONGO (ZAIRE)", "Kinshasa"},
            {"ZAMBIA", "Lusaka"},
            {"ZIMBABWE", "Harare"},
            // Asia
            {"AFGHANISTAN", "Kabul"},
            {"BAHRAIN", "Manama"},
            {"BANGLADESH", "Dhaka"},
            {"BHUTAN", "Thimphu"},
            {"BRUNEI", "Bandar Seri Begawan"},
            {"CAMBODIA", "Phnom Penh"},
            {"CHINA", "Beijing"},
            {"CYPRUS", "Nicosia"},
            {"INDIA", "New Delhi"},
            {"INDONESIA", "Jakarta"},
            {"IRAN", "Tehran"},
            {"IRAQ", "Baghdad"},
            {"ISRAEL", "Jerusalem"},
            {"JAPAN", "Tokyo"},
            {"JORDAN", "Amman"},
            {"KUWAIT", "Kuwait City"},
            {"LAOS", "Vientiane"},
            {"LEBANON", "Beirut"},
            {"MALAYSIA", "Kuala Lumpur"},
            {"THE MALDIVES", "Male"},
            {"MONGOLIA", "Ulan Bator"},
            {"MYANMAR (BURMA)", "Rangoon"},
            {"NEPAL", "Katmandu"},
            {"NORTH KOREA", "P'yongyang"},
            {"OMAN", "Muscat"},
            {"PAKISTAN", "Islamabad"},
            {"PHILIPPINES", "Manila"},
            {"QATAR", "Doha"},
            {"SAUDI ARABIA", "Riyadh"},
            {"SINGAPORE", "Singapore"},
            {"SOUTH KOREA", "Seoul"},
            {"SRI LANKA", "Colombo"},
            {"SYRIA", "Damascus"},
            {"CHINESE TAIWAN", "Taipei"},
            {"THAILAND", "Bangkok"},
            {"TURKEY", "Ankara"},
            {"UNITED ARAB EMIRATES", "Abu Dhabi"},
            {"VIETNAM", "Hanoi"},
            {"YEMEN", "Sana'a"},
            // Australia and Oceania
            {"AUSTRALIA", "Canberra"},
            {"FIJI", "Suva"},
            {"KIRIBATI", "Bairiki"},
            {"MARSHALL ISLANDS", "Dalap-Uliga-Darrit"},
            {"MICRONESIA", "Palikir"},
            {"NAURU", "Yaren"},
            {"NEW ZEALAND", "Wellington"},
            {"PALAU", "Koror"},
            {"PAPUA NEW GUINEA", "Port Moresby"},
            {"SOLOMON ISLANDS", "Honaira"},
            {"TONGA", "Nuku'alofa"},
            {"TUVALU", "Fongafale"},
            {"VANUATU", "Port Vila"},
            {"WESTERN SAMOA", "Apia"},
            // Eastern Europe and former USSR
            {"ARMENIA", "Yerevan"},
            {"AZERBAIJAN", "Baku"},
            {"BELARUS (BYELORUSSIA)", "Minsk"},
            {"BULGARIA", "Sofia"},
            {"GEORGIA", "Tbilisi"},
            {"KAZAKSTAN", "Almaty"},
            {"KYRGYZSTAN", "Alma-Ata"},
            {"MOLDOVA", "Chisinau"},
            {"RUSSIA", "Moscow"},
            {"TAJIKISTAN", "Dushanbe"},
            {"TURKMENISTAN", "Ashkhabad"},
            {"UKRAINE", "Kyiv"},
            {"UZBEKISTAN", "Tashkent"},
            // Europe
            {"ALBANIA", "Tirana"},
            {"ANDORRA", "Andorra la Vella"},
            {"AUSTRIA", "Vienna"},
            {"BELGIUM", "Brussels"},
            {"BOSNIA-HERZEGOVINA", "Sarajevo"},
            {"CROATIA", "Zagreb"},
            {"CZECH REPUBLIC", "Prague"},
            {"DENMARK", "Copenhagen"},
            {"ESTONIA", "Tallinn"},
            {"FINLAND", "Helsinki"},
            {"FRANCE", "Paris"},
            {"GERMANY", "Berlin"},
            {"GREECE", "Athens"},
            {"HUNGARY", "Budapest"},
            {"ICELAND", "Reykjavik"},
            {"IRELAND", "Dublin"},
            {"ITALY", "Rome"},
            {"LATVIA", "Riga"},
            {"LIECHTENSTEIN", "Vaduz"},
            {"LITHUANIA", "Vilnius"},
            {"LUXEMBOURG", "Luxembourg"},
            {"MACEDONIA", "Skopje"},
            {"MALTA", "Valletta"},
            {"MONACO", "Monaco"},
            {"MONTENEGRO", "Podgorica"},
            {"THE NETHERLANDS", "Amsterdam"},
            {"NORWAY", "Oslo"},
            {"POLAND", "Warsaw"},
            {"PORTUGAL", "Lisbon"},
            {"ROMANIA", "Bucharest"},
            {"SAN MARINO", "San Marino"},
            {"SERBIA", "Belgrade"},
            {"SLOVAKIA", "Bratislava"},
            {"SLOVENIA", "Ljuijana"},
            {"SPAIN", "Madrid"},
            {"SWEDEN", "Stockholm"},
            {"SWITZERLAND", "Berne"},
            {"UNITED KINGDOM", "London"},
            {"VATICAN CITY", "Vatican City"},
            // North and Central America
            {"ANTIGUA AND BARBUDA", "Saint John's"},
            {"BAHAMAS", "Nassau"},
            {"BARBADOS", "Bridgetown"},
            {"BELIZE", "Belmopan"},
            {"CANADA", "Ottawa"},
            {"COSTA RICA", "San Jose"},
            {"CUBA", "Havana"},
            {"DOMINICA", "Roseau"},
            {"DOMINICAN REPUBLIC", "Santo Domingo"},
            {"EL SALVADOR", "San Salvador"},
            {"GRENADA", "Saint George's"},
            {"GUATEMALA", "Guatemala City"},
            {"HAITI", "Port-au-Prince"},
            {"HONDURAS", "Tegucigalpa"},
            {"JAMAICA", "Kingston"},
            {"MEXICO", "Mexico City"},
            {"NICARAGUA", "Managua"},
            {"PANAMA", "Panama City"},
            {"ST. KITTS AND NEVIS", "Basseterre"},
            {"ST. LUCIA", "Castries"},
            {"ST. VINCENT AND THE GRENADINES", "Kingstown"},
            {"UNITED STATES OF AMERICA", "Washington, D.C."},
            // South America
            {"ARGENTINA", "Buenos Aires"},
            {"BOLIVIA", "Sucre (legal)/La Paz(administrative)"},
            {"BRAZIL", "Brasilia"},
            {"CHILE", "Santiago"},
            {"COLOMBIA", "Bogota"},
            {"ECUADOR", "Quito"},
            {"GUYANA", "Georgetown"},
            {"PARAGUAY", "Asuncion"},
            {"PERU", "Lima"},
            {"SURINAME", "Paramaribo"},
            {"TRINIDAD AND TOBAGO", "Port of Spain"},
            {"URUGUAY", "Montevideo"},
            {"VENEZUELA", "Caracas"},
    };

    // Use AbstractMap by implementing entrySet()
    private static class FlyweightMap extends AbstractMap<String, String> {
        private static class Entry implements Map.Entry<String, String> {
            int index;

            Entry(int index) {
                this.index = index;
            }

            @Override
            public String getKey() {
                return DATA[index][0];
            }

            @Override
            public String getValue() {
                return DATA[index][1];
            }

            @Override
            public String setValue(String value) {
                throw new UnsupportedOperationException();
            }

            //- Warning: Condition 'obj instanceof FlyweightMap && Objects.equals(DATA[index][0], obj)' is always 'false'
            //- Warning: Condition 'Objects.equals(DATA[index][0], obj)' is always 'false' when reached
            //- Warning: 'equals' between objects of inconvertible types 'String' and 'FlyweightMap'
            @Override
            public boolean equals(Object obj) {
                // 在 Java 集合框架的顶级契约中，java.util.Map.Entry 接口明确规定了它的 equals 规范：
                // 只要两个对象都是 Map.Entry 的实现类，并且它们的 Key 相等、Value 也相等，那么这两个对象就必须相等（返回 true）。
                // 这里有一个极其重要的面向对象原则：面向接口比较，而不是面向实现类比较。在实现集合相关的底层接口时，
                // 我们通常使用最高级的公共接口（Map.Entry）来做 instanceof 判断，这是为了保证多态生态下的互相兼容。
                //- return obj instanceof FlyweightMap && Objects.equals(DATA[index][0], obj);
                return obj instanceof Map.Entry<?, ?> entry &&
                        Objects.equals(this.getKey(), entry.getKey()) &&
                        Objects.equals(this.getValue(), entry.getValue());
            }

            @Override
            public int hashCode() {
                //- return Objects.hashCode(DATA[index][0]);
                // 遵循 Map.Entry 的官方契约：Key 的哈希值 异或 (^) Value 的哈希值
                // The hash code of a map entry e is defined to be:
                // (e.getKey()==null ? 0 : e.getKey().hashCode()) ^ (e.getValue()==null ? 0 : e.getValue().hashCode())
                return Objects.hashCode(this.getKey()) ^ Objects.hashCode(this.getValue());
            }
        }

        // Implement size() & iterator() for AbstractSet:
        static class EntrySet extends AbstractSet<Map.Entry<String, String>> {
            private int size;

            EntrySet(int size) {
                if (size < 0) {
                    this.size = 0;
                }
                // Can't be any bigger than the array:
                else if (size > DATA.length) {
                    this.size = DATA.length;
                } else {
                    this.size = size;
                }
            }

            @Override
            public int size() {
                return size;
            }

            private class Iter implements Iterator<Map.Entry<String, String>> {
                // Only one Entry object per Iterator:
                private Entry entry = new Entry(-1);

                @Override
                public boolean hasNext() {
                    return entry.index < size - 1;
                }

                @Override
                public Map.Entry<String, String> next() {
                    entry.index++;
                    return entry;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            }

            @Override
            public @NonNull Iterator<Map.Entry<String, String>> iterator() {
                return new Iter();
            }
        }

        private static Set<Map.Entry<String, String>> entries = new EntrySet(DATA.length);

        @Override
        public @NonNull Set<Map.Entry<String, String>> entrySet() {
            return entries;
        }
    }

    // Create a partial map of 'size' countries:
    static Map<String, String> select(final int size) {
        return new FlyweightMap() {
            @Override
            public @NonNull Set<Map.Entry<String, String>> entrySet() {
                return new EntrySet(size);
            }
        };
    }

    static Map<String, String> map = new FlyweightMap();

    public static Map<String, String> capitals() {
        return map; // The entire map
    }

    public static Map<String, String> capitals(int size) {
        return select(size); // A partial map
    }

    static List<String> names = new ArrayList<>(map.keySet());

    // All the names:
    public static List<String> names() {
        return names;
    }

    // A partial list:
    public static List<String> names(int size) {
        return new ArrayList<>(select(size).keySet());
    }

    public static void main(String[] args) {
        System.out.println(capitals(10));
        System.out.println(names(10));
        System.out.println(new HashMap<>(capitals(3)));
        System.out.println(new LinkedHashMap<>(capitals(3)));
        System.out.println(new TreeMap<>(capitals(3)));
        System.out.println(new Hashtable<>(capitals(3)));
        System.out.println(new HashSet<>(names(6)));
        System.out.println(new LinkedHashSet<>(names(6)));
        System.out.println(new TreeSet<>(names(6)));
        System.out.println(new ArrayList<>(names(6)));
        System.out.println(new LinkedList<>(names(6)));
        System.out.println(capitals().get("BRAZIL"));
    }
}
/* Output:
{ALGERIA=Algiers, ANGOLA=Luanda, BENIN=Porto-Novo, BOTSWANA=Gaberone, BURKINA FASO=Ouagadougou, BURUNDI=Bujumbura, CAMEROON=Yaounde, CAPE VERDE=Praia, CENTRAL AFRICAN REPUBLIC=Bangui, CHAD=N'djamena}
[ALGERIA, ANGOLA, BENIN, BOTSWANA, BURKINA FASO, BURUNDI, CAMEROON, CAPE VERDE, CENTRAL AFRICAN REPUBLIC, CHAD]
{ANGOLA=Luanda, BENIN=Porto-Novo, ALGERIA=Algiers}
{ALGERIA=Algiers, ANGOLA=Luanda, BENIN=Porto-Novo}
{ALGERIA=Algiers, ANGOLA=Luanda, BENIN=Porto-Novo}
{ALGERIA=Algiers, ANGOLA=Luanda, BENIN=Porto-Novo}
[BENIN, BOTSWANA, ANGOLA, BURKINA FASO, ALGERIA, BURUNDI]
[ALGERIA, ANGOLA, BENIN, BOTSWANA, BURKINA FASO, BURUNDI]
[ALGERIA, ANGOLA, BENIN, BOTSWANA, BURKINA FASO, BURUNDI]
[ALGERIA, ANGOLA, BENIN, BOTSWANA, BURKINA FASO, BURUNDI]
[ALGERIA, ANGOLA, BENIN, BOTSWANA, BURKINA FASO, BURUNDI]
Brasilia
 */
