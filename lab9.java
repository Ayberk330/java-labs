import java.util.Scanner;

abstract class Animal {
    private String name;

    public Animal(String name) {
        // FIX: Parametreyi sınıf alanına atarken "this." unutulmamalı
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // FIX: Parametreyi doğru alana atıyoruz
        this.name = name;
    }

    public abstract void speak();
}

class Dog extends Animal {
    private String breed;

    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public void speak() {
        // FIX: getName() çağrısı doğrudur, breed'i büyük harfe çevirmek isteğe bağlı
        System.out.println(getName() + " barks. Breed: " + breed.toUpperCase());
    }
}

class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    @Override
    public void speak() {
        System.out.println(getName() + " meows.");
    }
}

class AnimalTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter animal type (dog/cat): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter animal name: ");
        String name = scanner.nextLine();

        Animal animal = null;

        // FIX: String karşılaştırmasında equals veya equalsIgnoreCase kullanılmalı
        if (type.equalsIgnoreCase("dog")) {
            System.out.print("Enter dog breed: ");
            String breed = scanner.nextLine();
            animal = new Dog(name, breed);

        } else if (type.equalsIgnoreCase("cat")) {
            animal = new Cat(name);

        } else {
            System.out.println("Unknown animal type. Exiting.");
            scanner.close();
            return;
        }

        // CHANGED: animal hiç null değil burada, çünkü yukarıdaki bloktan return edilmiş olur
        animal.speak();

        scanner.close();
    }
}
