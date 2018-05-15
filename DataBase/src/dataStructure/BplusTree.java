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
            if(node.isLeaf && newNode.isLeaf)
            {
                newNode.next = node.next;
                node.next = newNode;
                newNode.previous = node;
            }
            return true;
        } catch (Error e) {
            return false;
        }
    }

    private boolean insertSort(BplusNode p, K key, BplusNode child)             // insert key into root or normal node
    {
        p.children.add(null);
        p.keys.add(key);
        int t = p.keys.size() - 1;
        while(t>=0 && p.keys.get(t).compareTo(key)>0)                           // insert sort
        {
            p.keys.set(t+1, p.keys.get(t));
            p.children.set(t+1, p.children.get(t));
            t--;
        }
        p.keys.set(t+1, key);                                                   // insert
        p.children.set(t+1, child);
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
        }
        int idx = p.keys.indexOf(key);
        if(idx >= 0)                                                                // key is already in leaves
        {
            p.flags.get(idx).append(flag);
            return  true;
        }
        insertSort(p, key, flag);                                                   // insert new data
        while(p.keys.size() >= rank);
        {
            BplusNode newNode = new BplusNode(false, p.isLeaf);
            if(!divideBplusNode(p, newNode)) {
                return false;
            }
            if(p.parent == null)
            {
                BplusNode newParent = new BplusNode(p.isRoot, false);
                newParent.keys.add(p.keys.get(p.keys.size()-1));
                newParent.keys.add(newNode.keys.get(newNode.keys.size()-1));
                newParent.children.add(p);
                newParent.children.add(newNode);
                p.parent = newParent;
            }
            else
            {
                int location = p.parent.children.indexOf(p);
                p.parent.children.remove(location);
                p.parent.keys.remove(location);
                insertSort(p.parent, p.keys.get(p.keys.size()-1), p);
                insertSort(p.parent, newNode.keys.get(newNode.keys.size()-1), newNode);
            }
            newNode.parent = p.parent;                                              // renew parent pointer
            p = p.parent;                                                           // iterate to renew all nodes
        }
        while(p.parent != null)                                                     // renew keys
        {

            p = p.parent;
        }
        return true;
    }

    public boolean delete(K key)
    {
        return true;
    }

    public boolean alter(K key, K newKey)
    {
        return true;
    }

    public String contentOf(K key)
    {
        return "";
    }
}
