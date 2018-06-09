package utils;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;

public class BplusTree<K extends Comparable<K>, F extends Comparable<F>>
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

        List<F> flags = null;

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

    private BplusNode locate(K key)
    {
        BplusNode p = this.root;
        while (!p.isLeaf)
            p = p.children.get(binarySearch(key, p.keys));
        return p;
    }

    private int binarySearch(K key, List<K> keys)                                   // find the next node index
    {
        int low = 0, high = keys.size() - 1, mid, cmp;
        while (low <= high) {
            mid = (low + high) / 2;
            cmp = keys.get(mid).compareTo(key);
            if (cmp == 0)
                return mid;
            else if (cmp > 0) {
                if (mid == 0 || keys.get(mid - 1).compareTo(key) < 0)
                    return mid;
                else
                    high = mid - 1;
            } else
                low = mid + 1;
        }
        return low - 1;
    }

    private boolean divideBplusNode(BplusNode node, BplusNode newNode)
    {
        try {
            int mid = node.keys.size() / 2, cpLen = node.keys.size() - mid;
            boolean isLeaf = node.isLeaf && newNode.isLeaf;
            while (cpLen-- > 0) {
                K key = node.keys.remove(mid);
                newNode.keys.add(key);
                if (isLeaf) {
                    F flag = node.flags.remove(mid);
                    newNode.flags.add(flag);
                } else {
                    BplusNode child = node.children.remove(mid);
                    child.parent = newNode;
                    newNode.children.add(child);
                }
            }
            if (isLeaf) {
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
        int t = p.keys.size() - 2;
        while (t >= 0 && p.keys.get(t).compareTo(key) > 0)                           // insert sort
        {
            p.keys.set(t + 1, p.keys.get(t));
            p.children.set(t + 1, p.children.get(t));
            t--;
        }
        p.keys.set(t + 1, key);                                                         // insert
        p.children.set(t + 1, child);
        return true;
    }

    private boolean insertSort(BplusNode p, K key, F flag)                              // insert data into leaf
    {
        p.flags.add(flag);                                                              // increace length
        p.keys.add(key);
        int t = p.keys.size() - 2;
        while (t >= 0 && p.keys.get(t).compareTo(key) > 0)                              // insert sort
        {
            p.keys.set(t + 1, p.keys.get(t));
            p.flags.set(t + 1, p.flags.get(t));
            t--;
        }
        p.keys.set(t + 1, key);                                                         // insert
        p.flags.set(t + 1, flag);
        return true;
    }

    public boolean insert(K key, F flag)
    {
        BplusNode p = this.root;
        while (!p.isLeaf) {
            if(p.keys.contains(key))
                return false;                                                         // key is already in this tree
            if (key.compareTo(p.keys.get(p.keys.size() - 1)) > 0) {
                p.keys.set(p.keys.size() - 1, key);                                   // renew key
                p = p.children.get(p.keys.size() - 1);
            } else {
                int location = binarySearch(key, p.keys);
                p = p.children.get(location);
            }
        }
        if(p.keys.contains(key))                                                      // key is already in the leaf
            return false;
        if (!insertSort(p, key, flag))                                                // insert new data
            return false;
        while (p.keys.size() > rank) {
            BplusNode newNode = new BplusNode(false, p.isLeaf);
            if (!divideBplusNode(p, newNode)) {
                return false;
            }
            if (p.parent == null)                                                     // leaf doesn't have parent
            {
                BplusNode newParent = new BplusNode(true, false);
                newParent.keys.add(p.keys.get(p.keys.size() - 1));
                newParent.keys.add(newNode.keys.get(newNode.keys.size() - 1));
                newParent.children.add(p);
                newParent.children.add(newNode);
                root = newParent;                                                   // renew root
                p.parent = newNode.parent = root;
                p.isRoot = false;
                return true;
            } else {
                int location = p.parent.children.indexOf(p);
                p.parent.children.remove(location);
                p.parent.keys.remove(location);
                if (!(insertSort(p.parent, p.keys.get(p.keys.size() - 1), p) && insertSort(p.parent, newNode.keys.get(newNode.keys.size() - 1), newNode)))
                    return false;
                newNode.parent = p.parent;
            }
            p = p.parent;                                                           // iterate to renew all nodes
        }
        return true;
    }

    private void show()
    {
        BplusNode p = this.root;
        Queue<BplusNode> q = new LinkedList<>();
        q.add(p);
        System.out.println(p.keys);
        while (!q.isEmpty()) {
            ArrayList<BplusNode> a = new ArrayList<>();
            while (!q.isEmpty())
                a.add(q.poll());
            while (!a.isEmpty()) {
                BplusNode pos = a.remove(0);
                if (pos.children != null) {
                    for (BplusNode n : pos.children) {
                        q.add(n);
                        System.out.print(n.keys + " ");
                    }
                }
            }
            System.out.println();
        }
    }

    private boolean mergeBplusNode(BplusNode node1, BplusNode node2)
    {
        int len = node2.keys.size();
        for (int i = 0; i < len; i++)
            node1.keys.add(node2.keys.remove(0));
        if (node1.isLeaf && node2.isLeaf) {
            for (int i = 0; i < len; i++)
                node1.flags.add(node2.flags.remove(0));
            node1.next = node2.next;
            if (node2.next != null)
                node2.next.previous = node1;
            node2.flags = null;                                                         // free resource
        } else {
            for (int i = 0; i < len; i++)
                node1.children.add(node2.children.remove(0));
            node2.children = null;                                                      // free resource
        }
        node2.previous = null;                                                          // free resource
        node2.next = null;
        node2.keys = null;
        return true;
    }

    public boolean remove(K key)
    {
        BplusNode p = this.root;
        if (p.keys.size() == 0 || key.compareTo(p.keys.get(p.keys.size() - 1)) > 0)                         // tree can't be empty and key can't be the largest
            return false;

        while (!p.isLeaf)
            p = p.children.get(binarySearch(key, p.keys));
        int location = p.keys.indexOf(key);
        if (location < 0)                                                                                   // key isn't in this tree
            return false;

        p.keys.remove(location);
        p.flags.remove(location);

        int leafMinSize = (int) Math.ceil(rank / 2.);
        while (!p.isRoot && p.keys.size() < leafMinSize) {
            int pos = p.parent.children.indexOf(p);                                                         // position of present child
            BplusNode brother = null;
            boolean left = false;
            if (pos - 1 > 0 && p.parent.children.get(pos - 1).keys.size() - 1 >= leafMinSize)               // try to borrow from brother if exists
            {
                brother = p.parent.children.get(pos - 1);
                left = true;
            } else if (pos + 1 < p.parent.children.size() && p.parent.children.get(pos + 1).keys.size() - 1 >= leafMinSize) {
                brother = p.parent.children.get(pos + 1);
            }
            if (brother != null)                                                                                // borrow
            {
                int last = brother.keys.size() - 1;
                if (p.isLeaf) {
                    if (!left) {
                        insertSort(p, brother.keys.remove(0), brother.flags.remove(0));
                    } else {
                        insertSort(p, brother.keys.remove(last), brother.flags.remove(last));
                    }
                } else {
                    if (!left) {
                        insertSort(p, brother.keys.remove(0), brother.children.remove(0));
                    } else {
                        insertSort(p, brother.keys.remove(last), brother.children.remove(last));
                    }
                }
                break;
            } else {                                                                                            // merge
                left = false;
                if (pos - 1 > 0) {
                    brother = p.parent.children.get(pos - 1);
                    left = true;
                } else if (pos + 1 < p.parent.children.size()) {
                    brother = p.parent.children.get(pos + 1);
                }
                if (left) {
                    mergeBplusNode(brother, p);
                    p.parent.keys.remove(pos);
                    p.parent.children.remove(pos);
                    pos--;
                    p = brother;
                } else {
                    mergeBplusNode(p, brother);
                    p.parent.keys.remove(pos + 1);
                    p.parent.children.remove(pos + 1);
                }
            }
            p.parent.keys.set(pos, p.keys.get(p.keys.size() - 1));                      // renew max key
            p = p.parent;
        }
        if (p.isRoot) {
            if (p.isLeaf)
                return true;
            if (p.keys.size() < 2) {
                this.root = p.children.remove(0);
                this.root.isRoot = true;
                return true;
            }
        }
        while (!p.isRoot) {
            int pos = p.parent.children.indexOf(p);
            p.parent.keys.set(pos, p.keys.get(p.keys.size() - 1));                            // renew max key
            p = p.parent;
        }
        return true;
    }

    public boolean contains(K key)
    {
        BplusNode p = this.root;
        while(!p.isLeaf)
        {
            if(p.keys.contains(key))
                return true;
            int location = binarySearch(key, p.keys);
            p = p.children.get(location);
        }
        if(p.keys.contains(key))
            return true;
        return false;
    }

    public F getFlag(K key)
    {
        BplusNode p = this.root;
        while(!p.isLeaf)
            p = p.children.get(binarySearch(key, p.keys));
        int location = p.keys.indexOf(key);
        return p.flags.get(location);
    }

    public int alter(K key, K newKey)
    {
        if(!contains(key))
            return -1;                                                                  // key isn't exist
        if(contains(newKey))
            return -2;                                                                  // new key is already existed
        F flag = getFlag(key);
        this.remove(key);
        this.insert(newKey, flag);
        return 0;
    }

    public String contentOf(K key)
    {
        return "";
    }

    public static void main(String[] args)
    {
        BplusTree<Integer, Integer> T = new BplusTree<>(10);
        int count = 500, t;
        while (count-- > 0)
            T.insert(count, 1);
        T.alter(0, 1);
        T.alter(0, 500);
        T.show();
    }
}
