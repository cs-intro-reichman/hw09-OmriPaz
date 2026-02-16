/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        // Your code goes here
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData cd = new CharData(chr);
        Node nd = new Node(cd, this.first);
        this.first = nd;
        this.size++;
    }
    
    /** GIVE Textual representation of this list. */
    @Override
    public String toString() {
        String str = "";
        Node dummy = first;
        for (int i = 0; i < this.size; i++) {
            str += dummy;
            dummy = dummy.next;
        }
        return str;
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        Node dummy = first;
        for (int i = 0; i < this.size; i++) {
            if (dummy.cp.equals(chr)) {return i;}
            else {
                dummy = dummy.next;
            }
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
       Node dummy = first;
        for (int i = 0; i < this.size; i++) {
            if (dummy.cp.equals(chr)) {
                dummy.cp.count++;
                return;
            }
            dummy = dummy.next;
       }
       CharData cd = new CharData(chr);
       Node new_node = new Node(cd, this.first);
       this.first = new_node;
       this.size++;
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        Node dummy = first;
        if (dummy.cp.equals(chr)) {
            this.first = dummy.next;
            this.size--;
            return true;
        }
        for (int i = 0; i < this.size - 1; i++) {
            if (dummy.next.cp.equals(chr)) { // Remove dummy.next
                Node new_next = dummy.next.next;
                dummy.next = new_next;
                this.size--;
                return true;
            }
            dummy = dummy.next;
        }
        return false;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index >= this.size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Node dummy = first;
        for (int i = 0; i < index; i++) {
            dummy = dummy.next;
        }
        return dummy.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}