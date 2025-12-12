package ua.kpi.comsys.test2.implementation.internal;

import ua.kpi.comsys.test2.NumberList;
import ua.kpi.comsys.test2.implementation.NumberListImpl;

/**
 * Реалізація додаткової операції
 * <b>алгебраїчне та логічне AND двох чисел</b>.
 * <p>
 * Обидва операнди подаються у вигляді об’єктів {@link NumberList},
 * що зберігають число у своїй внутрішній системі числення.
 *
 *
 * @author Андрій Шевчук
 * @group  ІС-31
 * @recordBookNumber 26
 */
public class BitwiseAndOperation {

    /**
     * Виконує побітове AND двох чисел, представлених у вигляді списків цифр.
     *
     *
     * @param left  перший операнд
     * @param right другий операнд
     * @return новий {@link NumberListImpl}, що містить результат операції у трійковій системі числення
     */
    public static NumberListImpl apply(NumberList left, NumberList right) {

        NumberListImpl leftBin =
            BaseConverter.convert((NumberListImpl) left, 2);
        NumberListImpl rightBin =
            BaseConverter.convert((NumberListImpl) right, 2);

        int max = Math.max(leftBin.size(), rightBin.size());
        padWithZeros(leftBin, max);
        padWithZeros(rightBin, max);

        NumberListImpl andResult = new NumberListImpl(2);
        for (int i = 0; i < max; i++) {
            byte bit = (byte) (leftBin.get(i) & rightBin.get(i));
            andResult.add(bit);
        }

        return BaseConverter.convert(andResult, 3);
    }

    private static void padWithZeros(NumberListImpl list, int size) {
        while (list.size() < size) {
            list.addFirstDigit((byte) 0);
        }
    }
}
