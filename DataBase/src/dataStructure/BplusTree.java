package dataStructure;

import java.util.List;
import java.util.ArrayList;

public class BplusTree<K extends  Comparable<K>, F>
{
    private class BplusNode
    {
        boolean isLeaf;

        boolean isRoot;

        BplusNode parent = null;

        BplusNode previous = null;

        BplusNode next = null;

        List<BplusNode> children = null;

        List<K> keys;

        List<LinkedList<F>> flags = null;

        BplusNode(boolean isRoot, boolean isLeaf)
        {
            this.isRoot = isRoot;
            this.isLeaf = isLeaf;
            this.keys = new ArrayList<>();
            if (!this.isLeaf)
                this.children = new ArrayList<>();
            else
                this.flags = new ArrayList<>();
        }
    }

    private final static short defaultRand = 200;

    private final static byte NOTFOUND = -1;

    private int rank;

    private BplusNode root;

    private BplusNode sqt;

    public BplusTree(int rank)
    {
        this.rank = rank;
        this.root = new BplusNode(true, true);
        this.sqt = this.root;
    }

    public BplusTree()
    {
        this(BplusTree.defaultRand);
    }

    private int binaryLocate(K key, List<K> keys)                                   // find the next node index
    {
        if (keys.size() == 1)
            return 0;
        int low = 0, high = keys.size() - 1, mid, cmp;
        while (low <= high) {
            mid = (low + high) / 2;
            cmp = keys.get(mid).compareTo(key);
            if (cmp >= 0)
                return mid;
            else
                low = mid + 1;
        }
        return low;
    }

    private boolean divideBplusNode(BplusNode node, BplusNode newNode)
    {
        try {
            int mid = node.keys.size() / 2, cpLen = mid;
            while (cpLen-- > 0) {
                K key = node.keys.remove(mid);
                LinkedList<F> flag = node.flags.remove(mid);
                newNode.keys.add(key);
                newNode.flags.add(flag);
            }
            return true;
        } catch (Error e) {
            return false;
        }
    }

    private boolean insertSort(BplusNode p, K key)                              // insert key into root or normal node
    {
        p.keys.add(key);
        int t = p.keys.size() - 1;
        while(t>=0 && p.keys.get(t).compareTo(key)>0)                           // insert sort
        {
            p.keys.set(t+1, p.keys.get(t));
            t--;
        }
        p.keys.set(t+1, key);                                                   // insert
        return true;
    }
    private boolean insertSort(BplusNode p, K key, F flag)                      // insert data into leaf
    {
        LinkedList<F> newL = new LinkedList<>();
        newL.append(flag);
        p.flags.add(newL);                                                      // increace length
        p.keys.add(key);
        int t = p.keys.size() - 1;
        while(t>=0 && p.keys.get(t).compareTo(key)>0)                           // insert sort
        {
            p.keys.set(t+1, p.keys.get(t));
            p.flags.set(t+1, p.flags.get(t));
            t--;
        }
        p.keys.set(t+1, key);                                                   // insert
        p.flags.set(t+1, newL);
        return true;
    }

    public boolean insert(K key, F flag)
    {
        BplusNode p = this.root;
        while(!p.isLeaf)
        {
            int location = binaryLocate(key, p.keys);
            p = p.children.get(location);
            if(p.keys.get(location).compareTo(key) < 0)                             // alter parent keys with new max value
                p.keys.set(location, key);
        }
        int idx = p.keys.indexOf(key);
        if(idx >= 0)                                                                // key is already in leaves
        {
            p.flags.get(idx).append(flag);
            return  true;
        }
        insertSort(p, key, flag);                                                   // insert new data
        if(p.keys.size() >= rank)                                                   // divide if greater than rank
        {
            BplusNode newNode = new BplusNode(false, p.isLeaf);
            if(!divideBplusNode(p, newNode))
                return false;
            newNode.next = p.next;
            p.next = newNode;
            newNode.previous = p;
        }
        do{                                                                     // update parent with maxvalue of p
            break;
        } while(true);
        return true;
    }

    public boolean delete(K key)
    {
        return true;
    }

//    private BTNode locate(String data)
//    {
//
//    }

    public boolean alter(String data, String newData)
    {
        return true;
    }

    public String contentOf(String key)
    {
        return "";
    }
}
