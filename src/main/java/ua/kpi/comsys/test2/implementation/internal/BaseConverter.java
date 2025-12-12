package ua.kpi.comsys.test2.implementation.internal;

import ua.kpi.comsys.test2.implementation.NumberListImpl;


/**
 * Утилітний клас для перетворення чисел між різними системами числення.
 * <p>
 * Забезпечує:
 * <ul>
 *     <li>Перетворення списку цифр у десяткове число;</li>
 *     <li>Перетворення десяткового числа у список цифр заданої основи;</li>
 *     <li>Загальну конвертацію між будь-якими системами числення.</li>
 * </ul>
 *
 * Усі методи є статичними і не потребують створення об’єкта класу.
 * Клас використовується всередині {@link NumberListImpl}.
 *
 *
 * @author Андрій Шевчук
 * @group  ІС-31
 * @recordBookNumber 26
 */
public class BaseConverter {

    /**
     * Перетворює число, представлене у вигляді списку цифр
     * у його десяткове значення.
     *
     *
     * @param list об'єкт {@link NumberListImpl}, що містить цифри числа
     * @return десяткове значення числа
     */
    public static String toDecimal(NumberListImpl list) {
        String result = "0";

        for (int i = 0; i < list.size(); i++) {
            result = multiply(result, list.getBase());
            result = add(result, list.get(i));
        }

        return result;
    }


    /**
     * Перетворює десяткове число у список цифр заданої системи числення.
     *
     *
     * @param value десяткове число для перетворення
     * @param base основа системи числення результату
     * @return новий {@link NumberListImpl}, що містить число у новій основі
     */
    public static NumberListImpl fromDecimal(String value, int base) {
        NumberListImpl result = new NumberListImpl(base);

        if (value == null || value.isEmpty()) {
            return result;
        }

        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid decimal number");
        }

        String current = value;

        while (!current.equals("0")) {
            int carry = 0;
            StringBuilder quotient = new StringBuilder();

            for (char c : current.toCharArray()) {
                int cur = carry * 10 + (c - '0');
                quotient.append(cur / base);
                carry = cur % base;
            }

            result.addFirstDigit((byte) carry);

            current = quotient.toString().replaceFirst("^0+", "");
            if (current.isEmpty()) current = "0";
        }

        return result;
    }

    /**
     * Загальна конвертація числа з однієї системи числення в іншу.
     *
     *
     * @param src вихідний список цифр (число в поточній основі)
     * @param newBase нова основа системи числення
     * @return нове число у вигляді {@link NumberListImpl} в основі newBase
     */
    public static NumberListImpl convert(NumberListImpl src, int newBase) {
        String decimal = toDecimal(src);
        return fromDecimal(decimal, newBase);
    }

    private static String add(String number, int digit) {
        StringBuilder sb = new StringBuilder();
        int carry = digit;

        for (int i = number.length() - 1; i >= 0; i--) {
            int sum = (number.charAt(i) - '0') + carry;
            sb.append(sum % 10);
            carry = sum / 10;
        }

        if (carry > 0) sb.append(carry);
        return sb.reverse().toString();
    }

    private static String multiply(String number, int multiplier) {
        StringBuilder sb = new StringBuilder();
        int carry = 0;

        for (int i = number.length() - 1; i >= 0; i--) {
            int prod = (number.charAt(i) - '0') * multiplier + carry;
            sb.append(prod % 10);
            carry = prod / 10;
        }

        while (carry > 0) {
            sb.append(carry % 10);
            carry /= 10;
        }

        return sb.reverse().toString();
    }
}
