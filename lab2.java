import java.util.*;

// Base Character Class
class Character {
    private String name;
    private double hitPoint;
    private String gender;
    private int level = 1;
    private int experience = 0;

    public Character(String name, double hitPoint, String gender) {
        this.name = name;
        this.hitPoint = hitPoint;
        this.gender = gender;
    }

    public String getName() { return name; }
    public double getHitPoint() { return hitPoint; }
    public String getGender() { return gender; }
    public int getLevel() { return level; }

    public void setHitPoint(double hitPoint) { this.hitPoint = hitPoint; }

    public double calculateDamage() { return hitPoint; }

    public void attack() {
        System.out.println(name + " is attacking! Damage: " + calculateDamage());
        gainExperience(20);
    }

    public void regeneratePower() {
        System.out.println(name + " is regenerating power...");
    }

    public void gainExperience(int xp) {
        experience += xp;
        if (experience >= 100) {
            levelUp();
        }
    }

    public void levelUp() {
        level++;
        experience = experience - 100;
        hitPoint += 10; // Increase HP when leveling up
        System.out.println(name + " leveled up! New level: " + level);
    }

    public void printInfo() {
        System.out.println("Name: " + name + ", HP: " + hitPoint + ", Gender: " + gender + ", Level: " + level);
    }
}

// Warrior Class
class Warrior extends Character {
    private int energy = 20;
    private int defense = 5;

    public Warrior(String name, double hitPoint, String gender) {
        super(name, hitPoint, gender);
    }

    private void rest() {
        energy += 20;
        System.out.println(getName() + " is resting. Energy restored: " + energy);
    }

    @Override
    public double calculateDamage() {
        return super.calculateDamage() * 1.2;
    }

    @Override
    public void attack() {
        if (energy < 10) {
            System.out.println(getName() + " is out of energy! Need rest.");
        } else {
            energy -= 10;
            super.attack();
            System.out.println(getName() + " remaining energy: " + energy);
        }
    }

    @Override
    public void regeneratePower() {
        rest();
    }
}

// Mage Class
class Mage extends Character {
    private int mana = 10;
    private double criticalChance = 0.1;

    public Mage(String name, double hitPoint, String gender) {
        super(name, hitPoint, gender);
    }

    private void drinkPotion() {
        mana += 10;
        System.out.println(getName() + " drank a potion. Mana restored: " + mana);
    }

    @Override
    public double calculateDamage() {
        return super.calculateDamage() * 0.8;
    }

    @Override
    public void attack() {
        if (mana < 5) {
            System.out.println(getName() + " needs to drink a potion!");
        } else {
            mana -= 5;
            double damage = calculateDamage();
            if (new Random().nextDouble() < criticalChance) {
                damage *= 2;
                System.out.println("Critical hit!");
            }
            System.out.println(getName() + " attacks! Damage: " + damage);
            gainExperience(20);
            System.out.println(getName() + " remaining mana: " + mana);
        }
    }

    @Override
    public void regeneratePower() {
        drinkPotion();
    }
}

// Player Class
class Player {
    private String name;
    private String password;
    private ArrayList<Character> characters = new ArrayList<>();

    public Player(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() { return name; }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public double getTotalDamage() {
        return characters.stream().mapToDouble(Character::calculateDamage).sum();
    }

    public void printPlayerInfo() {
        System.out.println("Player: " + name);
        characters.forEach(Character::printInfo);
    }
}

// Party Class
class Party {
    private String partyName;
    ArrayList<Character> members = new ArrayList<>();


    public Party(String partyName) {
        this.partyName = partyName;
    }

    public void addMember(Character character) {
        if (members.size() < 10) {
            members.add(character);
        } else {
            System.out.println("Party is full!");
        }
    }

    public void printPartyInfo() {
        System.out.println("Party: " + partyName);
        members.forEach(Character::printInfo);
    }
}

// Battle Class
class Battle {
    private Party party1, party2;
    private List<Character> team1, team2;

    public Battle(Party party1, Party party2) {
        this.party1 = party1;
        this.party2 = party2;
        this.team1 = new ArrayList<>(party1.members);
        this.team2 = new ArrayList<>(party2.members);
    }

    public void startBattle() {
        Random random = new Random();
        while (!team1.isEmpty() && !team2.isEmpty()) {
            Character attacker, defender;

            if (random.nextBoolean()) {
                attacker = team1.get(random.nextInt(team1.size()));
                defender = team2.get(random.nextInt(team2.size()));
            } else {
                attacker = team2.get(random.nextInt(team2.size()));
                defender = team1.get(random.nextInt(team1.size()));
            }

            double damage = attacker.calculateDamage();
            defender.setHitPoint(defender.getHitPoint() - damage);

            System.out.println(attacker.getName() + " attacks " + defender.getName() + " for " + damage + " damage!");

            if (defender.getHitPoint() <= 0) {
                System.out.println(defender.getName() + " has fallen!");
                team1.remove(defender);
                team2.remove(defender);
            }
        }
    }

}



// Game Class
class Game {
    public static void main(String[] args) {
        Player player1 = new Player("Alice", "password123");
        Player player2 = new Player("Bob", "securepass");
        Player player3 = new Player("Charlie", "charliepass");

        Warrior warrior1 = new Warrior("Thor", 100, "Male");
        Mage mage1 = new Mage("Merlin", 80, "Male");
        Warrior warrior2 = new Warrior("Athena", 110, "Female");
        Mage mage2 = new Mage("Morgana", 90, "Female");

        player1.addCharacter(warrior1);
        player1.addCharacter(mage1);
        player2.addCharacter(warrior2);
        player3.addCharacter(mage2);

        System.out.println("player infos");
        player1.printPlayerInfo();
        player2.printPlayerInfo();
        player3.printPlayerInfo();

        Party party1 = new Party("Knights");
        Party party2 = new Party("Wizards");

        party1.addMember(warrior1);
        party1.addMember(warrior2);
        party2.addMember(mage1);
        party2.addMember(mage2);

        Battle battle = new Battle(party1, party2);
        battle.startBattle();

        System.out.println("player infos after battle");
        player1.printPlayerInfo();
        player2.printPlayerInfo();
        player3.printPlayerInfo();

        Player topPlayer = null;
        double maxDamage = 0;
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        for (Player player : players) {
            double totalDamage = player.getTotalDamage();
            if (totalDamage > maxDamage) {
                maxDamage = totalDamage;
                topPlayer = player;
            }
        }

        if (topPlayer != null) {
            System.out.println("Strongest player: " + topPlayer.getName() + " (total damage: " + maxDamage + ")");
        }
    }
}
