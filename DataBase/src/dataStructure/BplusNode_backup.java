package dataStructure;

import java.lang.reflect.Array;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;

public class BplusNode_backup<K extends Comparable<K> ,F>
{
    private boolean isLeaf;

    private boolean isRoot;

    private BplusNode_backup<K, F> parent;

    private BplusNode_backup<K, F> previous;

    private BplusNode_backup<K, F> next;

    private List<BplusNode_backup<K, F>> children;

    private List<K> keys;

    private List<LinkedList<F>> flags;

    public BplusNode_backup(boolean isRoot, boolean isLeaf)
    {
        this.isRoot = isRoot;
        this.isLeaf = isLeaf;
        this.keys = new ArrayList<>();
        if(!this.isLeaf)
            this.children = new ArrayList<>();
        else
            this.flags = new ArrayList<>();
    }

    public boolean isLeaf()
    {
        return this.isLeaf;
    }

    public boolean isRoot()
    {
        return this.isRoot;
    }

    public boolean setParent(BplusNode_backup<K, F> parent)
    {
        if(parent == null)
            return false;
        this.parent = parent;
        return true;
    }

    public boolean insert()
    {
        return true;
    }

    public boolean devide()
    {
        return true;
    }

    public boolean alterKey(K key, K newKey)
    {
        return true;
    }

    public boolean alterFlag(K key, F newFlag)
    {
        return true;
    }

    public boolean delete(K key)
    {
        return true;
    }

//    public F get(K key)
//    {
//    }

    public static void main(String[] args)
    {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        System.out.println(a.indexOf(1));

    }
}
