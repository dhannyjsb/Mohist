package red.mohist.util.perfomance;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Using thread safety
 * TreeSet(Non-thread safe) ---> ConcurrentSkipListSet(thread safety)
 * @param <E>
 */
public class SafetyTreeSet<E> extends TreeSet<E> {
    private ConcurrentSkipListSet<E> css;

    SafetyTreeSet(ConcurrentSkipListSet<E> m) {
        this.css = m;
    }

    public SafetyTreeSet() {
        this(new ConcurrentSkipListSet());
    }

    public SafetyTreeSet(Comparator<? super E> comparator) {
        this(new ConcurrentSkipListSet(comparator));
    }

    public SafetyTreeSet(Collection<? extends E> c) {
        this();
        this.addAll(c);
    }

    public SafetyTreeSet(SortedSet<E> s) {
        this(s.comparator());
        this.addAll(s);
    }

    @Override
    public Iterator<E> iterator() {
        return this.css.iterator();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return this.css.descendingIterator();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return this.css.descendingSet();
    }

    @Override
    public int size() {
        return this.css.size();
    }

    @Override
    public boolean isEmpty() {
        return this.css.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.css.contains(o);
    }

    @Override
    public boolean add(E e) {
        return this.css.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.css.remove(o);
    }

    @Override
    public void clear() {
        this.css.clear();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.css.addAll(c);
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return this.css.subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return this.css.headSet(toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return this.css.tailSet(fromElement, inclusive);
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return this.subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return this.headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return this.tailSet(fromElement, true);
    }

    @Override
    public Comparator<? super E> comparator() {
        return this.css.comparator();
    }

    @Override
    public E first() {
        return this.css.first();
    }

    @Override
    public E last() {
        return this.css.last();
    }

    @Override
    public E lower(E e) {
        return this.css.lower(e);
    }

    @Override
    public E floor(E e) {
        return this.css.floor(e);
    }

    @Override
    public E ceiling(E e) {
        return this.css.ceiling(e);
    }

    @Override
    public E higher(E e) {
        return this.css.higher(e);
    }

    @Override
    public E pollFirst() {
        return this.css.pollFirst();
    }

    @Override
    public E pollLast() {
        return this.css.pollLast();
    }

    @Override
    public Object clone() {
        return this.css.clone();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(this.css.comparator());
        s.writeInt(this.css.size());
        for (E e : this.css) {
            s.writeObject(e);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not support");
    }

    @Override
    public Spliterator<E> spliterator() {
        return this.css.spliterator();
    }
}
