import java.util.ArrayList;
class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}

class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String message) {
        super(message);
    }
}

class DuplicateMemberException extends Exception {
    public DuplicateMemberException(String message) {
        super(message);
    }
}

class DuplicateItemException extends Exception {
    public DuplicateItemException(String message) {
        super(message);
    }
}

class OverLimitException extends RuntimeException {
    public OverLimitException(String message) {
        super(message);
    }
}

class InvalidMemberNameException extends RuntimeException {
    public InvalidMemberNameException(String message) {
        super(message);
    }
}

class InvalidItemTitleException extends RuntimeException {
    public InvalidItemTitleException(String message) {
        super(message);
    }
}



class LibraryMember {
    private String name;
    private int memberId;
    private int borrowedCount;
    private int borrowLimit;

    public LibraryMember(String name, int memberId) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidMemberNameException("Library member name cannot be empty.");
        }
        this.name = name;
        this.memberId = memberId;
        this.borrowedCount = 0;
        this.borrowLimit = 5;
    }

    public String getName() {
        return name;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidMemberNameException("Library member name cannot be empty.");
        }
        this.name = name;
    }

    // Increments the borrowedCount, but if the limit is reached, throws an unchecked exception.
    public void borrowItem() {
        if (borrowedCount >= borrowLimit) {
            throw new OverLimitException("Borrow limit reached for member " + name);
        }
        borrowedCount++;
    }

    public void returnItem() {
        if (borrowedCount > 0) {
            borrowedCount--;
        }
    }

    @Override
    public String toString() {
        return "Member ID: " + memberId + ", Name: " + name +
                ", Borrowed: " + borrowedCount + "/" + borrowLimit;
    }
}

class LibraryItem {
    private String title;
    private String itemId;
    private boolean isBorrowed;

    public LibraryItem(String title, String itemId) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidItemTitleException("Library item title cannot be empty.");
        }
        this.title = title;
        this.itemId = itemId;
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getItemId() {
        return itemId;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void borrow() {
        if (!isBorrowed) {
            isBorrowed = true;
        }
    }

    public void returnItem() {
        if (isBorrowed) {
            isBorrowed = false;
        }
    }

    @Override
    public String toString() {
        return "Item ID: " + itemId + ", Title: " + title +
                ", Borrowed: " + isBorrowed;
    }
}



class LibrarySystem {
    private ArrayList<LibraryMember> members;
    private ArrayList<LibraryItem> items;

    public LibrarySystem() {
        members = new ArrayList<>();
        items = new ArrayList<>();
    }

    public void registerMember(LibraryMember member) throws DuplicateMemberException {
        if (findMemberById(member.getMemberId()) != null) {
            throw new DuplicateMemberException("Member with ID " + member.getMemberId() + " already exists.");
        }
        members.add(member);
    }

    public void addNewItem(LibraryItem item) throws DuplicateItemException {
        if (findItemById(item.getItemId()) != null) {
            throw new DuplicateItemException("Item with ID " + item.getItemId() + " already exists.");
        }
        items.add(item);
    }

    public LibraryMember findMemberById(int memberId) {
        for (LibraryMember m : members) {
            if (m.getMemberId() == memberId)
                return m;
        }
        return null;
    }

    public LibraryItem findItemById(String itemId) {
        for (LibraryItem i : items) {
            if (i.getItemId().equals(itemId))
                return i;
        }
        return null;
    }


    public void borrowItem(int memberId, String itemId) throws UserNotFoundException, ItemNotFoundException {
        LibraryMember member = findMemberById(memberId);
        if (member == null) {
            throw new UserNotFoundException("Member with ID " + memberId + " not found.");
        }
        LibraryItem item = findItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + itemId + " not found.");
        }
        if (item.isBorrowed()) {
            System.out.println("Item " + itemId + " is already borrowed.");
            return;
        }
        member.borrowItem(); // May throw OverLimitException if limit reached.
        item.borrow();
        System.out.println("Item " + itemId + " borrowed by member " + memberId);
    }

    public void returnItem(int memberId, String itemId) throws UserNotFoundException, ItemNotFoundException {
        LibraryMember member = findMemberById(memberId);
        if (member == null) {
            throw new UserNotFoundException("Member with ID " + memberId + " not found.");
        }
        LibraryItem item = findItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + itemId + " not found.");
        }
        if (!item.isBorrowed()) {
            System.out.println("Item " + itemId + " is not currently borrowed.");
            return;
        }
        member.returnItem();
        item.returnItem();
        System.out.println("Item " + itemId + " returned by member " + memberId);
    }

    public void printAllMembers() {
        System.out.println("=== Library Members ===");
        for (LibraryMember m : members) {
            System.out.println(m);
        }
    }

    public void printAllItems() {
        System.out.println("=== Library Items ===");
        for (LibraryItem i : items) {
            System.out.println(i);
        }
    }
}


 class Main {
    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();

        try {
            LibraryMember m1 = new LibraryMember("Alice", 101);
            LibraryMember m2 = new LibraryMember("Bob", 102);
            LibraryMember m3 = new LibraryMember("Charlie", 103);
            LibraryMember m4 = new LibraryMember("David", 101);

            system.registerMember(m1);
            system.registerMember(m2);
            system.registerMember(m3);
            try {
                system.registerMember(m4);
            } catch (DuplicateMemberException dme) {
                System.out.println("Caught Exception: " + dme.getMessage());
            }

            LibraryItem item1 = new LibraryItem("Java Programming", "I001");
            LibraryItem item2 = new LibraryItem("Data Structures", "I002");
            LibraryItem item3 = new LibraryItem("Algorithms", "I003");
            LibraryItem item4 = new LibraryItem("Operating Systems", "I002");

            system.addNewItem(item1);
            system.addNewItem(item2);
            system.addNewItem(item3);
            try {
                system.addNewItem(item4);
            } catch (DuplicateItemException die) {
                System.out.println("Caught Exception: " + die.getMessage());
            }

            system.printAllMembers();
            system.printAllItems();

            system.borrowItem(101, "I001");

            try {
                system.borrowItem(999, "I002");
            } catch (UserNotFoundException une) {
                System.out.println("Caught Exception: " + une.getMessage());
            }

            try {
                system.borrowItem(102, "I999");
            } catch (ItemNotFoundException ine) {
                System.out.println("Caught Exception: " + ine.getMessage());
            }


            for (int i = 0; i < 5; i++) {

                system.returnItem(103, "I003");
                system.borrowItem(103, "I003");
            }
            try {
                system.borrowItem(103, "I003");
            } catch (OverLimitException ole) {
                System.out.println("Caught Exception: " + ole.getMessage());
            }

            system.returnItem(101, "I001");

            try {
                LibraryMember mInvalid = new LibraryMember("", 104);
            } catch (InvalidMemberNameException imne) {
                System.out.println("Caught Exception: " + imne.getMessage());
            }

            try {
                LibraryItem iInvalid = new LibraryItem("", "I004");
            } catch (InvalidItemTitleException iite) {
                System.out.println("Caught Exception: " + iite.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
        }
    }
}
