package ua.kpi.comsys.test2.implementation.internal;

/**
 * Реалізація кільцевого двонаправленого списку для зберігання цифр числа.
 * <p>
 * Структура використовується як базовий контейнер у {@code NumberListImpl}.
 * Кожен елемент списку представлений об'єктом {@link Node}, який має
 * посилання на попередній та наступний вузол, утворюючи кільцеву структуру.
 * <p>
 * Список підтримує базові операції:
 * <ul>
 *     <li>Отримання та зміну значення за індексом;</li>
 *     <li>Додавання у кінець або в довільну позицію;</li>
 *     <li>Видалення елемента;</li>
 *     <li>Пошук вузла за індексом з оптимізованим обходом;</li>
 *     <li>Додавання елемента на початок.</li>
 * </ul>
 *
 *
 * @author Андрій Шевчук
 * @group  ІС-31
 * @recordBookNumber 26
 */

public class CircularDoublyList {

    private Node head = null;
    private int size = 0;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public byte get(int index) {
        return nodeAt(index).value;
    }

    public void set(int index, byte value) {
        nodeAt(index).value = value;
    }

    public void add(byte value) {
        Node n = new Node(value);

        if (head == null) {
            head = n;
            head.next = head;
            head.prev = head;
        } else {
            Node tail = head.prev;

            tail.next = n;
            n.prev = tail;

            n.next = head;
            head.prev = n;
        }
        size++;
    }

    public void add(int index, byte value) {
        if (index == size) {
            add(value);
            return;
        }

        Node target = nodeAt(index);
        Node newNode = new Node(value);

        Node prev = target.prev;
        prev.next = newNode;
        newNode.prev = prev;

        newNode.next = target;
        target.prev = newNode;

        if (index == 0) {
            head = newNode;
        }

        size++;
    }

    public byte remove(int index) {
        Node target = nodeAt(index);
        byte val = target.value;

        if (size == 1) {
            head = null;
        } else {
            Node prev = target.prev;
            Node next = target.next;

            prev.next = next;
            next.prev = prev;

            if (target == head) {
                head = next;
            }
        }

        size--;
        return val;
    }

    public Node nodeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        if (index <= size / 2) {
            Node curr = head;
            for (int i = 0; i < index; i++) curr = curr.next;
            return curr;
        }

        Node curr = head.prev;
        for (int i = size - 1; i > index; i--) curr = curr.prev;

        return curr;
    }

    public Node getHead() {
        return head;
    }

    public void addFirst(byte value) {
        Node newNode = new Node(value);

        if (head == null) {
            head = newNode;
            head.next = head;
            head.prev = head;
        } else {
            Node tail = head.prev;

            newNode.next = head;
            newNode.prev = tail;

            tail.next = newNode;
            head.prev = newNode;

            head = newNode;
        }

        size++;
    }
}
