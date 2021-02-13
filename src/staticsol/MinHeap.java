package staticsol;

import java.util.HashMap;

public class MinHeap {
    private final Node[] heap;
    private int heapSize;
    private final HashMap<String, Integer> indexMap;

    public MinHeap(int n) {
        heap = new Node[n];
        heapSize = 0;
        indexMap = new HashMap<>();
    }

    public void heapify(int index) {
        while (left(index) != -1) {
            var rci = right(index);
            var lci = left(index);
            int min = heap[index].compareTo(heap[lci]) < 0 ? index : lci;
            if (rci != -1)
                min = heap[rci].compareTo(heap[min]) < 0 ? rci : min;
            if (min == index)
                return;
            swap(min == lci ? lci : rci, index);
            index = min == lci ? lci : rci;
        }
    }

    public Node poll() {
        var res = heap[0];
        swap(0, --heapSize);
        var dis = heap[0].getDistance();
        heap[0].setDistance(res.getDistance());
        updateDistance(heap[0].getID(), dis);
        return res;
    }

    public boolean updateDistance(String id, double distance) {
        var index = indexMap.get(id);
        var old = heap[index].getDistance();
        if (old == distance)
            return false;
        heap[index].setDistance(distance);
        if (old < distance) {
            heapify(index);
        } else {
            while (true) {
                int parent = parent(index);
                if (parent == -1 || heap[parent].compareTo(heap[index]) < 0)
                    return true;
                swap(parent, index);
                index = parent;
            }
        }
        return true;
    }

    private int left(int index) {
        var res = 2 * index + 1;
        return isOutOfBound(res) ? res : -1;
    }

    private int right(int index) {
        var res = 2 * index + 2;
        return isOutOfBound(res) ? res : -1;
    }

    private boolean isOutOfBound(int index) {
        return index >= 0 && index < heapSize;
    }

    public void push(Node newNode) {
        indexMap.put(newNode.getID(), heapSize);
        heap[heapSize] = newNode;
        int index = heapSize++;
        while (true) {
            int parent = parent(index);
            if (parent == -1 || heap[parent].compareTo(heap[index]) < 0)
                return;
            swap(parent, index);
            index = parent;
        }
    }

    private int parent(int index) {
        if (index < 1)
            return -1;
        return (index-1) / 2;
    }

    private void swap(int index1, int index2) {
        indexMap.put(heap[index1].getID(), index2);
        indexMap.put(heap[index2].getID(), index1);
        Node temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }
}
