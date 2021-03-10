import java.util.Random;

public class Main {

    public static int bossHealth = 1000;
    public static int bossDamage = 50;
    public static int defaultBossDamage = 50; // для метода thorStunsBoss
    public static boolean isBossStunned; // для метода thorStunsBoss
    public static String bossDefenceType = "";
    public static int[] heroesHealth = {260, 250, 240, 100, 150, 100, 300, 280};
    public static int[] heroesDamages = {25, 20, 15, 15, 10, 10, 5, 0};
    public static String[] heroesAttackTypes = {"Physical", "Magical", "Kinetic", "Thor", "Berserk", "Lucky", "Golem", "Medic"};

    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            round();
        }
    }

    public static void round() {
        if (bossHealth > 0) {
            changeBossDefence();
            bossHits();
        }
        thorStunsBoss();
        berserkReturnsDamage();
        heroesHit();
        golemGetsPartOfDamage();
        luckyEvades();
        medicHealsHeroes();
        printStatistics();
    }

    public static void medicHealsHeroes() { // - Д/З (Medic)
        Random r = new Random();
        int random = r.nextInt(heroesHealth.length - 1); // Medic всегда последний в массивах
        int heal = 20;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[heroesHealth.length - 1] != 0 && heroesHealth[random] != heroesHealth[heroesHealth.length - 1]) {
                if (heroesHealth[random] < 100 && heroesHealth[random] != 0) {
                    heroesHealth[random] = heroesHealth[random] + heal;
                    System.out.println("Heal " + heroesAttackTypes[random] + " + " + heal);
                }
                break;
            }
        }
    }

    public static void golemGetsPartOfDamage(){ // Д/З (Golem)
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[heroesHealth.length - 2] != 0 && !isBossStunned) { // Golem всегда второй с конца в массивах
                heroesHealth[i] = heroesHealth[i] + (bossDamage/5);
                heroesHealth[heroesHealth.length - 2]  = heroesHealth[heroesHealth.length - 2]  - (bossDamage/5);
            }
        }
    }

    private static void luckyEvades() { // - Д/З (Lucky)
        Random ran = new Random();
        int r = ran.nextInt(2);
        if (r == 0) {
            heroesHealth[heroesHealth.length - 3] += bossDamage; // Lucky всегда третий с конца в массивах
            System.out.println(heroesAttackTypes[heroesAttackTypes.length - 3] + " " + "evades");
        }
    }

    private static void berserkReturnsDamage() { // Д/З Berserk
        int partOfDamage = bossDamage / 2;
        if(heroesHealth[heroesHealth.length - 4] != 0 && !isBossStunned) { // Berserk всегда четвертый с конца в массивах
            heroesHealth[heroesHealth.length - 4] += bossDamage;
            heroesHealth[heroesHealth.length - 4] -= partOfDamage;
            heroesDamages[heroesDamages.length - 4] += partOfDamage;
            System.out.println(heroesAttackTypes[heroesAttackTypes.length - 4] + " returns damage");
        }
    }

    public static void thorStunsBoss() { // Д/З (Thor)
        Random ran = new Random();
        int r = ran.nextInt(2);
        if (heroesHealth[heroesHealth.length - 5] != 0) { // Thor всегда пятый с конца в массивах
            if (r == 1) {
                bossDamage = 0;
                isBossStunned = true;
                System.out.println("Boss is stunned");
            }
        } else {
            bossDamage = defaultBossDamage;
            isBossStunned = false;
        }
    }

    public static void changeBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackTypes.length);
        bossDefenceType = heroesAttackTypes[randomIndex];
        System.out.println("Boss chooses " + bossDefenceType);
    }

    public static void printStatistics() {
        System.out.println("_________________________");
        System.out.println("Boss's health: " + bossHealth);
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackTypes[i] + " hero's health: " + heroesHealth[i]);
        }
        System.out.println("_________________________");
    }

    public static void bossHits() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] = heroesHealth[i] - bossDamage;
                }
            }
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamages.length - 1; i++) { // кроме последнего, т.е. медика
            if (heroesHealth[i] > 0) {
                if (bossHealth > 0) {
                    if (bossDefenceType.equals(heroesAttackTypes[i])) {
                        Random random = new Random();
                        int coeff = random.nextInt(7) + 2; // 2,3,4,5,6,7,8,9
                        System.out.println("Critical damage = " + heroesDamages[i] * coeff);
                        if (bossHealth - heroesDamages[i] * coeff < 0) {
                            bossHealth = 0;
                        } else {
                            bossHealth = bossHealth - heroesDamages[i] * coeff;
                        }
                    }
                    if (bossHealth - heroesDamages[i] < 0) {
                        bossHealth = 0;
                    } else {
                        bossHealth = bossHealth - heroesDamages[i];
                    }
                }
            }
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }
}
