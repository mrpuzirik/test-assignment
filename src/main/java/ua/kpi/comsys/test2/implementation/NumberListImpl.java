/*
 * Copyright (c) 2014, NTUU KPI, Computer systems department and/or its affiliates. All rights reserved.
 * NTUU KPI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 */

package ua.kpi.comsys.test2.implementation;

import java.io.File;
import java.util.*;

import ua.kpi.comsys.test2.NumberList;
import ua.kpi.comsys.test2.implementation.internal.BaseConverter;
import ua.kpi.comsys.test2.implementation.internal.BitwiseAndOperation;
import ua.kpi.comsys.test2.implementation.internal.CircularDoublyList;

/**
 * Реалізація інтерфейсу {@link ua.kpi.comsys.test2.NumberList}, що представляє число
 * у вигляді користувацького списку цифр у відповідній системі числення.
 * <p>
 * Для залікового варіанта студента використовується:
 * <ul>
 *     <li><b>С3 = 2</b> — кільцевий двонаправлений список;</li>
 *     <li><b>С5 = 1</b> — основна система числення: трійкова (base-3);</li>
 *     <li><b>Додаткова система</b> — (C5 + 1) mod 5 = 2 → вісімкова (base-8);</li>
 *     <li><b>С7 = 5</b> — додаткова операція:
 *         алгебраїчне та логічне AND над двома числами.</li>
 * </ul>
 *
 * Клас забезпечує:
 * <ul>
 *     <li>Збереження числа у вигляді кільцевого двонаправленого списку;</li>
 *     <li>Контроль коректності додавання цифр відповідно до поточної основи;</li>
 *     <li>Маніпуляції над списком (додавання, видалення, зсуви, сортування);</li>
 *     <li>Перетворення числа у десяткову та інші системи числення;</li>
 *     <li>Виконання додаткової операції AND;</li>
 * </ul>
 *
 *
 * @author Андрій Шевчук
 * @group  ІС-31
 * @recordBookNumber 26
 */

public class NumberListImpl implements NumberList {

    private final int base;
    private CircularDoublyList list = new CircularDoublyList();


    /**
     * Перевіряє, чи належить цифра допустимому діапазону
     * для поточної системи числення.
     *
     * @param e цифра, яку потрібно перевірити
     * @throws IllegalArgumentException якщо цифра < 0 або ≥ base
     */
    private void checkDigit(Byte e) {
        if (e == null || e < 0 || e >= base)
            throw new IllegalArgumentException(
                "Digit " + e + " invalid for base " + base
            );
    }


    /**
     * Повертає основу системи числення, у якій зберігається дане число.
     *
     * @return система числення (base)
     */
    public int getBase() {
        return base;
    }


    /**
     * Додає цифру на початок списку.
     * Використовується перетворювачем систем числення.
     *
     * @param d цифра для додавання
     * @throws IllegalArgumentException якщо цифра не відповідає основі системи
     */
    public void addFirstDigit(byte d) {
        checkDigit(d);
        list.addFirst(d);
    }


    /**
     * Створює порожній список цифр у трійковій системі числення (base-3).
     */
    public NumberListImpl() {
        this.base = 3;
    }


    /**
     * Створює порожній список цифр у вказаній системі числення.
     *
     * @param base системи числення
     */
    public NumberListImpl(int base) {
        this.base = base;
    }


    /**
     * Створює число, зчитуючи його зі звичайного текстового файлу,
     * де число представлено в десятковій системі числення.
     * <p>
     * Якщо файл не існує, порожній або містить некоректні дані,
     * створюється порожній список.
     *
     * @param file файл із числом у десятковому представленні
     */
    public NumberListImpl(File file) {
        this.base = 3;

        try (Scanner sc = new Scanner(file)) {
            if (!sc.hasNextLine()) {
                return;
            }

            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                return;
            }

            NumberListImpl converted = BaseConverter.fromDecimal(line, base);
            this.list = converted.list;

        } catch (Exception e) {
            this.list = new CircularDoublyList();
        }
    }



    /**
     * Створює число з рядка, який містить десяткове представлення числа.
     * <p>
     * Якщо рядок є порожнім, містить нечислові символи або відʼємне число,
     * створюється порожній список.
     *
     * @param value рядок з десятковим представленням числа
     */
    public NumberListImpl(String value) {
        this.base = 3;

        if (value == null || value.isEmpty())
            return;

        if (!value.matches("\\d+"))
            return;

        NumberListImpl converted = BaseConverter.fromDecimal(value, base);
        this.list = converted.list;
    }



    /**
     * Зберігає число у вказаний файл у вигляді десяткового значення.
     *
     * @param file файл для збереження
     * @throws RuntimeException якщо файл не може бути створений або записаний
     */
    public void saveList(File file) {
        try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
            pw.print(toDecimalString());
        } catch (Exception e) {
            throw new RuntimeException("Cannot save number to file", e);
        }
    }


    /**
     * Повертає номер залікової книжки студента.
     *
     * @return номер залікової книжки, у цьому випадку порядковий номер у списку — 26
     */
    public static int getRecordBookNumber() {
        return 26;
    }


    /**
     * Повертає новий список, що є представленням цього ж числа,
     * але у додатковій системі числення (base-8).
     * <p>Оригінальний список не змінюється.</p>
     *
     * @return список у новій системі числення
     */
    public NumberListImpl changeScale() {
        int newBase = 8;
        return BaseConverter.convert(this, newBase);
    }


    /**
     * Виконує додаткову операцію згідно —
     * алгебраїчне та логічне AND над двома числами.
     * <p>Оригінальні списки не модифікуються.</p>
     *
     * @param arg другий операнд
     * @return результат операції як новий NumberListImpl
     */
    public NumberListImpl additionalOperation(NumberList arg) {
        return BitwiseAndOperation.apply(this, arg);
    }


    /**
     * Повертає десяткове представлення числа.
     *
     * @return рядок з десятковим значенням
     */
    public String toDecimalString() {
        return BaseConverter.toDecimal(this);
    }


    /**
     * Повертає рядкове представлення числа, де всі цифри з'єднані без роздільників.
     * Використовується для виведення числа у його внутрішній системі числення.
     *
     * @return рядок із послідовності цифр
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++)
            sb.append(get(i));
        return sb.toString();
    }


    /**
     * Порівнює два списки-числа за значенням.
     *
     * @param o інший список
     * @return true, якщо обидва числа мають однакову довжину та цифри
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NumberList other)) return false;
        if (size() != other.size()) return false;
        for (int i = 0; i < size(); i++)
            if (!Objects.equals(get(i), other.get(i)))
                return false;
        return true;
    }


    /**
     * Повертає кількість цифр у числі.
     *
     * @return розмір списку
     */
    @Override
    public int size() {
        return list.size();
    }


    /**
     * Перевіряє, чи є список порожнім.
     *
     * @return true, якщо не містить жодної цифри
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }


    /**
     * Перевіряє, чи містить список певну цифру.
     *
     * @param o об'єкт, що перевіряється
     * @return true, якщо о — Byte і присутній у списку
     */
    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Byte d)) return false;
        for (int i = 0; i < size(); i++)
            if (get(i).equals(d)) return true;
        return false;
    }


    /**
     * Повертає ітератор для обходу цифр числа зліва направо.
     *
     * @return ітератор по цифрах
     */
    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public Byte next() {
                if (!hasNext()) throw new NoSuchElementException();
                return get(index++);
            }
        };
    }


    /**
     * Перетворює список у масив об'єктів.
     *
     * @return масив елементів списку
     */
    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size()];
        for (int i = 0; i < size(); i++)
            arr[i] = get(i);
        return arr;
    }


    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Додає цифру в кінець списку.
     *
     * @param e цифра
     * @return true (за контрактом List)
     * @throws IllegalArgumentException якщо цифра не відповідає основі системи
     */
    @Override
    public boolean add(Byte e) {
        checkDigit(e);
        list.add(e);
        return true;
    }


    /**
     * Видаляє перше входження заданої цифри.
     *
     * @param o цифра
     * @return true, якщо видалення відбулося
     */
    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Byte d)) return false;
        int idx = indexOf(d);
        if (idx == -1) return false;
        remove(idx);
        return true;
    }


    /**
     * Перевіряє, чи всі елементи колекції містяться у цьому списку.
     *
     * @param c колекція для перевірки
     * @return true, якщо всі присутні
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }


    /**
     * Додає всі елементи колекції у кінець списку.
     *
     * @param c колекція цифр
     * @return true, якщо список змінено
     */
    @Override
    public boolean addAll(Collection<? extends Byte> c) {
        boolean modified = false;
        for (Byte b : c) {
            add(b);
            modified = true;
        }
        return modified;
    }

    /**
     * Вставляє всі елементи колекції, починаючи з певної позиції.
     *
     * @param index позиція вставки
     * @param c колекція цифр
     * @return true, якщо список змінено
     */
    @Override
    public boolean addAll(int index, Collection<? extends Byte> c) {
        int pos = index;
        boolean modified = false;
        for (Byte b : c) {
            add(pos++, b);
            modified = true;
        }
        return modified;
    }


    /**
     * Видаляє всі входження елементів, які містяться у вказаній колекції.
     *
     * @param c колекція елементів для видалення
     * @return true, якщо список змінено
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o))
                modified = true;
        }
        return modified;
    }


    /**
     * Залишає тільки ті елементи, що містяться у вказаній колекції.
     *
     * @param c колекція допустимих елементів
     * @return true, якщо список змінено
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int i = 0;
        while (i < size()) {
            if (!c.contains(get(i))) {
                remove(i);
                modified = true;
            } else {
                i++;
            }
        }
        return modified;
    }


    /**
     * Очищає список, роблячи його порожнім.
     */
    @Override
    public void clear() {
        list = new CircularDoublyList();
    }


    /**
     * Повертає цифру за індексом.
     *
     * @param index індекс
     * @return цифра
     * @throws IndexOutOfBoundsException якщо індекс некоректний
     */
    @Override
    public Byte get(int index) {
        return list.get(index);
    }


    /**
     * Замінює цифру за індексом.
     *
     * @param index позиція
     * @param element нова цифра
     * @return попереднє значення
     * @throws IllegalArgumentException якщо цифра недопустима
     */
    @Override
    public Byte set(int index, Byte element) {
        checkDigit(element);
        byte old = get(index);
        list.set(index, element);
        return old;
    }


    /**
     * Вставляє цифру у вказану позицію.
     *
     * @param index індекс вставки
     * @param element цифра
     */
    @Override
    public void add(int index, Byte element) {
        checkDigit(element);
        list.add(index, element);
    }

    /**
     * Видаляє цифру за індексом.
     *
     * @param index позиція видалення
     * @return видалена цифра
     */
    @Override
    public Byte remove(int index) {
        return list.remove(index);
    }


    /**
     * Повертає індекс першої появи цифри.
     *
     * @param o цифра
     * @return індекс або -1
     */
    @Override
    public int indexOf(Object o) {
        if (!(o instanceof Byte d)) return -1;
        for (int i = 0; i < size(); i++)
            if (get(i).equals(d)) return i;
        return -1;
    }

    /**
     * Повертає індекс останньої появи цифри.
     *
     * @param o цифра
     * @return індекс або -1
     */
    @Override
    public int lastIndexOf(Object o) {
        if (!(o instanceof Byte d)) return -1;
        for (int i = size()-1; i >= 0; i--)
            if (get(i).equals(d)) return i;
        return -1;
    }

    /**
     * Повертає двосторонній ітератор по списку.
     *
     * @return ListIterator для обходу та модифікації списку
     */
    @Override
    public ListIterator<Byte> listIterator() {
        return listIterator(0);
    }

    /**
     * Повертає двосторонній ітератор по списку починаючи з індексу.
     *
     * @param  index індекс
     * @return ListIterator для обходу та модифікації списку
     */
    @Override
    public ListIterator<Byte> listIterator(int index) {
        return new ListIterator<>() {

            int cursor = index;
            int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return cursor < size();
            }

            @Override
            public Byte next() {
                if (!hasNext()) throw new NoSuchElementException();
                Byte val = get(cursor);
                lastReturned = cursor;
                cursor++;
                return val;
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public Byte previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                cursor--;
                lastReturned = cursor;
                return get(cursor);
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                if (lastReturned == -1)
                    throw new IllegalStateException("remove() without next()/previous()");

                NumberListImpl.this.remove(lastReturned);

                if (lastReturned < cursor)
                    cursor--;

                lastReturned = -1;
            }

            @Override
            public void set(Byte e) {
                if (lastReturned == -1)
                    throw new IllegalStateException("set() without next()/previous()");

                checkDigit(e);
                NumberListImpl.this.set(lastReturned, e);
            }

            @Override
            public void add(Byte e) {
                checkDigit(e);

                NumberListImpl.this.add(cursor, e);
                cursor++;
                lastReturned = -1;
            }
        };
    }


    /**
     * Створює новий NumberListImpl, що містить підпослідовність цифр.
     *
     * @param fromIndex початок включно
     * @param toIndex кінець не включно
     * @return новий список
     */
    @Override
    public List<Byte> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size() || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();

        NumberListImpl sub = new NumberListImpl();
        for (int i = fromIndex; i < toIndex; i++)
            sub.add(get(i));

        return sub;
    }

    /**
     * Міняє місцями дві цифри у списку.
     *
     * @param index1 індекс першого числа
     * @param index2 індекс другого числа
     * @return true (за контрактом)
     */
    @Override
    public boolean swap(int index1, int index2) {
        byte a = get(index1);
        byte b = get(index2);
        set(index1, b);
        set(index2, a);
        return true;
    }


    /**
     * Сортує цифри у порядку що зростає.
     */
    @Override
    public void sortAscending() {
        for (int i = 0; i < size(); i++)
            for (int j = i+1; j < size(); j++)
                if (get(i) > get(j)) swap(i, j);
    }

    /**
     * Сортує цифри у порядку що спадає.
     */
    @Override
    public void sortDescending() {
        for (int i = 0; i < size(); i++)
            for (int j = i+1; j < size(); j++)
                if (get(i) < get(j)) swap(i, j);
    }


    /**
     * Виконує циклічний зсув вліво: перший елемент переміщується в кінець.
     */
    @Override
    public void shiftLeft() {
        if (size() <= 1) return;
        byte first = remove(0);
        add(first);
    }

    /**
     * Виконує циклічний зсув вправо: останній елемент переміщується на початок.
     */
    @Override
    public void shiftRight() {
        if (size() <= 1) return;
        byte last = remove(size()-1);
        list.add(0, last);
    }
}
