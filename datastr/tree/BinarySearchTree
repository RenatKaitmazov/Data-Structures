package datastr.tree;

import datastr.tree.Node;

/**
 * Created by Renat Kaitmazov on 08/08/15.
 */

public final class BinarySearchTree<T extends Comparable<T>> {
    private Node<T> root    = null;
    private int count       = 0; // we can find out the size of the tree recursively but this approach is more efficient


    @Override
    public String toString() {
        if (isEmpty()) return "[]";

        StringBuilder builder = new StringBuilder("[");
        inOrderString(root, builder);

        int start   = builder.length() - 2;
        int end     = builder.length();
        builder.replace(start, end, "]");


        return builder.toString();
    }

    private void inOrderString(Node<T> node, StringBuilder builder) {
        if (node != null) {
            inOrderString(node.leftChild(), builder);
            builder.append(node.toString()).append(", ");
            inOrderString(node.rightChild(), builder);
        }
    }

                        /*** State Information ***/

    public int size()               { return count; }
    public boolean isEmpty()        { return root == null; }
    public boolean contains(T data) { return find(data) != null; }

                        /*** Main Operations ***/


                        /* Insertion */

    public void insert(T data) {
        ++count;

        Node<T> newNode = new Node<>(data);
        // To insert a new item, we first need to find an appropriate place
        // When a tree is empty, we just assign the item to the root node
        if (isEmpty())
            root = newNode;
        else {
        // If not, we have to traverse through the tree
            Node<T> current     = root;

            while (true) {
                // If the data is less than the current node, we go to the left of the current node
                if (data.compareTo(current.data()) < 0) {
                    if (current.hasLeftChild())
                        current = current.leftChild();
                    else {
                        current.setLeftChild(newNode);
                        break;
                    }
                } else {
                    // If the data is greater or equal to current node, we go to the right of the current node
                    if (current.hasRightChild())
                        current = current.rightChild();
                    else {
                        current.setRightChild(newNode);
                        break;
                    }
                }
            }

            newNode.setParent(current);
        }
    }

                        /* Searching */

    public Node<T> find(T key) {
        // The simplest operation
        if (isEmpty()) return null;

        Node<T> current = root;

        // We traverse through a tree until we find the item
        while (!current.data().equals(key)) {
            if (key.compareTo(current.data()) < 0)
                current = current.leftChild();
            else
                current = current.rightChild();

            // Couldn't find the item
            if (current == null) return null;
        }

        return current;
    }


                        /* Deletion */

    private Node<T> getReplacementNode(Node<T> nodeToDelete) {

        // This operation is involved when finding an appropriate successor to replace a node that
        // has two child nodes

        Node<T> replacementNode         = nodeToDelete.rightChild();
        Node<T> replacementNodeParent   = nodeToDelete;

        while (replacementNode.hasLeftChild()) {
            replacementNodeParent = replacementNode;
            replacementNode = replacementNode.leftChild();
        }


        if (replacementNode != nodeToDelete.rightChild()) {

            if (replacementNode.hasRightChild())
                replacementNodeParent.setLeftChild(replacementNode.rightChild());

            replacementNode.setRightChild(nodeToDelete.rightChild());
        }

        replacementNode.setLeftChild(nodeToDelete.leftChild());

        return replacementNode;
    }

    public T delete(T key) {
        if (isEmpty()) return null;

        Node<T> nodeToDelete    = root;
        Node<T> parentNode      = root;


        // First, we find the node to delete
        while (!nodeToDelete.data().equals(key)) {
            parentNode = nodeToDelete;

            if (key.compareTo(nodeToDelete.data()) < 0)
                nodeToDelete = nodeToDelete.leftChild();
            else
                nodeToDelete = nodeToDelete.rightChild();

            if (nodeToDelete == null) return null;
        }

        --count;

        if (nodeToDelete.isLeafNode()) {

            // If it is a leaf node, that is, does not have any children

            if (nodeToDelete == root)
                root = null;
            else if (nodeToDelete.isLeftChild())
                parentNode.setLeftChild(null);
            else
                parentNode.setRightChild(null);

        } else if (!nodeToDelete.hasRightChild()) {

            // If it has a left node only

            if (nodeToDelete == root)
                root = root.leftChild();
            else if (nodeToDelete.isLeftChild())
                parentNode.setLeftChild(nodeToDelete.leftChild());
            else
                parentNode.setRightChild(nodeToDelete.leftChild());

        } else if (!nodeToDelete.hasLeftChild()) {

            // If it has a right node only

            if (nodeToDelete == root)
                root = root.rightChild();
            else if (nodeToDelete.isLeftChild())
                parentNode.setLeftChild(nodeToDelete.rightChild());
            else
                parentNode.setRightChild(nodeToDelete.rightChild());

        } else {

            // If it has both nodes

            Node<T> replacementNode = getReplacementNode(nodeToDelete);

            if (nodeToDelete == root)
                root = replacementNode;
            else if (nodeToDelete.isLeftChild())
                parentNode.setLeftChild(replacementNode);
            else
                parentNode.setRightChild(replacementNode);

        }

        return nodeToDelete.data();
    }

    public T deleteMin() {
        if (isEmpty()) return null;

        Node<T> current = root;
        Node<T> parent  = root;

        while (current.hasLeftChild()) {
            parent = current;
            current = current.leftChild();
        }

        if (current == root)
            root = root.rightChild();
        else if (current.hasRightChild())
            parent.setLeftChild(current.rightChild());
        else
            parent.setLeftChild(null);

        --count;

        return current.data();
    }
    public T deleteMax() {
        if (isEmpty()) return null;

        Node<T> current = root;
        Node<T> parent  = root;

        while (current.hasRightChild()) {
            parent = current;
            current = current.rightChild();
        }


        if (current == root)
            root = root.leftChild();
        else if (current.hasLeftChild())
            parent.setRightChild(current.leftChild());
        else
            parent.setRightChild(null);

        --count;

        return current.data();
    }


    /**
     *
     * @return the smallest key
     */
    public T min() {
        if (isEmpty()) return null;

        Node<T> current = root;

        while (current.hasLeftChild())
            current = current.leftChild();

        return current.data();
    }

    /**
     *
     * @return the largest key
     */
    public T max() {
        if (isEmpty()) return null;

        Node<T> current = root;

        while (current.hasRightChild())
            current = current.rightChild();

        return current.data();
    }
}
