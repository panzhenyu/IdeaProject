package dataStructure;

public class LinkedList<T>
{
    private class Node
    {
        T data;
        Node preBrother = null;
        Node nextBrother = null;
        Node preElem = null;
        Node nextElem = null;
        private Node()
        {
            this(null);
        }
        private Node(T data)
        {
            this.data = data;
        }
    }
    private Node head;

    public LinkedList()
    {
        head = new Node();
    }

    public boolean append(T data)
    {
        try {
            Node newNode = new Node(data);
            newNode.preElem = head;
            head.nextElem = newNode;
            return true;
        } catch(Error e) {
            return false;
        }
    }

    public boolean delete(T data)
    {
        Node p = locate(data);
        if(p == null)
            return false;
        p.preElem.nextElem = p.nextElem;
        p.nextElem.preElem = p.preElem;
        p.preElem = p.nextElem = null;
        return true;
    }

    private Node locate(T data)
    {
        Node p = head.nextElem;
        while(!p.data.equals(data))
            p = p.nextElem;
        return p;
    }

    public boolean insert(T data, T pos)
    {
        Node p = locate(pos);
        if(p == null)
            return false;
        try{
            Node newNode = new Node(data);
            newNode.preElem = p;
            newNode.nextElem = p.nextElem;
            p.nextElem.preElem = newNode;
            p.nextElem = newNode;
            return true;
        } catch(Error e) {
            return false;
        }
    }

    public boolean alter(T data, T newData)
    {
        Node p = locate(data);
        if(p == null)
            return false;
        p.data = newData;
        return true;
    }

    public boolean clear()
    {
        try{
            head = null;
            return true;
        } catch(Error e) {
            return false;
        }
    }

    public boolean contains(T data)
    {
        Node p = locate(data);
        if(p == null)
            return false;
        return true;
    }

}
