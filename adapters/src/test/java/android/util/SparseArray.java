package android.util;

import java.util.HashMap;

@SuppressWarnings("unused")
public class SparseArray<E> {

    private final HashMap<Integer, E> mHashMap;


    public SparseArray() {
        mHashMap = new HashMap<>();
    }


    public void put(int key, E value) {
        mHashMap.put(key, value);
    }

    public E get(int key) {
        return mHashMap.get(key);
    }

    public E valueAt(int index) {
        return mHashMap.get(index);
    }

    public int size() {
        return mHashMap.size();
    }

}
