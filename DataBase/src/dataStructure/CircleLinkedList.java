package dataStructure;

import java.io.Serializable;

public class CircleLinkedList<T extends Comparable<T>>
{
    private class Node
    {
        T data;
        Node next;
        private Node() {
            this.data = null;
            this.next = null;
        }
        private Node(T data) {
            this();
            this.data = data;
        }
    }
    private Node Tail;

    public CircleLinkedList() {
        Tail = new Node();
        Tail.next = Tail;
    }

    public boolean append(T data)
    {
        try {
            Node Head = Tail.next;
            Node newNode = new Node(data);
            newNode.next = Tail.next;
            Tail.next = newNode;
            Tail = newNode;
            return true;
        } catch(Error e) {
            return false;
        }
    }

    public boolean delete(T data)
    {
        Node preNode = locate(data);
        if(preNode == null)
            return false;
        preNode.next = preNode.next.next;
        return true;
    }

    private Node locate(T data)                                             // return pre Node, or null if not exist
    {
        Node p = Tail.next;
        while(p.next != Tail.next)
        {
            if(p.next.data.compareTo(data) == 0)
                return p;
            p = p.next;
        }
        return null;
    }

    public boolean insert(T data, T pos)
    {
        Node preNode = locate(pos);
        if(preNode == null)
            return false;
        try{
            Node newNode = new Node(data);
            newNode.next = preNode.next;
            preNode.next = newNode;
            return true;
        } catch(Error e) {
            return false;
        }
    }

    public boolean alter(T data, T newData)
    {
        Node preNode = locate(data);
        if(preNode == null)
            return false;
        preNode.next.data = newData;
        return true;
    }

    public boolean clear()
    {
        if(Tail == null)
            return false;
        Tail = Tail.next;
        Tail.next = Tail;
        return true;
    }

    public boolean contains(T data)
    {
        if(data == null)
            return false;
        return locate(data)==null? false: true;
    }

    private void show()
    {
        Node p = Tail.next;
        while(p.next != Tail.next)
        {
            p = p.next;
            System.out.print(p.data + " ");
        }
    }

    public String toString()
    {
        String retStr = "";
        Node p = Tail.next;
        while(p.next != Tail.next)
        {
            p = p.next;
            retStr += p.data.toString();
        }
        return retStr;
    }
}
