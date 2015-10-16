package datastr.tree;

import java.lang.reflect.Array;

/**
 * Created by Renat Kaitmazov on 17/08/15.
 */

public final class RedBlackBST<T extends Comparable<T>> {

    /** This part is for SortedMap **/
    /************************************************************/
    private static class Item<K extends Comparable<K>, V> implements Comparable<Item<K, V>> {
        private K key;
        private V value;

        public Item(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() { return String.format("%s=%s", key.toString(), value.toString()); }

        public void setValue(V value) { this.value = value; }

        public int compareTo(Item<K, V> item) {
            if (key.compareTo(item.key) > 0) return 1;
            if (key.compareTo(item.key) < 0) return -1;
            return 0;
        }
    }

    public static class RBTreeDecorator<K extends Comparable<K>, V> {
        private Class keyClass;
        private Class valueClass;
        private RedBlackBST<Item<K, V>> storage = new RedBlackBST<>();

        @Override
        public String toString() { return storage.toString(); }

        public boolean isEmpty() { return storage.isEmpty(); }
        public int size() { return storage.size(); }
        public boolean contains(K key) { return find(key) != null; }

        private Item<K, V> find(K key) {
            if (isEmpty()) return null;

            Node<Item<K,V>> node = storage.root;

            while (node != null) {
                if (node.data().key.equals(key)) return node.data();
                if (node.data().key.compareTo(key) > 0) node = node.leftChild();
                else node = node.rightChild();
            }

            return null;
        }

        public V get(K key) {
            Item<K, V> item = find(key);
            return item != null ? item.value : null;
        }

        public void insert(K key, V value) {
            keyClass = key.getClass();
            valueClass = value.getClass();
            Item<K, V> oldValue = find(key);
            if (oldValue == null) storage.insert(new Item<K, V>(key, value));
            else {
                oldValue.setValue(value);
            }
        }

        public V delete(K key) {
            Item<K, V> itemToDelete = find(key);
            return itemToDelete != null ? storage.delete(itemToDelete).value : null;
        }

        public K[] keySet() {
            K[] keys = (K[]) Array.newInstance(keyClass, size());
            int i = 0;
            for (Item<K, V> item: storage.toArray()) keys[i++] = item.key;
            return keys;
        }

        public V[] values() {
            V[] values = (V[]) Array.newInstance(valueClass, size());
            int i = 0;
            for (Item<K, V> item: storage.toArray()) values[i++] = item.value;
            return values;
        }
    }
                    /** This part is for SortedMap **/
    /**********************************************************/

    private Node<T> root;
    private int count = 0;
    private Class clazz; // Need it for toArray() method

    public boolean isEmpty() { return root == null; }
    public int size() { return count; }
    public boolean contains(T element) { return get(element) != null; }
    public int depth() { return depth(root); }

    private int depth(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(depth(node.leftChild()), depth(node.rightChild()));
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";

        StringBuilder builder = new StringBuilder("[");

        inOrder(root, builder);

        int start   = builder.length() - 2;
        int end     = builder.length();
        builder.replace(start, end, "]");

        return builder.toString();
    }

    private void inOrder(Node<T> node, StringBuilder sb) {
        if (node != null) {
            inOrder(node.leftChild(), sb);
            sb.append(node).append(", ");
            inOrder(node.rightChild(), sb);
        }
    }

    /********************   INSERTION   ***************************/
    /**************************************************************/

    private void flipColor(Node<T> parentNode) {
        // Here we flip the color of the node and its children.
        // Switch the parent's color unless it is the root (the root is always black, there is no need in flipping its color)
        if (parentNode != root)
            parentNode.flipColor();
        parentNode.leftChild().flipColor(); // Flip left child's color
        parentNode.rightChild().flipColor(); // Flip right child's color
    }

    private void rotateRight(Node<T> nodeToRotate) {
        // When rotating to the right, the node being rotated must have a left child
        Node<T> left = nodeToRotate.leftChild();

        // If the node being rotated is not the root, then it must have the parent
        // Inside "if" block we will change references so that the node being rotated will go down to the right (we perform a right rotation)
        // and its left child will go up becoming a new parent with respect to the node being rotated
        if (nodeToRotate != root) {

            if (nodeToRotate.isLeftChild())
                nodeToRotate.parent().setLeftChild(left);
            else
                nodeToRotate.parent().setRightChild(left);

            left.setParent(nodeToRotate.parent());
        } else {
            left.setParent(null);
            root = left;
        }

        if (left.hasRightChild()) {
            nodeToRotate.setLeftChild(left.rightChild());
            left.rightChild().setParent(nodeToRotate);
        } else
            nodeToRotate.setLeftChild(null);

        left.setRightChild(nodeToRotate);
        nodeToRotate.setParent(left);
    }

    private void rotateLeft(Node<T> nodeToRotate) {
        // When rotating to the left, the node being rotated must have a right child
        Node<T> right = nodeToRotate.rightChild();

        // If the node being rotated is not the root, then it must have a parent
        // Inside "if" block we will change references so that the node being rotated will go down to the left (we perform a left rotation)
        // and its right child will go up becoming a new parent with respect to the node being rotated
        if (nodeToRotate != root) {
            if (nodeToRotate.isLeftChild())
                nodeToRotate.parent().setLeftChild(right);
            else
                nodeToRotate.parent().setRightChild(right);

            right.setParent(nodeToRotate.parent());
        } else {
            right.setParent(null);
            root = right;
        }

        if (right.hasLeftChild()) {
            nodeToRotate.setRightChild(right.leftChild());
            right.leftChild().setParent(nodeToRotate);
        } else
            nodeToRotate.setRightChild(null);

        right.setLeftChild(nodeToRotate);
        nodeToRotate.setParent(right);
    }

    private void fixUp(Node<T> node) {
        Node<T> parent = node.parent();
        // If we have two red nodes in a row
        if (parent != null && parent.isRed()) {
            // we need to the color of its uncle
            Node<T> grandParent = parent.parent();
            Node<T> uncle       = node.uncle();

            // If the uncle is black, we have to perform rotations
            // How do we know how many rotations we need to perform?
            // We should find out whether the passed node is an inside or an outside child
            // If it inside node, we perform one rotation, otherwise we perform two
            if (uncle == null || !uncle.isRed()) {

                grandParent.flipColor();
                // If the parent is a left child
                if (parent.isLeftChild()) {

                    // and the node is a left child as well, it means the node is an outside child
                    // so we need to perform only one right rotation
                    if (node.isLeftChild())
                        parent.flipColor();
                    else {
                        // but if the node is a right child, the node is an inside child
                        // and thus we need to perform two rotations: one left rotation and one right
                        node.flipColor();
                        rotateLeft(parent);
                    }

                    // performing a right rotation
                    rotateRight(grandParent);

                } else {

                    // If the parent is a right child
                    if (node.isLeftChild()) {

                        // and the node is a right child, then we know it is an inside child
                        // so we need two rotations to maintain balance (right and left)
                        node.flipColor();
                        // at first perform a right rotation
                        rotateRight(parent);
                    } else
                        parent.flipColor();
                    // but if the node is a left child just like its parent, then it must be an outside child
                    // so perform only one rotation
                    rotateLeft(grandParent);
                }


            } else {
                // If the uncle is red, we just flip colors
                flipColor(grandParent);

                // And it is possible that after color flipping we can have two red nodes in a row
                // To resolve this problem call this method again to check whether it is true or not
                fixUp(grandParent);
            }
        }
    }

    public void insert(T element) {
        clazz = element.getClass();
        ++count;
        Node<T> newNode = new Node<>(element);

        if (isEmpty()) {
            root = newNode;
            root.blacken();
        } else {

            Node<T> current = root;

            while (true) {
                if (element.compareTo(current.data()) < 0) {
                    if (current.hasLeftChild())
                        current = current.leftChild();
                    else {
                        current.setLeftChild(newNode);
                        break;
                    }
                } else {
                    if (current.hasRightChild())
                        current = current.rightChild();
                    else {
                        current.setRightChild(newNode);
                        break;
                    }
                }
            }

            newNode.setParent(current);

            fixUp(newNode);
        }
    }


    /*********************  GETTING ELEMENTS    **********************/
    /*****************************************************************/
    public T get(T element) {
        Node<T> node = find(element);
        // If the node is not null, return its data otherwise return null
        return node != null ? node.data() : null;
    }

    private Node<T> find(T element) {
        Node<T> current = root;

        while (current != null && !current.data().equals(element)) {
            if (element.compareTo(current.data()) < 0)
                current = current.leftChild();
            else
                current = current.rightChild();
        }

        return current != null ? current : null;
    }

    public T min() {
        if (isEmpty()) return null;

        Node<T> current = root;

        while (current.hasLeftChild())
            current = current.leftChild();

        return current.data();
    }

    public T max() {
        if (isEmpty()) return null;

        Node<T> current = root;

        while (current.hasRightChild())
            current = current.rightChild();

        return current.data();
    }


    /**********************     DELETION    **************************/
    /*****************************************************************/

    private void swap(Node<T> a, Node<T> b) {
        T temp = a.data();
        a.setData(b.data());
        b.setData(temp);
    }
    
    private T deleteAndFix(Node<T> nodeToDelete) {
        // Deletion in a red black tree is trickier
        // Here we need to check the colors of the node being deleted, and its sibling
        // and take appropriate actions to restore the tree's balance. Usually the node being deleted marked as double black
        // but I'm not going to do so. I am going to use a reference instead. Let's call it "doubleBlackNode".
        // There are 6 cases we should consider when deleting a node from a red black tree.
        Node<T> doubleBlackNode = nodeToDelete; // I don't have an additional field in my Node class, so I use this reference
        Node<T> sibling = nodeToDelete.sibling();
        Node<T> outsideNephew, insideNephew;

        if (nodeToDelete.isRed()) {
            // CASE 1. If the node being deleted is red. Just delete it since no rules will be violated
            if (nodeToDelete.isLeftChild()) nodeToDelete.parent().setLeftChild(null);
            else nodeToDelete.parent().setRightChild(null);
            return nodeToDelete.data();
        }

        if (nodeToDelete.parent().isRed() && sibling == null) {
            // CASE 2. If the node being deleted is black, its parent is red, and it does not have a sibling
            // Just delete it and color its parent black
            nodeToDelete.parent().blacken();
            if (nodeToDelete.isLeftChild()) nodeToDelete.parent().setLeftChild(null);
            else nodeToDelete.parent().setRightChild(null);
            return nodeToDelete.data();
        }

        while (!doubleBlackNode.isRed() && doubleBlackNode != root) {
            sibling = doubleBlackNode.sibling();
            if (sibling.isRed()) {
                // CASE 3. The sibling of the node being deleted is red. Rotate around the parent. Switch the parent's
                // color to red, and the sibling's color to black
                doubleBlackNode.parent().flipColor();
                sibling.flipColor();
                if (doubleBlackNode.isLeftChild()) rotateLeft(doubleBlackNode.parent());
                else rotateRight(doubleBlackNode.parent());
            } else if ((outsideNephew = doubleBlackNode.outsideNephew()) != null && outsideNephew.isRed()) {
                // CASE 4. If the outside nephew of the node being deleted is red, rotate around the parent
                // color the nephew black, the sibling gets its parent's color, and the parent becomes black
                sibling.setColor(doubleBlackNode.parent().isRed());
                doubleBlackNode.outsideNephew().blacken();
                if (doubleBlackNode.isLeftChild()) rotateLeft(doubleBlackNode.parent());
                else rotateRight(doubleBlackNode.parent());
                doubleBlackNode = doubleBlackNode.parent();

            } else if ((insideNephew = doubleBlackNode.insideNephew()) != null && insideNephew.isRed()) {
                // CASE 5. The inside nephew is red. Rotate around the sibling, color it red, color the nephew black
                // so that we will enter the case 4
                insideNephew.blacken();
                sibling.setColor(true);
                if (doubleBlackNode.isLeftChild()) rotateRight(sibling);
                else rotateLeft(sibling);
            } else {
                // CASE 6. The node being deleted has a black sibling and black nephews.
                // Make the sibling red, move the reference to the double black node up to consider the cases above
                sibling.setColor(true);
                doubleBlackNode = doubleBlackNode.parent();
            }
        }

        // If in the case 6 or 4 the parent is red, we will stop executing the while loop
        // and below we blacken it
        if (doubleBlackNode != nodeToDelete) doubleBlackNode.blacken();


        // What we have done so far was keeping balance of the tree, now
        // we will perform the actual deletion
        if (nodeToDelete.isLeftChild()) nodeToDelete.parent().setLeftChild(null);
        else nodeToDelete.parent().setRightChild(null);

        return nodeToDelete.data();
    }

    public T delete(T element) {
        // First of all, find the node we are looking for
        Node<T> nodeToDelete;
        if (isEmpty() || (nodeToDelete = find(element)) == null) return null;

        --count;

        // If this node is in the tree, we will delete it and return its data
        while (true) {
            // Now we need to know whether the node is a leaf, has one child or has two children
            if (nodeToDelete.isLeafNode()) {
                // CASE 1
                T temp = nodeToDelete.data();
                // If it is a leaf, we need to know whether it is the root
                // If so, it means this node is the only one in the tree, so just remove it
                if (nodeToDelete == root) root = null;
                // But if it isn't the root it means we are about to delete the node from the bottom of the tree
                // and delegate this task to deleteAndFix() method
                else temp = deleteAndFix(nodeToDelete);

                return temp;

            } else if (!nodeToDelete.hasRightChild()) {
                // CASE 2
                // If the node being deleted has a left child only
                // and it is the root
                if (nodeToDelete == root) {
                    // Break the reference from that child to the parent, that is the node being deleted
                    // And make its left child the root
                    nodeToDelete.leftChild().setParent(null);
                    root = nodeToDelete.leftChild();
                    return nodeToDelete.data();
                } else {
                    // But if it is the root, swap the values of the node being deleted and its left child, and then remove that
                    // left child. Thus we will always end up deleting a leaf node
                    swap(nodeToDelete, nodeToDelete.leftChild());
                    nodeToDelete = nodeToDelete.leftChild();
                }

            } else if (!nodeToDelete.hasLeftChild()) {
                // CASE 3
                // Everything is the same as above except for now the node being deleted has a right child only
                if (nodeToDelete == root) {
                    nodeToDelete.rightChild().setParent(null);
                    root = nodeToDelete.rightChild();
                    return nodeToDelete.data();
                } else {
                    swap(nodeToDelete, nodeToDelete.rightChild());
                    nodeToDelete = nodeToDelete.rightChild();
                }
            } else {
                // CASE 4
                // This is the case when the node being deleted has two children.
                // And to delete it we have to find the node with the least value in the right tree of the node being deleted
                // which we call its inorder successor
                Node<T> successor = nodeToDelete.successor();
                // Swap their values
                swap(nodeToDelete, successor);
                // Delete the successor and the control flow will eventually enter the case 1
                nodeToDelete = successor;
            }
        }
    }

    public T[] toArray() {
        T[] array = (T[]) Array.newInstance(clazz, count);

        inOrder(root, array, 0);

        return array;
    }

    private int inOrder(Node<T> node, T[] array, int i) {
        if (node != null) {
            i = inOrder(node.leftChild(), array, i);
            array[i++] = node.data();
            i = inOrder(node.rightChild(), array, i);
        }
        return i;
    }
}
