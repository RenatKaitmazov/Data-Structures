package datastr.tree;

/**
 * Created by Renat Kaitmazov on 08/08/15.
 */

final class Node<T extends Comparable<T>> {

    private T data;
    private Node<T> parent;
    private Node<T> leftChild;
    private Node<T> rightChild;
    private boolean isRed = true;

    Node(T data) { this.data = data; }

    @Override
    public String toString() { return data.toString(); }

    // Getters
    public T data()             { return data; }
    public Node<T> leftChild()  { return leftChild; }
    public Node<T> rightChild() { return rightChild; }
    public Node<T> parent()     { return parent; }
    public boolean isRed()      { return isRed; }


    // Setters
    public void setData(T data)                     { this.data         = data; }
    public void setLeftChild(Node<T> leftChild)     { this.leftChild    = leftChild; }
    public void setRightChild(Node<T> rightChild)   { this.rightChild   = rightChild; }
    public void setParent(Node<T> parent)           { this.parent       = parent; }
    public void setColor(boolean color)             { isRed             = color; }

    // Helper methods
    public boolean hasLeftChild()   { return leftChild      != null; }
    public boolean hasRightChild()  { return rightChild     != null; }
    public boolean isLeftChild()    { return parent.leftChild == this; }
    public boolean isRightChild()   { return parent.rightChild == this; }
    public boolean isLeafNode()     { return leftChild == null && rightChild == null; }
    public void flipColor()         { isRed = !isRed; }
    public void blacken()           { isRed = false; }

    // Helper method for insertion in a red black tree
    public Node<T> uncle() {
        Node<T> grand = parent.parent;
        if (grand == null) return null;

        return parent.isLeftChild() ? grand.rightChild : grand.leftChild;
    }

    // Helper methods for deletion in a red black tree
    public Node<T> sibling() {
        if (parent == null) return null;
        return isLeftChild() ? parent.rightChild : parent.leftChild;
    }

    public Node<T> successor() {
        Node<T> successor = rightChild;
        while (successor.hasLeftChild()) successor = successor.leftChild;
        return successor;
    }

    public Node<T> outsideNephew() {
        return isLeftChild() ? sibling().rightChild() : sibling().leftChild();
    }

    public Node<T> insideNephew() {
        return isLeftChild() ? sibling().leftChild() : sibling().rightChild();
    }
}
